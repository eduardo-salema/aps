
package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementação do algoritmo Bubble Sort.
 *
 * Esse algoritmo percorre a lista comparando elementos vizinhos.
 * Quando encontra dois elementos fora de ordem, realiza a troca
 * entre eles.
 *
 * O processo é repetido até que todos os elementos estejam ordenados.
 */
public class BubbleSort implements Ordenador {

    /**
     * Executa a ordenação dos focos de incêndio utilizando Bubble Sort.
     *
     * @param dados Lista original de focos de incêndio.
     * @param criterio Critério utilizado: data, bioma ou municipio.
     *
     * @return ResultadoOrdenacao com a lista ordenada e as métricas
     *         de desempenho do algoritmo.
     */
    @Override
    public ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio) {

        // Cria uma cópia da lista para não modificar os dados originais.
        List<FocoIncendio> lista = new ArrayList<>(dados);

        // Obtém o comparador responsável pelo critério escolhido.
        Comparator<FocoIncendio> comparador =
                ComparadorFoco.criar(criterio);

        // Contador de comparações realizadas entre os elementos.
        long comparacoes = 0;

        // Contador de gravações realizadas na lista principal.
        long substituicoes = 0;

        // Registra o instante inicial da execução.
        long inicio = System.nanoTime();

        // Percorre a lista repetidamente.
        // A cada passagem, o maior elemento restante vai para o final.
        for (int i = 0; i < lista.size() - 1; i++) {

            // Indica se ocorreu alguma troca durante a passagem.
            boolean trocou = false;

            // Compara elementos vizinhos.
            // O limite diminui porque os últimos elementos já estão ordenados.
            for (int j = 0; j < lista.size() - 1 - i; j++) {

                // Registra uma comparação entre dois elementos.
                comparacoes++;

                // Verifica se os elementos estão fora de ordem.
                if (comparador.compare(
                        lista.get(j),
                        lista.get(j + 1)) > 0) {

                    // Guarda temporariamente o elemento da posição atual.
                    FocoIncendio temporario = lista.get(j);

                    // Coloca o próximo elemento na posição atual.
                    lista.set(j, lista.get(j + 1));

                    // Coloca o elemento temporário na posição seguinte.
                    lista.set(j + 1, temporario);

                    // Cada troca realiza duas gravações na lista principal.
                    substituicoes += 2;

                    // Informa que ocorreu uma troca nesta passagem.
                    trocou = true;
                }
            }

            // Se nenhuma troca ocorreu, a lista já está ordenada.
            // O algoritmo pode encerrar antecipadamente.
            if (!trocou) {
                break;
            }
        }

        // Calcula o tempo total de execução em nanossegundos.
        long tempo = System.nanoTime() - inicio;

        // Retorna a lista ordenada e todas as métricas coletadas.
        return new ResultadoOrdenacao(
                lista,
                comparacoes,
                substituicoes,
                tempo
        );
    }
}
