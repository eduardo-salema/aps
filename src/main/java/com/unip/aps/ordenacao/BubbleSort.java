package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BubbleSort implements Ordenador{
    @Override
    public ResultadoOrdenacao ordenar(List<FocoIncendio> dados, String criterio){
        List<FocoIncendio> lista = new ArrayList<>(dados);
        Comparator<FocoIncendio> comparador = ComparadorFoco.criar(criterio);

        long comparacoes = 0;
        long substituicoes = 0;

        long inicio = System.nanoTime();

        for (int i = 0; i < lista.size() - 1; i++){
            boolean trocou = false;

            for (int j = 0; j < lista.size() - 1 - i; j++){
                comparacoes++;

                if(comparador.compare(lista.get(j), lista.get(j + i)) > 0) {



                    FocoIncendio temporario = lista.get(j);

                    lista.set(j, lista.get(j + 1));
                    lista.set(j + i, temporario);

                    substituicoes++;
                    trocou = true;
                }
            }

            if (!trocou){
                break;
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
