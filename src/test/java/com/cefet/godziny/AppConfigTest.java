package com.cefet.godziny;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import com.cefet.godziny.configs.AppConfig;
import com.cefet.godziny.infraestrutura.exceptions.usuario.UsuarioNaoEncontradoException;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class AppConfigTest {
    @Mock
    private UsuarioRepositorioJpa usuarioRepositorioJpa;

    @InjectMocks
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        usuarioRepositorioJpa = mock(UsuarioRepositorioJpa.class);
        appConfig = new AppConfig(usuarioRepositorioJpa, mock(PasswordEncoder.class));
    }

    @AfterEach
    void clean() {
        usuarioRepositorioJpa.deleteAll();
    }

    @Test
    @DisplayName("Should find an Usuario by EMAIL")
    void testUserDetailsServiceFindsUserByEmail() {
        UsuarioEntidade usuarioMock = new UsuarioEntidade();
        usuarioMock.setEmail("existent@example.com");

        when(usuarioRepositorioJpa.findByEmailOptional("existent@example.com"))
            .thenReturn(Optional.of(usuarioMock));
        UserDetailsService userDetailsService = appConfig.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername("existent@example.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(UserDetails.class);
        assertThat(userDetails.getUsername()).isEqualTo("existent@example.com");
    }

    @Test
    @DisplayName("Should find an Usuario by EMAIL but return an exeption because there isn't in the databse")
    void testUserDetailsServiceThrowsUsuarioNaoEncontradoException() {
        when(usuarioRepositorioJpa.findByEmailOptional("nonexistent@example.com")).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = appConfig.userDetailsService();
        
        Exception thrown = assertThrows(UsuarioNaoEncontradoException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });

        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Usuário não encontrado na base de dados");
    }
}