package com.unip.aps.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

// Representa UM foco de incêndio, ou seja, uma linha do CSV do INPE.
// É a classe que os algoritmos de ordenação vão comparar (por data, bioma e município).
// Depois de criado o objeto não muda (só tem getters), então ordenar só troca a posição na lista.
public class FocoIncendio {

    // Texto usado quando o CSV vem com município/bioma vazio, para nunca ter null na hora de comparar.
    public static final String NAO_INFORMADO = "Não informado";

    private final LocalDateTime dataHora;
    private final double latitude;
    private final double longitude;
    private final String satelite;
    private final String municipio;
    private final String estado;
    private final String pais;
    private final String bioma;
    // Os campos abaixo não existem no CSV do ES; ficam null quando não vêm no arquivo.
    private final Double diasSemChuva;
    private final Double precipitacao;
    private final Double riscoFogo;
    private final Double frp;

    public FocoIncendio(LocalDateTime dataHora, double latitude, double longitude,
                        String satelite, String municipio, String estado, String pais,
                        String bioma, Double diasSemChuva, Double precipitacao,
                        Double riscoFogo, Double frp) {
        this.dataHora = Objects.requireNonNull(dataHora, "dataHora é obrigatória");
        this.latitude = latitude;
        this.longitude = longitude;
        this.satelite = textoOuPadrao(satelite);
        this.municipio = textoOuPadrao(municipio);
        this.estado = textoOuPadrao(estado);
        this.pais = textoOuPadrao(pais);
        this.bioma = textoOuPadrao(bioma);
        this.diasSemChuva = diasSemChuva;
        this.precipitacao = precipitacao;
        this.riscoFogo = riscoFogo;
        this.frp = frp;
    }

    // Tira espaços das pontas e troca texto vazio por "Não informado".
    private static String textoOuPadrao(String s) {
        return (s == null || s.isBlank()) ? NAO_INFORMADO : s.trim();
    }

    public LocalDateTime getDataHora() { return dataHora; }
    // Só a data, sem a hora (útil para ordenar por dia).
    public LocalDate getData() { return dataHora.toLocalDate(); }
    public int getAno() { return dataHora.getYear(); }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getSatelite() { return satelite; }
    public String getMunicipio() { return municipio; }
    public String getEstado() { return estado; }
    public String getPais() { return pais; }
    public String getBioma() { return bioma; }
    public Double getDiasSemChuva() { return diasSemChuva; }
    public Double getPrecipitacao() { return precipitacao; }
    public Double getRiscoFogo() { return riscoFogo; }
    public Double getFrp() { return frp; }

    @Override
    public String toString() {
        return "FocoIncendio{" + dataHora + ", " + municipio + "/" + estado
                + ", bioma=" + bioma + ", lat=" + latitude + ", lon=" + longitude + "}";
    }
}
