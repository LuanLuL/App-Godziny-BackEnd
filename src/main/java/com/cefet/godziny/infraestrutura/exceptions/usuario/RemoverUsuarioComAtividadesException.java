package com.cefet.godziny.infraestrutura.exceptions.usuario;

public class RemoverUsuarioComAtividadesException extends RuntimeException{

    public RemoverUsuarioComAtividadesException() {super("Não é possível excluir um usuário que possui atividades cadastradas");}

}