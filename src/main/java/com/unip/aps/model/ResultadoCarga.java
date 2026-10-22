package com.unip.aps.model;

import java.util.ArrayList;
import java.util.List;

// Guarda o resultado da leitura dos CSV: os focos lidos + números e erros da leitura.
// Serve para o relatório de qualidade dos dados (quantas linhas foram lidas, quantas deram erro, tempo).
public class ResultadoCarga {

    private final List<FocoIncendio> focos = new ArrayList<>();
    private final List<String> arquivos = new ArrayList<>();
    // Mensagens de erro, uma por linha ruim (ex.: "ES 2023.csv (linha 10): latitude inválida").
    private final List<String> erros = new ArrayList<>();
    private long linhasLidas;
    private long linhasInvalidas;
    private long tempoMs;

    public List<FocoIncendio> getFocos() { return focos; }
    public List<String> getArquivos() { return arquivos; }
    public List<String> getErros() { return erros; }
    public long getLinhasLidas() { return linhasLidas; }
    public long getLinhasInvalidas() { return linhasInvalidas; }
    public long getTempoMs() { return tempoMs; }
    public int getTotalFocos() { return focos.size(); }

    public void setTempoMs(long tempoMs) { this.tempoMs = tempoMs; }
    public void incrementarLidas() { linhasLidas++; }
    public void incrementarInvalidas() { linhasInvalidas++; }

    // Junta o resultado de outro arquivo neste (usado para somar o CSV de 2023 com o de 2024).
    public void incorporar(ResultadoCarga outro) {
        focos.addAll(outro.focos);
        arquivos.addAll(outro.arquivos);
        erros.addAll(outro.erros);
        linhasLidas += outro.linhasLidas;
        linhasInvalidas += outro.linhasInvalidas;
        tempoMs += outro.tempoMs;
    }
}
