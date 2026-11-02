package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Implementação do algoritmo Merge Sort.
// Utiliza a estratégia de dividir a lista em partes menores,
// ordenar essas partes e depois juntá-las novamente em ordem.
public class MergeSort implements Ordenador {

    // Classe auxiliar responsável por armazenar as métricas.
    // Os contadores são criados dentro de cada execução do algoritmo.
    private static class Contadores {
        long comparacoes = 0;
        long substituicoes = 0;
    }

    @Override
    public ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio) {

        // Cria uma cópia da lista original para preservar os dados recebidos.
        List<FocoIncendio> lista = new ArrayList<>(dados);

        // Cria o comparador conforme o critério escolhido:
        // data, bioma ou município.
        Comparator<FocoIncendio> comparador =
                ComparadorFoco.criar(criterio);

        // Cria os contadores de comparações e substituições.
        Contadores contadores = new Contadores();

        // Cria uma lista auxiliar utilizada durante a intercalação.
        List<FocoIncendio> auxiliar =
                new ArrayList<>(lista);

        // Registra o instante inicial da execução.
        long inicio = System.nanoTime();

        // Verifica se a lista possui elementos antes de iniciar a ordenação.
        if (!lista.isEmpty()) {

            // Inicia a ordenação recursiva considerando toda a lista.
            ordenar(
                    lista,
                    auxiliar,
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

    // Método responsável por dividir a lista em partes menores
    // até que cada parte tenha apenas um elemento.
    private void ordenar(
            List<FocoIncendio> lista,
            List<FocoIncendio> auxiliar,
            int inicio,
            int fim,
            Comparator<FocoIncendio> comparador,
            Contadores contadores) {

        // Condição de parada da recursividade.
        // Se o início for maior ou igual ao fim,
        // significa que a parte possui zero ou um elemento.
        if (inicio >= fim) {
            return;
        }

        // Calcula o ponto médio para dividir a lista em duas partes.
        int meio = inicio + (fim - inicio) / 2;

        // Ordena recursivamente a primeira metade da lista.
        ordenar(
                lista, auxiliar, inicio, meio,
                comparador, contadores
        );

        // Ordena recursivamente a segunda metade da lista.
        ordenar(
                lista, auxiliar, meio + 1, fim,
                comparador, contadores
        );

        // Depois de ordenar as duas metades,
        // realiza a intercalação dos elementos.
        intercalar(
                lista, auxiliar, inicio, meio, fim,
                comparador, contadores
        );
    }

    // Método responsável por juntar duas partes já ordenadas
    // em uma única sequência ordenada.
    private void intercalar(
            List<FocoIncendio> lista,
            List<FocoIncendio> auxiliar,
            int inicio,
            int meio,
            int fim,
            Comparator<FocoIncendio> comparador,
            Contadores contadores) {

        // Copia os elementos da lista principal para a lista auxiliar.
        // Essas cópias não são contabilizadas como substituições,
        // pois a métrica considera apenas gravações na lista principal.
        for (int i = inicio; i <= fim; i++) {
            auxiliar.set(i, lista.get(i));
        }

        // Índice que percorre a primeira metade da lista.
        int i = inicio;

        // Índice que percorre a segunda metade da lista.
        int j = meio + 1;

        // Percorre as posições que precisam receber os elementos ordenados.
        for (int k = inicio; k <= fim; k++) {

            // Verifica se todos os elementos da primeira metade
            // já foram utilizados.
            if (i > meio) {

                // Copia o próximo elemento da segunda metade
                // para a lista principal.
                lista.set(k, auxiliar.get(j++));

                // Conta uma gravação na lista principal.
                contadores.substituicoes++;

                // Verifica se todos os elementos da segunda metade
                // já foram utilizados.
            } else if (j > fim) {

                // Copia o próximo elemento da primeira metade
                // para a lista principal.
                lista.set(k, auxiliar.get(i++));

                // Conta uma gravação na lista principal.
                contadores.substituicoes++;

            } else {

                // Registra uma comparação entre os elementos
                // atuais das duas metades.
                contadores.comparacoes++;

                // Compara os elementos para decidir qual deve
                // ocupar a próxima posição da lista principal.
                if (comparador.compare(
                        auxiliar.get(i),
                        auxiliar.get(j)) <= 0) {

                    // Se o elemento da primeira metade for menor
                    // ou igual, ele é colocado na lista principal.
                    lista.set(k, auxiliar.get(i++));

                } else {

                    // Caso contrário, utiliza o elemento
                    // da segunda metade.
                    lista.set(k, auxiliar.get(j++));
                }

                // Conta a gravação realizada na lista principal.
                contadores.substituicoes++;
            }
        }
    }
}
