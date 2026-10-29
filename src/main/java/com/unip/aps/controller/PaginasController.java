package com.unip.aps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginasController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/dados")
    public String dados() {
        return "dados";
    }

    @GetMapping("/resultados")
    public String resultados() {
        return "resultados";
    }
}