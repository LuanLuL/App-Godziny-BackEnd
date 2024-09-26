package com.cefet.godziny.infraestrutura.exceptions.usuario;

public class CriarUsuarioNormalSemCursoException extends RuntimeException{

    public CriarUsuarioNormalSemCursoException() {super("Usuários do tipo 'NORMAL' devem ser associados a um curso");}
    
}

