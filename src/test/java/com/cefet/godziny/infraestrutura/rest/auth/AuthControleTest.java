package com.cefet.godziny.infraestrutura.rest.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.cefet.godziny.api.auth.AuthDto;
import com.cefet.godziny.api.auth.AuthResponseDto;
import com.cefet.godziny.api.curso.CursoRecuperarDto;
import com.cefet.godziny.api.usuario.UsuarioRecuperarDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.auth.AuthRepositorioJpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
public class  AuthControleTest {
    private static final String EMAIL = "usuariologado@login.com.br";
    private static final String SENHA = "123456";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZUB0ZXN0ZS5jb20iLCJpYXQiOjE3MjMxNjMyMzUsImV4cCI6MTcyMzc2ODAzNX0.1LFktW5TeeWISJtGTBNf3CbyQSQ5neSurcQ_muPH5KM";

    private AuthDto authDto;
    private AuthResponseDto authResponseDto;

    @Mock
    AuthRepositorioJpa authRepositorioJpa;

    @InjectMocks
    AuthControle controler;

    @BeforeEach
    void inicializarDados() {
        MockitoAnnotations.openMocks(this);
        controler = new AuthControle(authRepositorioJpa);
    };

    @AfterEach
    void limparDados() {
        this.authDto = null;
        this.authResponseDto = null;
    }
    
    @Test
    @DisplayName("Should do the login of an user by EMAIL and SENHA successfully")
    void testLoginUsuario() throws Exception {
        this.authResponseDto = createAuthResponseDto();
        this.authDto = createAuthDto();

        when(authRepositorioJpa.loginUsuario(Mockito.any(AuthDto.class))).thenReturn(this.authResponseDto);
        ResponseEntity<AuthResponseDto> response = controler.loginUsuario(this.authDto);

        assertThat(response.getBody()).isInstanceOf(AuthResponseDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private AuthDto createAuthDto(){
        return new AuthDto(EMAIL, SENHA);
    }

    private AuthResponseDto createAuthResponseDto(){
        CursoRecuperarDto cursoRecuperarDto = new CursoRecuperarDto(
            UUID.randomUUID(),
            "ENG_ELE_BH",
            "Engenhari Elétrica",
            500,
            new UsuarioRecuperarDto(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())    
        );
        UsuarioRecuperarDto usuarioRecuperarDto = new UsuarioRecuperarDto(99999, cursoRecuperarDto, "nome TESTE", EMAIL, SENHA, EnumRecursos.ADM, LocalDateTime.now());
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setToken(TOKEN);
        authResponseDto.setUsuario(usuarioRecuperarDto);
        return authResponseDto;
    }
}