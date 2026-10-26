
package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuickSort implements Ordenador {

    private long comparacoes;
    private long substituicoes;

    @Override
    public ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio) {

        List<FocoIncendio> lista = new ArrayList<>(dados);
        Comparator<FocoIncendio> comparador =
                ComparadorFoco.criar(criterio);

        comparacoes = 0;
        substituicoes = 0;

        long inicio = System.nanoTime();

        if (!lista.isEmpty()) {
            ordenar(lista, 0, lista.size() - 1, comparador);
        }

        long tempo = System.nanoTime() - inicio;

        return new ResultadoOrdenacao(
                comparacoes, substituicoes, tempo);
    }

    private void ordenar(
            List<FocoIncendio> lista,
            int inicio, int fim,
            Comparator<FocoIncendio> comparador) {

        if (inicio < fim) {
            int posicaoPivo = particionar(
                    lista, inicio, fim, comparador);

            ordenar(lista, inicio, posicaoPivo - 1, comparador);
            ordenar(lista, posicaoPivo + 1, fim, comparador);
        }
    }

    private int particionar(
            List<FocoIncendio> lista,
            int inicio, int fim,
            Comparator<FocoIncendio> comparador) {

        FocoIncendio pivo = lista.get(fim);
        int i = inicio - 1;

        for (int j = inicio; j < fim; j++) {
            comparacoes++;

            if (comparador.compare(lista.get(j), pivo) <= 0) {
                i++;

                if (i != j) {
                    trocar(lista, i, j);
                }
            }
        }

        if (i + 1 != fim) {
            trocar(lista, i + 1, fim);
        }

        return i + 1;
    }

    private void trocar(
            List<FocoIncendio> lista, int i, int j) {

        FocoIncendio temporario = lista.get(i);

        lista.set(i, lista.get(j));
        lista.set(j, temporario);

        // Três atribuições de referência por troca.
        substituicoes += 3;
    }
}
