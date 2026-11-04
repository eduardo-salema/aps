package com.unip.aps.controller;

import com.unip.aps.model.ResultadoOrdenacao;
import com.unip.aps.service.CarregadorDadosService;
import com.unip.aps.service.OrdenacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;
import java.util.Map;

@Controller
public class OrdenacaoControler {

    private final OrdenacaoService ordenacaoService;
    private final CarregadorDadosService carregadorDadosService;

    public OrdenacaoControler(OrdenacaoService ordenacaoService, CarregadorDadosService carregadorDadosService) {
        this.ordenacaoService = ordenacaoService;
        this.carregadorDadosService = carregadorDadosService;
    }

    @GetMapping({"/resultados", "/ordenar"})
    public String resultados(
            @RequestParam(defaultValue = "quick") String algoritmo,
            @RequestParam(defaultValue = "data") String criterio,
            @RequestParam(required = false) Integer limite,
            Model model) {

        if (carregadorDadosService.temDados()) {
            ResultadoOrdenacao resultado = ordenacaoService.executarOrdenacao(algoritmo, criterio, limite);

            long totalOperacoes = resultado.getComparacoes() + resultado.getSubstituicoes();
            String tempoFormatado = String.format(Locale.US, "%.2f", resultado.getTempoMs());

            model.addAttribute("algoritmoSelecionado", ordenacaoService.formatarNomeAlgoritmo(algoritmo));
            model.addAttribute("criterioSelecionado", criterio);
            model.addAttribute("totalOperacoes", totalOperacoes);
            model.addAttribute("comparacoes", resultado.getComparacoes());
            model.addAttribute("substituicoes", resultado.getSubstituicoes());
            model.addAttribute("tempoExecucaoMs", tempoFormatado);
            model.addAttribute("totalOrdenado", resultado.getDadosOrdenados().size());
            model.addAttribute("dadosOrdenados", resultado.getDadosOrdenados());

            // Compara todos os 5 algoritmos (limitando a 2000 registros caso nenhum limite seja passado para não travar nos algoritmos O(n²))
            Integer limiteComparacao = (limite != null) ? limite : 2000;
            Map<String, ResultadoOrdenacao> comparativo = ordenacaoService.compararTodos(criterio, limiteComparacao);
            model.addAttribute("comparativoAlgoritmos", comparativo);
        }

        return "resultados";
    }
}