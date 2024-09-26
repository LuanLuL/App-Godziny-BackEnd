package com.cefet.godziny.infraestrutura.exceptions.categoria;

public class RemoverCategoriaComAtividadesException extends RuntimeException{

    public RemoverCategoriaComAtividadesException() {super("Não é possível excluir uma categoria que possui atividades cadastradas");}

}