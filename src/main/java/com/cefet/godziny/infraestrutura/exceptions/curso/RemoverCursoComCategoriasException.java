package com.cefet.godziny.infraestrutura.exceptions.curso;

public class RemoverCursoComCategoriasException extends RuntimeException{

    public RemoverCursoComCategoriasException() {super("Não é possível excluir um curso que possui categorias cadastradas");}
}
