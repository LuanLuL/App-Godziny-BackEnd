package com.cefet.godziny.infraestrutura.exceptions.auth;

public class AuthEmailOuSenhaIncorretoException extends RuntimeException {
    
    public AuthEmailOuSenhaIncorretoException() {super("Credenciais inválidas. Verifique seu e-mail e senha e tente novamente");}

}
