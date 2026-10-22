package com.unip.aps.service;

import com.unip.aps.exception.ErroLeituraCsvException;
import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoCarga;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

// Guarda na memória todos os focos lidos dos CSV.
// É a classe que os outros módulos (ordenação, relatórios, painel) devem usar para pegar os dados.
// O Spring cria uma única instância dela; é só pedir no construtor da sua classe.
@Service
public class CarregadorDadosService {

    private static final Logger log = LoggerFactory.getLogger(CarregadorDadosService.class);

    // Pasta dos CSV e limite de erros guardados; os valores vêm do application.properties.
    private final Path diretorio;
    private final int maxErros;

    // Dados atuais em memória. "volatile" para todas as threads enxergarem a versão mais nova.
    private volatile List<FocoIncendio> focos = List.of();
    private volatile ResultadoCarga ultimoResultado = new ResultadoCarga();

    public CarregadorDadosService(@Value("${aps.dados.diretorio:dados}") String diretorio,
                                  @Value("${aps.dados.max-erros:100}") int maxErros) {
        this.diretorio = Paths.get(diretorio);
        this.maxErros = maxErros;
    }

    // Roda sozinho quando o projeto inicia e já carrega a pasta "dados".
    // Se der erro, só avisa no log e o projeto continua subindo.
    @PostConstruct
    void carregarNaInicializacao() {
        try {
            recarregar();
        } catch (ErroLeituraCsvException e) {
            log.warn("Carga inicial não realizada: {}", e.getMessage());
        }
    }

    // Lê de novo todos os .csv da pasta "dados" e troca os dados da memória.
    // Pode ser chamado por um botão "Recarregar dados" na tela.
    public synchronized ResultadoCarga recarregar() {
        if (!Files.isDirectory(diretorio)) {
            throw new ErroLeituraCsvException("Pasta de dados não encontrada: " + diretorio.toAbsolutePath());
        }
        List<Path> arquivos;
        try (Stream<Path> s = Files.list(diretorio)) {
            arquivos = s.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".csv"))
                        .sorted()
                        .toList();
        } catch (IOException e) {
            throw new ErroLeituraCsvException("Não foi possível listar " + diretorio + ": " + e.getMessage(), e);
        }
        if (arquivos.isEmpty()) {
            throw new ErroLeituraCsvException("Nenhum arquivo .csv em " + diretorio.toAbsolutePath());
        }

        LeitorCSV leitor = new LeitorCSV(maxErros);
        ResultadoCarga total = new ResultadoCarga();
        for (Path arquivo : arquivos) {
            try (InputStream in = Files.newInputStream(arquivo)) {
                total.incorporar(leitor.ler(in, arquivo.getFileName().toString()));
            } catch (IOException e) {
                total.getErros().add("Falha ao abrir " + arquivo.getFileName() + ": " + e.getMessage());
            } catch (ErroLeituraCsvException e) {
                // um arquivo ruim não impede a leitura dos outros
                total.getErros().add(e.getMessage());
            }
        }
        publicar(total);
        return total;
    }

    // Carrega um CSV enviado pelo usuário (upload na tela) e SUBSTITUI os dados que estavam na memória.
    public synchronized ResultadoCarga carregarDeUpload(InputStream in, String nomeArquivo) {
        ResultadoCarga r = new LeitorCSV(maxErros).ler(in, nomeArquivo);
        publicar(r);
        return r;
    }

    // Passa a usar o resultado da leitura como dados atuais e escreve um resumo no console.
    private void publicar(ResultadoCarga r) {
        this.focos = Collections.unmodifiableList(new ArrayList<>(r.getFocos()));
        this.ultimoResultado = r;
        log.info("Carga concluída: {} focos válidos, {} linhas inválidas, {} arquivo(s), {} ms",
                r.getTotalFocos(), r.getLinhasInvalidas(), r.getArquivos().size(), r.getTempoMs());
        r.getErros().stream().limit(20).forEach(e -> log.warn("Erro de carga: {}", e));
    }

    // ---------- acesso aos dados ----------

    // Lista só para leitura (não dá para adicionar/remover/reordenar). Use para exibir ou filtrar.
    public List<FocoIncendio> getFocos() {
        return focos;
    }

    // PARA QUEM FAZ A ORDENAÇÃO: devolve um array NOVO com os focos.
    // Ordene essa cópia, assim a lista original fica intacta e cada pedido de exibição
    // começa dos dados originais (importante para a contagem de operações ficar correta).
    public FocoIncendio[] copiaParaOrdenacao() {
        return focos.toArray(new FocoIncendio[0]);
    }

    // Números e erros da última leitura (para relatório de qualidade dos dados).
    public ResultadoCarga getUltimoResultado() {
        return ultimoResultado;
    }

    public boolean temDados() {
        return !focos.isEmpty();
    }

    // ---------- valores distintos (para filtros e listas na tela) ----------
    // TreeSet só elimina repetidos e deixa em ordem alfabética; não faz parte da ordenação do trabalho.

    public List<String> getBiomas() {
        TreeSet<String> s = new TreeSet<>();
        for (FocoIncendio f : focos) s.add(f.getBioma());
        return new ArrayList<>(s);
    }

    public List<String> getMunicipios() {
        TreeSet<String> s = new TreeSet<>();
        for (FocoIncendio f : focos) s.add(f.getMunicipio());
        return new ArrayList<>(s);
    }

    public List<Integer> getAnos() {
        TreeSet<Integer> s = new TreeSet<>();
        for (FocoIncendio f : focos) s.add(f.getAno());
        return new ArrayList<>(s);
    }
}
