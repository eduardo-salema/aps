package com.unip.aps.service;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;
import com.unip.aps.ordenacao.BubbleSort;
import com.unip.aps.ordenacao.InsertionSort;
import com.unip.aps.ordenacao.MergeSort;
import com.unip.aps.ordenacao.Ordenador;
import com.unip.aps.ordenacao.QuickSort;
import com.unip.aps.ordenacao.SelectionSort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrdenacaoService {

    private final CarregadorDadosService carregadorDadosService;
    private final Map<String, Ordenador> algoritmos = new LinkedHashMap<>();

    public OrdenacaoService(CarregadorDadosService carregadorDadosService) {
        this.carregadorDadosService = carregadorDadosService;
        algoritmos.put("bubble", new BubbleSort());
        algoritmos.put("selection", new SelectionSort());
        algoritmos.put("insertion", new InsertionSort());
        algoritmos.put("merge", new MergeSort());
        algoritmos.put("quick", new QuickSort());
    }

    public ResultadoOrdenacao executarOrdenacao(String nomeAlgoritmo, String criterio, Integer limite) {
        String chave = (nomeAlgoritmo == null || nomeAlgoritmo.isBlank())
                ? "quick"
                : nomeAlgoritmo.toLowerCase().trim();

        Ordenador ordenador = algoritmos.getOrDefault(chave, algoritmos.get("quick"));

        String crit = (criterio == null || criterio.isBlank())
                ? "data"
                : criterio.toLowerCase().trim();

        // Cria uma nova lista a partir da cópia para não alterar os dados originais em memória
        List<FocoIncendio> copia = new ArrayList<>(Arrays.asList(carregadorDadosService.copiaParaOrdenacao()));

        // Opcional: permite limitar a quantidade de registos se quiserem testar algoritmos mais lentos (ex: BubbleSort)
        if (limite != null && limite > 0 && limite < copia.size()) {
            copia = new ArrayList<>(copia.subList(0, limite));
        }

        return ordenador.ordenar(copia, crit);
    }

    public Map<String, ResultadoOrdenacao> compararTodos(String criterio, Integer limite) {
        Map<String, ResultadoOrdenacao> resultados = new LinkedHashMap<>();
        for (String nome : algoritmos.keySet()) {
            resultados.put(nome, executarOrdenacao(nome, criterio, limite));
        }
        return resultados;
    }

    public String formatarNomeAlgoritmo(String chave) {
        if (chave == null) return "Quick Sort";
        return switch (chave.toLowerCase().trim()) {
            case "bubble" -> "Bubble Sort";
            case "selection" -> "Selection Sort";
            case "insertion" -> "Insertion Sort";
            case "merge" -> "Merge Sort";
            case "quick" -> "Quick Sort";
            default -> chave;
        };
    }
}