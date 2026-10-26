package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectionSort implements Ordenador{
    @Override
    public ResultadoOrdenacao ordenar(List<FocoIncendio> dados, String criterio) {
        List<FocoIncendio> lista = new ArrayList<>(dados);
        Comparator<FocoIncendio> comparador = ComparadorFoco.criar(criterio);
        long comparacoes = 0;
        long substituicoes = 0;
        long inicio = System.nanoTime();
        for (int i = 0; i < lista.size() - 1; i++) {
            int indiceMenor = i;
            for (int j = i + 1; j < lista.size(); j++) {
                comparacoes++;
                if (comparador.compare(lista.get(j), lista.get(indiceMenor)) < 0) {
                    indiceMenor = j;
                }
            }
            if (indiceMenor != i) {
                FocoIncendio temporario = lista.get(i);
                lista.set(i, lista.get(indiceMenor));
                lista.set(indiceMenor, temporario);
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
