package com.cefet.godziny.infraestrutura.exceptions.atividade;

public class AtividadeNaoEncontradaException extends RuntimeException {
    
    public AtividadeNaoEncontradaException() {super("Atividade não encontrada na base de dados");}

}
