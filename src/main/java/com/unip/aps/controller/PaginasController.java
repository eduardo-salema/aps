package com.unip.aps.controller;

import com.unip.aps.model.ResultadoCarga;
import com.unip.aps.service.CarregadorDadosService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginasController {

    private final CarregadorDadosService carregadorDadosService;

    public PaginasController(CarregadorDadosService carregadorDadosService) {
        this.carregadorDadosService = carregadorDadosService;
    }

    @GetMapping("/dados")
    public String dados(Model model) {
        ResultadoCarga resultado = carregadorDadosService.getUltimoResultado();

        model.addAttribute("totalFocos", resultado.getTotalFocos());
        model.addAttribute("linhasLidas", resultado.getLinhasLidas());
        model.addAttribute("linhasInvalidas", resultado.getLinhasInvalidas());
        model.addAttribute("arquivos", resultado.getArquivos());
        model.addAttribute("focos", carregadorDadosService.getFocos());
        model.addAttribute("biomas", carregadorDadosService.getBiomas());
        model.addAttribute("municipios", carregadorDadosService.getMunicipios());
        model.addAttribute("anos", carregadorDadosService.getAnos());

        return "dados";
    }
}