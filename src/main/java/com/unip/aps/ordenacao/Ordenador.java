package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;
import com.unip.aps.model.ResultadoOrdenacao;

import java.util.List;

public interface Ordenador {
    ResultadoOrdenacao ordenar(
            List<FocoIncendio> dados, String criterio
    );
}
