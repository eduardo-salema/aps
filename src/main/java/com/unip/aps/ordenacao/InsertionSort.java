
package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InsertionSort implements Ordenador {

    @Override
    public ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio) {

        List<FocoIncendio> lista = new ArrayList<>(dados);

        Comparator<FocoIncendio> comparador =
                ComparadorFoco.criar(criterio);

        long comparacoes = 0;
        long substituicoes = 0;

        long inicio = System.nanoTime();

        for (int i = 1; i < lista.size(); i++) {

            FocoIncendio atual = lista.get(i);
            int j = i - 1;

            while (j >= 0) {
                comparacoes++;

                if (comparador.compare(lista.get(j), atual) > 0) {
                    lista.set(j + 1, lista.get(j));
                    substituicoes++;
                    j--;
                } else {
                    break;
                }
            }

            if (j + 1 != i) {
                lista.set(j + 1, atual);
                substituicoes++;
            }
        }

        long tempo = System.nanoTime() - inicio;

        return new ResultadoOrdenacao(
                comparacoes,
                substituicoes,
                tempo
        );
    }
}
