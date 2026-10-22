package com.unip.aps.exception;

// Erro nosso para problemas graves na leitura do CSV:
// arquivo vazio, cabeçalho sem as colunas que precisamos, pasta sem arquivos etc.
// Quem chamar a leitura pode capturar essa exceção e mostrar a mensagem na tela.
public class ErroLeituraCsvException extends RuntimeException {

    public ErroLeituraCsvException(String mensagem) {
        super(mensagem);
    }

    public ErroLeituraCsvException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
