
package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.List;

/**
 * Interface responsável por padronizar os algoritmos de ordenação.
 *
 * Todos os algoritmos do projeto (BubbleSort, SelectionSort,
 * InsertionSort, MergeSort e QuickSort) devem implementar esta interface.
 *
 * Dessa forma, todos utilizam o mesmo método para ordenar os dados
 * e retornam um objeto com os resultados da execução.
 */
public interface Ordenador {

    /**
     * Executa a ordenação de uma lista de focos de incêndio.
     *
     * @param dados Lista de focos de incêndio que será ordenada.
     * @param criterio Critério utilizado na ordenação:
     *                 "data", "bioma" ou "municipio".
     *
     * @return ResultadoOrdenacao contendo:
     *         - Lista de dados ordenados;
     *         - Quantidade de comparações;
     *         - Quantidade de substituições;
     *         - Tempo de execução.
     */
    ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio
    );
}
