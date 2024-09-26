package com.cefet.godziny.infraestrutura.exceptions.atividade;

public class RemoverAtividadeSimulandoOuAprovadaException extends RuntimeException {
    
    public RemoverAtividadeSimulandoOuAprovadaException() {super("Atividades em simulação ou aprovadas não podem ser removidas");}

}
