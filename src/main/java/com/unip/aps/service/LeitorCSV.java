package com.unip.aps.service;

import com.unip.aps.exception.ErroLeituraCsvException;
import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoCarga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// Lê um arquivo CSV do INPE e transforma cada linha em um FocoIncendio.
// Pontos principais:
//  - descobre as colunas pelo NOME no cabeçalho (não importa a ordem delas);
//  - linha com problema é pulada e anotada em ResultadoCarga, sem parar a leitura;
//  - só dá erro grave (exceção) se o arquivo for vazio ou faltar coluna obrigatória.
public class LeitorCSV {

    // Formatos de data que aceitamos. O CSV do ES usa o primeiro (2023-01-03 16:13:00).
    private static final DateTimeFormatter[] FORMATOS_DATA = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    };

    // Máximo de mensagens de erro guardadas por leitura (para não encher a memória se o arquivo for todo ruim).
    private final int maxErros;

    public LeitorCSV() {
        this(100);
    }

    public LeitorCSV(int maxErros) {
        this.maxErros = maxErros;
    }

    // Método principal: recebe o arquivo aberto e devolve os focos lidos + estatísticas.
    public ResultadoCarga ler(InputStream entrada, String nomeArquivo) {
        long inicio = System.currentTimeMillis();
        ResultadoCarga resultado = new ResultadoCarga();
        resultado.getArquivos().add(nomeArquivo);

        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(entrada, StandardCharsets.UTF_8))) {

            // 1) Primeira linha = cabeçalho com o nome das colunas.
            String cabecalho = leitor.readLine();
            if (cabecalho == null || cabecalho.isBlank()) {
                throw new ErroLeituraCsvException("Arquivo vazio: " + nomeArquivo);
            }
            // Alguns arquivos começam com um caractere invisível (BOM) que atrapalha o nome da 1ª coluna.
            if (cabecalho.charAt(0) == '\uFEFF') {
                cabecalho = cabecalho.substring(1);
            }

            // 2) Descobre em qual posição está cada coluna. "exigir" dá erro se não existir; "opcional" devolve -1.
            char separador = detectarSeparador(cabecalho);
            Map<String, Integer> colunas = mapearColunas(parseLinha(cabecalho, separador));
            int iData = exigir(colunas, nomeArquivo, "data_hora_gmt", "data_pas", "datahora", "data_hora", "data");
            int iLat = exigir(colunas, nomeArquivo, "lat", "latitude");
            int iLon = exigir(colunas, nomeArquivo, "lon", "longitude");
            int iMun = exigir(colunas, nomeArquivo, "municipio");
            int iBioma = exigir(colunas, nomeArquivo, "bioma");
            int iSat = opcional(colunas, "satelite");
            int iEst = opcional(colunas, "estado");
            int iPais = opcional(colunas, "pais");
            int iDias = opcional(colunas, "numero_dias_sem_chuva", "dias_sem_chuva");
            int iPrec = opcional(colunas, "precipitacao");
            int iRisco = opcional(colunas, "risco_fogo");
            int iFrp = opcional(colunas, "frp");

            // 3) Lê as demais linhas, uma por foco.
            String linha;
            long numeroLinha = 1;
            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;
                if (linha.isBlank()) continue;
                resultado.incrementarLidas();
                try {
                    List<String> c = parseLinha(linha, separador);
                    LocalDateTime dataHora = parseData(valor(c, iData));
                    double lat = parseCoordenada(valor(c, iLat), -90, 90, "latitude");
                    double lon = parseCoordenada(valor(c, iLon), -180, 180, "longitude");

                    resultado.getFocos().add(new FocoIncendio(
                            dataHora, lat, lon,
                            valor(c, iSat), valor(c, iMun), valor(c, iEst), valor(c, iPais),
                            valor(c, iBioma),
                            parseOpcional(valor(c, iDias)),
                            parseOpcional(valor(c, iPrec)),
                            parseOpcional(valor(c, iRisco)),
                            parseOpcional(valor(c, iFrp))));
                } catch (IllegalArgumentException e) {
                    // Linha ruim: conta, anota o motivo e segue para a próxima.
                    resultado.incrementarInvalidas();
                    if (resultado.getErros().size() < maxErros) {
                        resultado.getErros().add(nomeArquivo + " (linha " + numeroLinha + "): " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new ErroLeituraCsvException("Falha ao ler o arquivo " + nomeArquivo + ": " + e.getMessage(), e);
        }

        resultado.setTempoMs(System.currentTimeMillis() - inicio);
        return resultado;
    }

    // ---------- cabeçalho ----------

    // O CSV do INPE usa vírgula; se tiver mais ';' que ',' na primeira linha, usamos ';'.
    private static char detectarSeparador(String cabecalho) {
        long virgulas = cabecalho.chars().filter(ch -> ch == ',').count();
        long pontoVirgulas = cabecalho.chars().filter(ch -> ch == ';').count();
        return pontoVirgulas > virgulas ? ';' : ',';
    }

    // Monta um mapa "nome da coluna -> posição". Ex.: "municipio" -> 7.
    private static Map<String, Integer> mapearColunas(List<String> cabecalho) {
        Map<String, Integer> mapa = new HashMap<>();
        for (int i = 0; i < cabecalho.size(); i++) {
            mapa.putIfAbsent(normalizar(cabecalho.get(i)), i);
        }
        return mapa;
    }

    // Deixa o nome da coluna em minúsculas e sem acento, para comparar sem erro.
    private static String normalizar(String s) {
        String semAcento = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.trim().toLowerCase(Locale.ROOT);
    }

    // Procura a coluna por um dos nomes aceitos; se não achar nenhum, lança erro (coluna obrigatória).
    private static int exigir(Map<String, Integer> colunas, String arquivo, String... nomes) {
        int i = opcional(colunas, nomes);
        if (i < 0) {
            throw new ErroLeituraCsvException("Coluna obrigatória ausente (" + nomes[0]
                    + ") no arquivo " + arquivo + ". Colunas encontradas: " + colunas.keySet());
        }
        return i;
    }

    // Igual ao exigir, mas devolve -1 se a coluna não existir (o campo fica vazio no FocoIncendio).
    private static int opcional(Map<String, Integer> colunas, String... nomes) {
        for (String n : nomes) {
            Integer i = colunas.get(n);
            if (i != null) return i;
        }
        return -1;
    }

    // ---------- linhas e campos ----------

    // Separa uma linha em campos. Faz na mão (em vez de split) para aguentar texto entre aspas
    // que contenha vírgula, como "SÃO, PAULO".
    static List<String> parseLinha(String linha, char separador) {
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean entreAspas = false;
        for (int i = 0; i < linha.length(); i++) {
            char ch = linha.charAt(i);
            if (entreAspas) {
                if (ch == '"') {
                    if (i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                        atual.append('"');
                        i++;
                    } else {
                        entreAspas = false;
                    }
                } else {
                    atual.append(ch);
                }
            } else if (ch == '"') {
                entreAspas = true;
            } else if (ch == separador) {
                campos.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(ch);
            }
        }
        campos.add(atual.toString());
        return campos;
    }

    // Pega o campo na posição indicada, sem espaços. Devolve null se estiver vazio ou a coluna não existir.
    private static String valor(List<String> campos, int indice) {
        if (indice < 0 || indice >= campos.size()) return null;
        String v = campos.get(indice).trim();
        return v.isEmpty() ? null : v;
    }

    // Converte o texto da data tentando cada formato; se nenhum servir, a linha é considerada inválida.
    static LocalDateTime parseData(String texto) {
        if (texto == null) throw new IllegalArgumentException("data/hora ausente");
        String t = texto.trim();
        if (t.length() > 19 && t.contains(".")) t = t.substring(0, t.indexOf('.'));
        for (DateTimeFormatter f : FORMATOS_DATA) {
            try {
                return LocalDateTime.parse(t, f);
            } catch (DateTimeParseException ignorado) {
                // não era esse formato, tenta o próximo
            }
        }
        throw new IllegalArgumentException("data/hora inválida: '" + texto + "'");
    }

    // Converte latitude/longitude e confere se está dentro do intervalo possível (lat -90..90, lon -180..180).
    private static double parseCoordenada(String texto, double min, double max, String nome) {
        if (texto == null) throw new IllegalArgumentException(nome + " ausente");
        try {
            double v = Double.parseDouble(texto.replace(',', '.'));
            if (Double.isNaN(v) || v < min || v > max) {
                throw new IllegalArgumentException(nome + " fora do intervalo [" + min + ", " + max + "]: " + texto);
            }
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nome + " inválida: '" + texto + "'");
        }
    }

    // Número que pode faltar. Vazio, texto inválido ou -999 (valor que o INPE usa para "sem dado") viram null.
    private static Double parseOpcional(String texto) {
        if (texto == null) return null;
        try {
            double v = Double.parseDouble(texto.replace(',', '.'));
            return (Double.isNaN(v) || v <= -999) ? null : v;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
