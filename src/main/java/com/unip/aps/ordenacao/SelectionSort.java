package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Implementação do algoritmo Selection Sort.
// Organiza os focos de incêndio de acordo com o critério escolhido.
public class SelectionSort implements Ordenador {

    @Override
    public ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio) {

        // Cria uma cópia da lista original para não modificar os dados recebidos.
        List<FocoIncendio> lista = new ArrayList<>(dados);

        // Cria o comparador responsável por definir a ordem dos elementos.
        // O critério pode ser data, bioma ou município.
        Comparator<FocoIncendio> comparador =
                ComparadorFoco.criar(criterio);

        // Contabiliza quantas comparações e substituições são realizadas.
        long comparacoes = 0;
        long substituicoes = 0;

        // Registra o instante inicial para calcular o tempo de execução.
        long inicio = System.nanoTime();

        // Percorre a lista procurando o menor elemento de cada posição.
        for (int i = 0; i < lista.size() - 1; i++) {

            // Considera inicialmente que o primeiro elemento
            // da posição atual é o menor.
            int indiceMenor = i;

            // Percorre os elementos seguintes procurando um menor.
            for (int j = i + 1; j < lista.size(); j++) {

                // Registra uma comparação entre dois elementos.
                comparacoes++;

                // Verifica se o elemento atual é menor que o menor encontrado.
                if (comparador.compare(
                        lista.get(j),
                        lista.get(indiceMenor)) < 0) {

                    // Atualiza a posição do menor elemento encontrado.
                    indiceMenor = j;
                }
            }

            // Só realiza a troca se encontrou um elemento menor
            // em uma posição diferente da posição atual.
            if (indiceMenor != i) {

                // Guarda temporariamente o elemento da posição atual.
                FocoIncendio temporario = lista.get(i);

                // Coloca o menor elemento na posição correta.
                lista.set(i, lista.get(indiceMenor));

                // Coloca o elemento guardado na posição anterior do menor.
                lista.set(indiceMenor, temporario);

                // Cada troca realiza duas gravações na lista principal.
                substituicoes += 2;
            }
        }

        // Calcula o tempo total de execução em nanossegundos.
        long tempo = System.nanoTime() - inicio;

        // Retorna a lista ordenada e as métricas coletadas.
        return new ResultadoOrdenacao(
                lista,
                comparacoes,
                substituicoes,
                tempo
        );
    }
}
