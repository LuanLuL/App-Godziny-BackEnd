package com.cefet.godziny.infraestrutura.persistencia.usuario;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.time.LocalDateTime;
import java.util.Collection;


@SpringBootTest
@ActiveProfiles("test")
public class UsuarioEntidadeTest {

    private static final Integer MATRICULA = 9999999;
    private static final String NOME = "Teste";
    private static final CursoEntidade CURSO_ENTIDADE = new CursoEntidade(
        UUID.randomUUID(),
        "ODONT_DIV",
        "Odontologia",
        615,
        new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
    );
    private static final String EMAIL = "teste@teste.com";
    private static final String SENHA = "teste";
    private static final EnumRecursos TIPO = EnumRecursos.NORMAL;
    private static final LocalDateTime DATA = LocalDateTime.now();


    @InjectMocks
    private UsuarioEntidade usuarioEntidade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioEntidade = new UsuarioEntidade(MATRICULA, CURSO_ENTIDADE, NOME, EMAIL, SENHA, TIPO, DATA);
    }

    @Test
    @DisplayName("Test getAuthorities returns correct authority")
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = usuarioEntidade.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next()).isInstanceOf(SimpleGrantedAuthority.class);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo(TIPO.name());
    }

    @Test
    @DisplayName("Test getPassword returns correct password")
    void testGetPassword() {
        assertThat(usuarioEntidade.getPassword()).isEqualTo(SENHA);
    }

    @Test
    @DisplayName("Test getUsername returns correct username (email)")
    void testGetUsername() {
        assertThat(usuarioEntidade.getUsername()).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName("Test isAccountNonExpired returns true")
    void testIsAccountNonExpired() {
        assertThat(usuarioEntidade.isAccountNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Test isAccountNonLocked returns true")
    void testIsAccountNonLocked() {
        assertThat(usuarioEntidade.isAccountNonLocked()).isTrue();
    }

    @Test
    @DisplayName("Test isCredentialsNonExpired returns true")
    void testIsCredentialsNonExpired() {
        assertThat(usuarioEntidade.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Test isEnabled returns true")
    void testIsEnabled() {
        assertThat(usuarioEntidade.isEnabled()).isTrue();
    }
}