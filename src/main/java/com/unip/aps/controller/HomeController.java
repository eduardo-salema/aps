package com.unip.aps.controller;

import com.unip.aps.model.ResultadoCarga;
import com.unip.aps.service.CarregadorDadosService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final CarregadorDadosService carregadorDadosService;

    public HomeController(CarregadorDadosService carregadorDadosService) {
        this.carregadorDadosService = carregadorDadosService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ResultadoCarga resultado = carregadorDadosService.getUltimoResultado();

        model.addAttribute("totalFocos", resultado.getTotalFocos());
        model.addAttribute("linhasLidas", resultado.getLinhasLidas());
        model.addAttribute("linhasInvalidas", resultado.getLinhasInvalidas());
        model.addAttribute("tempoMs", resultado.getTempoMs());
        model.addAttribute("arquivos", resultado.getArquivos());
        model.addAttribute("erros", resultado.getErros());

        return "index";
    }

    @PostMapping("/recarregar")
    public String recarregar() {
        carregadorDadosService.recarregar();
        return "redirect:/";
    }
}