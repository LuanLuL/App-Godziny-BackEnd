package com.cefet.godziny.infraestrutura.persistencia.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import com.cefet.godziny.api.auth.AuthDto;
import com.cefet.godziny.api.auth.AuthResponseDto;
import com.cefet.godziny.domain.porta.auth.IAuthRepositorio;
import com.cefet.godziny.infraestrutura.authentication.JwtService;
import com.cefet.godziny.infraestrutura.exceptions.auth.AuthEmailOuSenhaIncorretoException;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import com.cefet.godziny.infraestrutura.rest.auth.AuthRestConverter;

@Component
public class AuthRepositorioJpa implements IAuthRepositorio{
    @Autowired
    private UsuarioRepositorioJpa usuarioRepositorioJpa;

    @Autowired
    private JwtService jwtService;

    @Autowired 
    private AuthenticationManager authenticationManager;

    public AuthRepositorioJpa(UsuarioRepositorioJpa usuarioRepositorioJpa, AuthenticationManager authenticationManager, JwtService jwtService){
        this.usuarioRepositorioJpa = usuarioRepositorioJpa;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDto loginUsuario(AuthDto dto) throws Exception {
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
            );
            UsuarioEntidade usuarioEntidade = usuarioRepositorioJpa.findByEmail(dto.getEmail());
            return AuthRestConverter.jwtTokenToAuthResponseDto(jwtService.generateToken(usuarioEntidade), usuarioEntidade);
        } catch(BadCredentialsException e) {
            throw new AuthEmailOuSenhaIncorretoException();
        }
    }
    
}
