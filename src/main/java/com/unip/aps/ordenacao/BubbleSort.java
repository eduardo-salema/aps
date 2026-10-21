package com.unip.aps.ordenacao;

public class BubbleSort {

    public static class Estatisticas{
        public long comparacoes = 0;
        public long escritas = 0;
        public long trocas = 0;

        public long getTotalOperacoes(){
            return comparacoes + escritas;
        }
    }

    public void bubbleSort(int[] vetor, Estatisticas e){
        int n = vetor.length;

        for (int i = 0; i < n - 1; i++){
            boolean trocou = false;

            for (int j = 0; j < n -1 - i; j++){
                e.comparacoes++;

                if (vetor[j] > vetor[j + i]){
                    int temp = vetor[j];
                    vetor[j] = vetor[j + i];
                    vetor[j + i] = temp;

                    e.escritas += 2;
                    e.trocas++;
                    trocou = true;
                }
            }
            if (!trocou){
                break;
            }


        }
    }
}
