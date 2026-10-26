package com.unip.aps.model;

public class ResultadoOrdenacao {
    private long comparacoes;
    private long substituicoes;
    private long tempoNanos;

    public ResultadoOrdenacao(long comparacoes, long substituicoes, long tempoNanos){
        this.comparacoes = comparacoes;
        this.substituicoes = substituicoes;
        this.tempoNanos = tempoNanos;
    }

    public long getComparacoes() {
        return comparacoes;
    }

    public long getSubstituicoes() {
        return substituicoes;
    }

    public long getTempoNanos() {
        return tempoNanos;
    }

    public double getTempoMs(){
        return tempoNanos / 1_000_000.0;
    }
}
