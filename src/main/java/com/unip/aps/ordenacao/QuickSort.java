package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Implementação do algoritmo Quick Sort.
// Utiliza um pivô para separar os elementos menores e maiores,
// organizando a lista por meio de chamadas recursivas.
public class QuickSort implements Ordenador {

    // Classe auxiliar que armazena as métricas de cada execução.
    private static class Contadores {
        long comparacoes = 0;
        long substituicoes = 0;
    }

    @Override
    public ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio) {

        // Cria uma cópia da lista original para não modificar os dados recebidos.
        List<FocoIncendio> lista = new ArrayList<>(dados);

        // Cria o comparador conforme o critério escolhido:
        // data, bioma ou município.
        Comparator<FocoIncendio> comparador =
                ComparadorFoco.criar(criterio);

        // Cria os contadores de comparações e substituições.
        Contadores contadores = new Contadores();

        // Registra o instante inicial da execução.
        long inicio = System.nanoTime();

        // Verifica se a lista possui elementos antes de iniciar a ordenação.
        if (!lista.isEmpty()) {

            // Inicia a ordenação considerando toda a lista.
            ordenar(
                    lista,
                    0,
                    lista.size() - 1,
                    comparador,
                    contadores
            );
        }

        // Calcula o tempo total de execução em nanossegundos.
        long tempo = System.nanoTime() - inicio;

        // Retorna a lista ordenada e as métricas coletadas.
        return new ResultadoOrdenacao(
                lista,
                contadores.comparacoes,
                contadores.substituicoes,
                tempo
        );
    }

    // Método recursivo responsável por ordenar as partes da lista.
    private void ordenar(
            List<FocoIncendio> lista,
            int inicio,
            int fim,
            Comparator<FocoIncendio> comparador,
            Contadores contadores) {

        // Verifica se ainda existe mais de um elemento na parte analisada.
        if (inicio < fim) {

            // Organiza os elementos ao redor do pivô
            // e retorna a posição correta desse pivô.
            int posicaoPivo = particionar(
                    lista,
                    inicio,
                    fim,
                    comparador,
                    contadores
            );

            // Ordena recursivamente os elementos anteriores ao pivô.
            ordenar(
                    lista, inicio, posicaoPivo - 1,
                    comparador, contadores
            );

            // Ordena recursivamente os elementos posteriores ao pivô.
            ordenar(
                    lista, posicaoPivo + 1, fim,
                    comparador, contadores
            );
        }
    }

    // Método responsável por dividir a lista utilizando um pivô.
    private int particionar(
            List<FocoIncendio> lista,
            int inicio,
            int fim,
            Comparator<FocoIncendio> comparador,
            Contadores contadores) {

        // Escolhe o último elemento da parte analisada como pivô.
        FocoIncendio pivo = lista.get(fim);

        // Define a posição anterior ao início da lista.
        // Esse índice será atualizado conforme os elementos menores
        // ou iguais ao pivô forem encontrados.
        int i = inicio - 1;

        // Percorre os elementos até o elemento anterior ao pivô.
        for (int j = inicio; j < fim; j++) {

            // Registra uma comparação entre o elemento atual e o pivô.
            contadores.comparacoes++;

            // Verifica se o elemento atual é menor ou igual ao pivô.
            if (comparador.compare(lista.get(j), pivo) <= 0) {

                // Avança a posição dos elementos menores ou iguais ao pivô.
                i++;

                // Realiza a troca somente quando as posições são diferentes.
                if (i != j) {
                    trocar(lista, i, j, contadores);
                }
            }
        }

        // Coloca o pivô em sua posição definitiva.
        // Só realiza a troca se ele ainda não estiver nessa posição.
        if (i + 1 != fim) {
            trocar(lista, i + 1, fim, contadores);
        }

        // Retorna a posição final do pivô.
        return i + 1;
    }

    // Método responsável por trocar dois elementos de posição na lista.
    private void trocar(
            List<FocoIncendio> lista,
            int i,
            int j,
            Contadores contadores) {

        // Guarda temporariamente o elemento da primeira posição.
        FocoIncendio temporario = lista.get(i);

        // Coloca o elemento da segunda posição na primeira.
        lista.set(i, lista.get(j));

        // Coloca o elemento guardado na segunda posição.
        lista.set(j, temporario);

        // Cada troca realiza duas gravações na lista principal.
        contadores.substituicoes += 2;
    }
}
