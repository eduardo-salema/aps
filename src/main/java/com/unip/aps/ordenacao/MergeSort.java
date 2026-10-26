
package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort implements Ordenador {

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

        List<FocoIncendio> auxiliar =
                new ArrayList<>(lista);

        long inicio = System.nanoTime();

        if (!lista.isEmpty()) {
            ordenar(lista, auxiliar, 0, lista.size() - 1,
                    comparador);
        }

        long tempo = System.nanoTime() - inicio;

        return new ResultadoOrdenacao(
                comparacoes, substituicoes, tempo);
    }

    private void ordenar(
            List<FocoIncendio> lista,
            List<FocoIncendio> auxiliar,
            int inicio, int fim,
            Comparator<FocoIncendio> comparador) {

        if (inicio >= fim) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;

        ordenar(lista, auxiliar, inicio, meio, comparador);
        ordenar(lista, auxiliar, meio + 1, fim, comparador);

        intercalar(lista, auxiliar, inicio, meio, fim,
                comparador);
    }

    private void intercalar(
            List<FocoIncendio> lista,
            List<FocoIncendio> auxiliar,
            int inicio, int meio, int fim,
            Comparator<FocoIncendio> comparador) {

        for (int i = inicio; i <= fim; i++) {
            auxiliar.set(i, lista.get(i));
            substituicoes++;
        }

        int i = inicio;
        int j = meio + 1;

        for (int k = inicio; k <= fim; k++) {

            if (i > meio) {
                lista.set(k, auxiliar.get(j++));
                substituicoes++;

            } else if (j > fim) {
                lista.set(k, auxiliar.get(i++));
                substituicoes++;

            } else {
                comparacoes++;

                if (comparador.compare(
                        auxiliar.get(i), auxiliar.get(j)) <= 0) {

                    lista.set(k, auxiliar.get(i++));
                } else {
                    lista.set(k, auxiliar.get(j++));
                }

                substituicoes++;
            }
        }
    }
}
