package com.cefet.godziny.infraestrutura.exceptions.categoria;

public class CategoriaNaoEncontradaException extends RuntimeException {
    
    public CategoriaNaoEncontradaException() {super("Categoria não encontrada na base de dados");}

}