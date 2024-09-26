package com.cefet.godziny.infraestrutura.authentication;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import java.lang.reflect.Method;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

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

    private String token;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.token = null;
    }

    @Test
    @DisplayName("Should extract username from the token")
    void testExtractUsername() {
        this.token = generateToken();
        String username = jwtService.extractUsername(this.token);
        assertThat(username).isEqualTo("usuarioteste@gmail.com");
    }

    @Test
    @DisplayName("Should generate refresh token for UserDetails")
    void testGenerateRefreshToken() {
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("usuarioteste@gmail.com");
    
        String token = jwtService.generateRefreshToken(USUARIO);
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("Should return false when username does not match")
    void testIsTokenValid_whenUsernameDoesNotMatch() {
        this.token = generateToken();
        
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("diferente@gmail.com");

        boolean isValid = jwtService.isTokenValid(this.token, userDetails);
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should validate token correctly")
    void testIsTokenValid() {
        this.token = generateToken();

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("usuarioteste@gmail.com");

        boolean isValid = jwtService.isTokenValid(this.token, userDetails);
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should return a valid Key instance from the SECRET_KEY")
    void testGetSignInKey() throws Exception {
        Method getSignInKeyMethod = JwtService.class.getDeclaredMethod("getSignInKey");
        getSignInKeyMethod.setAccessible(true);

        Key actualKey = (Key) getSignInKeyMethod.invoke(jwtService);

        byte[] expectedKeyBytes = Decoders.BASE64.decode("c41b5baaaee57ce9d68912da7d3fa9f94cbd66b76ae46d6ad915c0d9d272fac6");
        Key expectedKey = Keys.hmacShaKeyFor(expectedKeyBytes);
        assertThat(actualKey).isEqualTo(expectedKey);
    }

    public String generateToken(){
        return jwtService.generateToken(USUARIO);
    }
}