
package com.unip.aps.ordenacao;

import com.unip.aps.model.FocoIncendio;

import java.util.Comparator;
import java.util.Locale;

/**
 * Classe responsável por criar os comparadores utilizados
 * pelos algoritmos de ordenação.
 *
 * Permite escolher qual atributo dos focos de incêndio
 * será utilizado na ordenação: data, bioma ou município.
 */
public class ComparadorFoco {

    /**
     * Cria um comparador de acordo com o critério informado.
     *
     * @param criterio Critério de ordenação:
     *                 "data", "bioma" ou "municipio".
     *
     * @return Comparator utilizado para comparar dois focos de incêndio.
     *
     * @throws IllegalArgumentException caso o critério seja nulo
     *                                  ou inválido.
     */
    public static Comparator<FocoIncendio> criar(String criterio) {

        // Verifica se o critério recebido não é nulo.
        if (criterio == null) {
            throw new IllegalArgumentException(
                    "O critério não pode ser nulo."
            );
        }

        // Converte o critério para letras minúsculas e remove
        // espaços desnecessários no início e no final.
        // Locale.ROOT evita variações de conversão conforme o idioma.
        return switch (criterio.toLowerCase(Locale.ROOT).trim()) {

            // Ordena os focos pela data, da mais antiga para a mais recente.
            // Caso a data seja nula, o elemento será colocado no final.
            case "data" ->
                    Comparator.comparing(
                            FocoIncendio::getData,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    );

            // Ordena os focos pelo nome do bioma.
            // A comparação não diferencia letras maiúsculas de minúsculas.
            // Valores nulos são colocados no final.
            case "bioma" ->
                    Comparator.comparing(
                            FocoIncendio::getBioma,
                            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                    );

            // Ordena os focos pelo nome do município.
            // A comparação também ignora diferenças entre maiúsculas
            // e minúsculas. Valores nulos ficam no final.
            case "municipio" ->
                    Comparator.comparing(
                            FocoIncendio::getMunicipio,
                            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                    );

            // Caso o critério não corresponda a nenhuma das opções,
            // informa que o critério de ordenação é inválido.
            default ->
                    throw new IllegalArgumentException(
                            "Critério de ordenação inválido: " + criterio
                    );
        };
    }
}
