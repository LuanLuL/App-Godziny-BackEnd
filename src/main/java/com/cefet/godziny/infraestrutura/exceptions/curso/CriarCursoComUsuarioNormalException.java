package com.cefet.godziny.infraestrutura.exceptions.curso;

public class CriarCursoComUsuarioNormalException  extends RuntimeException{

    public CriarCursoComUsuarioNormalException() {super("O coordenador do curso deve ser um usuário do tipo 'ADM'");}

}
