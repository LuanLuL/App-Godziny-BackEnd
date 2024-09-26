package com.cefet.godziny.infraestrutura.exceptions;

public class UsuarioNaoAutorizadoException extends RuntimeException {
    
    public UsuarioNaoAutorizadoException() {super("Acesso restrito. Este módulo é acessível exclusivamente a administradores");}

}