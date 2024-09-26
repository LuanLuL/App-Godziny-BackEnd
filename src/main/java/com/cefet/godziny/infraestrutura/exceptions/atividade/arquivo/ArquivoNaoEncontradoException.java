package com.cefet.godziny.infraestrutura.exceptions.atividade.arquivo;

public class ArquivoNaoEncontradoException extends RuntimeException {
    
    public ArquivoNaoEncontradoException() {super("Arquivo não encontrado na base de dados");}
    
}

