
package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;

import java.util.Comparator;
import java.util.Locale;

public class ComparadorFoco {

    public static Comparator<FocoIncendio> criar(String criterio) {

        if (criterio == null) {
            throw new IllegalArgumentException("O critério não pode ser nulo.");
        }

        return switch (criterio.toLowerCase(Locale.ROOT).trim()) {
            case "data" ->
                    Comparator.comparing(
                            FocoIncendio::getData,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    );

            case "bioma" ->
                    Comparator.comparing(
                            FocoIncendio::getBioma,
                            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                    );

            case "municipio" ->
                    Comparator.comparing(
                            FocoIncendio::getMunicipio,
                            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Critério de ordenação inválido: " + criterio
                    );
        };
    }
}

