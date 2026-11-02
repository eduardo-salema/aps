
package com.unip.aps.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe responsável por armazenar os resultados de uma execução
 * de qualquer algoritmo de ordenação do projeto.
 *
 * Guarda a lista ordenada e as métricas utilizadas para avaliar
 * o desempenho dos algoritmos.
 */
public class ResultadoOrdenacao {

    // Lista contendo os focos de incêndio após a ordenação.
    private final List<FocoIncendio> dadosOrdenados;

    // Quantidade de comparações realizadas entre os elementos.
    private final long comparacoes;

    // Quantidade de gravações realizadas na lista principal.
    private final long substituicoes;

    // Tempo total de execução do algoritmo, em nanossegundos.
    private final long tempoNanos;

    /**
     * Construtor responsável por receber e armazenar
     * os resultados da execução do algoritmo.
     *
     * @param dadosOrdenados Lista de focos de incêndio ordenada.
     * @param comparacoes Quantidade de comparações realizadas.
     * @param substituicoes Quantidade de substituições realizadas.
     * @param tempoNanos Tempo de execução em nanossegundos.
     */
    public ResultadoOrdenacao(
            List<FocoIncendio> dadosOrdenados,
            long comparacoes,
            long substituicoes,
            long tempoNanos) {

        // Cria uma cópia da lista para evitar compartilhar
        // diretamente a lista original recebida.
        this.dadosOrdenados = new ArrayList<>(dadosOrdenados);

        this.comparacoes = comparacoes;
        this.substituicoes = substituicoes;
        this.tempoNanos = tempoNanos;
    }

    /**
     * Retorna a lista de focos de incêndio ordenada.
     *
     * @return Lista ordenada.
     */
    public List<FocoIncendio> getDadosOrdenados() {
        return dadosOrdenados;
    }

    /**
     * Retorna a quantidade de comparações realizadas.
     *
     * @return Número de comparações.
     */
    public long getComparacoes() {
        return comparacoes;
    }

    /**
     * Retorna a quantidade de substituições realizadas.
     *
     * @return Número de substituições.
     */
    public long getSubstituicoes() {
        return substituicoes;
    }

    /**
     * Retorna o tempo de execução em nanossegundos.
     *
     * @return Tempo em nanossegundos.
     */
    public long getTempoNanos() {
        return tempoNanos;
    }

    /**
     * Converte o tempo de execução de nanossegundos
     * para milissegundos.
     *
     * @return Tempo em milissegundos.
     */
    public double getTempoMs() {
        return tempoNanos / 1_000_000.0;
    }
}
