package com.unip.aps.model;

import java.util.Comparator;
import java.util.Locale;

public class ComparadorFoco {
    public static Comparator<FocoIncendio> criar(String criterio){

        return switch (criterio.toLowerCase()){
            case "data" ->
                Comparator.comparing(FocoIncendio::getData, Comparator.nullsLast(Comparator.naturalOrder()));

            case "bioma" ->
                Comparator.comparing(FocoIncendio::getBioma, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));

            case "municipio" ->
                Comparator.comparing(FocoIncendio::getMunicipio, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));

            default ->
                throw new IllegalArgumentException("Critério de ordenação inválido: " + criterio);
        };

    }
}
