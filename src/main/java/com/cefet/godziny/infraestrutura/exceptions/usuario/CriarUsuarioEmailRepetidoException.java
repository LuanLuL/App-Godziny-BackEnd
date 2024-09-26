package com.cefet.godziny.infraestrutura.exceptions.usuario;

public class CriarUsuarioEmailRepetidoException extends RuntimeException{

    public CriarUsuarioEmailRepetidoException() {super("O email fornecido já está cadastrado no sistema");}

}

