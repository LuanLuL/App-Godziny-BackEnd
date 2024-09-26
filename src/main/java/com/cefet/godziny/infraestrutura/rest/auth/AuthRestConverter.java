package com.cefet.godziny.infraestrutura.rest.auth;

import lombok.NoArgsConstructor;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import com.cefet.godziny.api.auth.AuthResponseDto;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.rest.usuario.UsuarioRestConverter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRestConverter {
    public static AuthResponseDto jwtTokenToAuthResponseDto(String jwtToken, UsuarioEntidade usuarioEntidade) {
        return AuthResponseDto.builder()
        .token(jwtToken)
        .usuario(UsuarioRestConverter.EntidadeToUsuarioRecuperarDto(usuarioEntidade))
        .build();
    }

    public static Claims getAllClaims(String token, Key chave) {
        return Jwts
        .parserBuilder()
        .setSigningKey(chave)
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    public static String getToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration,
          Key chave
    ) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(chave, SignatureAlgorithm.HS256)
            .compact();
    }
}
