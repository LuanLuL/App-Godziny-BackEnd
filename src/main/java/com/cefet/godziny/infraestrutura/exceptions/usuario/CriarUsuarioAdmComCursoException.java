package com.cefet.godziny.infraestrutura.exceptions.usuario;

public class CriarUsuarioAdmComCursoException extends RuntimeException{

    public CriarUsuarioAdmComCursoException() {super("Usuários do tipo 'ADM' não podem ser associados a um curso");}
    
}
