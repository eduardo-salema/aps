package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Implementação do algoritmo Insertion Sort.
// Organiza os focos de incêndio inserindo cada elemento
// na posição correta dentro da parte já ordenada da lista.
public class InsertionSort implements Ordenador {

    @Override
    public ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio) {

        // Cria uma cópia da lista original para preservar os dados recebidos.
        List<FocoIncendio> lista = new ArrayList<>(dados);

        // Cria o comparador conforme o critério escolhido:
        // data, bioma ou município.
        Comparator<FocoIncendio> comparador =
                ComparadorFoco.criar(criterio);

        // Inicializa os contadores de comparações e substituições.
        long comparacoes = 0;
        long substituicoes = 0;

        // Registra o instante inicial da execução.
        long inicio = System.nanoTime();

        // Começa pelo segundo elemento, pois o primeiro
        // é considerado inicialmente uma parte ordenada.
        for (int i = 1; i < lista.size(); i++) {

            // Guarda o elemento que será inserido na posição correta.
            FocoIncendio atual = lista.get(i);

            // Começa a comparação pelo elemento anterior ao atual.
            int j = i - 1;

            // Percorre os elementos anteriores enquanto existirem.
            while (j >= 0) {

                // Registra uma comparação entre elementos.
                comparacoes++;

                // Verifica se o elemento anterior deve ficar
                // depois do elemento que estamos inserindo.
                if (comparador.compare(lista.get(j), atual) > 0) {

                    // Desloca o elemento uma posição para a direita.
                    lista.set(j + 1, lista.get(j));

                    // Conta uma gravação realizada na lista principal.
                    substituicoes++;

                    // Continua verificando as posições anteriores.
                    j--;

                } else {

                    // Se encontrou um elemento menor ou igual,
                    // não é necessário continuar procurando.
                    break;
                }
            }

            // Verifica se o elemento saiu da posição original.
            if (j + 1 != i) {

                // Insere o elemento guardado na posição correta.
                lista.set(j + 1, atual);

                // Conta a gravação realizada na lista principal.
                substituicoes++;
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
