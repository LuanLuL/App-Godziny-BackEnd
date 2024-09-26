package com.cefet.godziny.infraestrutura.exceptions.usuario;

public class UsuarioNaoEncontradoException extends RuntimeException {
    
    public UsuarioNaoEncontradoException() {super("Usuário não encontrado na base de dados");}

}
