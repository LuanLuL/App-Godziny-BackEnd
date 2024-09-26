package com.cefet.godziny.infraestrutura.persistencia.auth;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import com.cefet.godziny.api.auth.AuthDto;
import com.cefet.godziny.api.auth.AuthResponseDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.authentication.JwtService;
import com.cefet.godziny.infraestrutura.exceptions.auth.AuthEmailOuSenhaIncorretoException;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class AuthRepositorioJpaTest {
    private static final UsuarioEntidade USUARIO = new UsuarioEntidade(
        99999,
        new CursoEntidade(
            UUID.randomUUID(),
            "ENG_ELET_BH",
            "Engenharia Elétrica",
            500,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        ),
        "Usuario teste",
        "usuarioteste@gmail.com",
        "123456",
        EnumRecursos.NORMAL,
        LocalDateTime.now()
    );

    private AuthDto authDto;

    @Mock
    private UsuarioRepositorioJpa usuarioRepositorioJpa;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthRepositorioJpa authRepositorio;

    @BeforeEach
    void start() {
        MockitoAnnotations.openMocks(this);
        authRepositorio = new AuthRepositorioJpa(usuarioRepositorioJpa, authenticationManager, jwtService);
    };

    @AfterEach
    void clean() {
        usuarioRepositorioJpa.deleteAll();
        this.authDto = null;
    }

    @Test
    @DisplayName("Should do the authentication Usuario successfully from DataBase")
    void testLoginUsuarioSuccess() throws Exception {
        this.authDto = createAuthDto();

        when(usuarioRepositorioJpa.findByEmail(Mockito.anyString())).thenReturn(USUARIO);
        AuthResponseDto result = authRepositorio.loginUsuario(this.authDto);

        assertThat(result).isInstanceOf(AuthResponseDto.class);
        assertThat(result).isNotNull();
    }
    
    @Test
    @DisplayName("Should do the authentication Usuario and return an exception cause EMAIL or SENHA isn't correct")
    void testLoginUsuarioCase1() throws Exception {
        this.authDto = createAuthDto();
        authDto.setSenha("senha diferente");

        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager)
            .authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getSenha()));
        Exception thrown = assertThrows(AuthEmailOuSenhaIncorretoException.class, () -> {
            authRepositorio.loginUsuario(this.authDto);
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Credenciais inválidas. Verifique seu e-mail e senha e tente novamente");
    }

    private AuthDto createAuthDto(){
        return new AuthDto("emailtest@teste.com", "123456");
    }
}