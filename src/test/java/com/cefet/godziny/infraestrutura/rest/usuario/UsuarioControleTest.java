package com.cefet.godziny.infraestrutura.rest.usuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.cefet.godziny.api.usuario.UsuarioDto;
import com.cefet.godziny.api.usuario.UsuarioFiltroDto;
import com.cefet.godziny.api.usuario.UsuarioRecuperarDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class UsuarioControleTest {
    private static final Integer MATRICULA = 99999;
    private static final String CURSO_ID = "CURSO_TESTE";
    private static final CursoEntidade CURSO_ENTIDADE = new CursoEntidade(
        UUID.randomUUID(),
        "ENG_ELET_BH",
        "Engenharia Elétrica",
        500,
        new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
    );
    private static final String NOME = "Usuário para Testes";
    private static final String EMAIL = "usuarioteste@usuariotese.com";
    private static final String SENHA = "teste123";
    private static final EnumRecursos TIPO = EnumRecursos.NORMAL;
    private static final LocalDateTime DATA = LocalDateTime.now();

    private UsuarioEntidade entidade;
    private UsuarioDto dto;

    @InjectMocks
    private UsuarioControle controler;

    @Mock
    private CursoRepositorioJpa cursoRepositorioJpa;

    @Mock
    private UsuarioRepositorioJpa usuarioRepositorioJpa;

    @Mock
    private AtividadeRepositorioJpa atividadeRepositorioJpa;

    @Mock
    private PasswordEncoder enconder;

    @BeforeEach
    void inicializarDados() {
        MockitoAnnotations.openMocks(this);
        controler = new UsuarioControle(usuarioRepositorioJpa, cursoRepositorioJpa, atividadeRepositorioJpa, enconder);
    };

    @AfterEach
    void limparDados() {
        this.entidade = null;
        this.dto = null;

        cursoRepositorioJpa.deleteAll();
        usuarioRepositorioJpa.deleteAll();
    }
    
    @Test
    @DisplayName("Should search an Usuário by Matrícula successfully")
    void testGetUsuarioSuccess() throws Exception {
        this.entidade = createUsuarioEntidade();

        when(usuarioRepositorioJpa.findById(Mockito.anyInt())).thenReturn(entidade);
        ResponseEntity<UsuarioRecuperarDto> response = controler.getUsuario(MATRICULA);

        assertThat(response.getBody()).isInstanceOf(UsuarioRecuperarDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should search an Usuário without Curso by Matrícula successfully")
    void testGetUsuarioSuccessCase2() throws Exception {
        this.entidade = createUsuarioEntidade();
        this.entidade.setCurso(null);

        when(usuarioRepositorioJpa.findById(Mockito.anyInt())).thenReturn(entidade);
        ResponseEntity<UsuarioRecuperarDto> response = controler.getUsuario(MATRICULA);

        assertThat(response.getBody()).isInstanceOf(UsuarioRecuperarDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    @DisplayName("Should search all Usuarios successfully")
    void testPesquisarUsuariosSuccess() throws Exception {
        this.entidade = createUsuarioEntidade();
        UsuarioFiltroDto filtroDto = new UsuarioFiltroDto();
        Page<UsuarioEntidade> page = new PageImpl<>(List.of(entidade));
        Pageable pageable = PageRequest.of(0, 10);

        when(usuarioRepositorioJpa.listUsuarios(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        ResponseEntity<Page<UsuarioRecuperarDto>> response = controler.pesquisarUsuarios(pageable, filtroDto);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSizeGreaterThan(0); 
        assertThat(response.getBody().getSize()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should create a new Usuário successfully")
    void testCreateUsuarioSuccess() throws Exception {
        this.dto = createUsuarioDto();

        when(usuarioRepositorioJpa.createUsuario(Mockito.any(UsuarioEntidade.class))).thenReturn(MATRICULA);
        ResponseEntity<Integer> response = controler.createUsuario(dto);

        assertThat(response.getBody()).isInstanceOf(Integer.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Should update an existing Usuário successfully")
    void testupdateUsuarioSuccess() throws Exception {
        this.entidade = createUsuarioEntidade();
        this.dto = createUsuarioDto();
        dto.setNome("Usuário Teste Atualizado");

        when(usuarioRepositorioJpa.updateUsuario(Mockito.any(UsuarioEntidade.class))).thenReturn(MATRICULA);
        when(usuarioRepositorioJpa.findById(Mockito.anyInt())).thenReturn(entidade);
        ResponseEntity<Integer> response = controler.updateUsuario(this.dto.getMatricula(), dto);

        assertThat(response.getBody()).isInstanceOf(Integer.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should delete an Usuário successfully")
    void testRemoveUsuarioSuccess() throws Exception {
        this.entidade = createUsuarioEntidade();

        when(usuarioRepositorioJpa.findById(Mockito.anyInt())).thenReturn(entidade);
        ResponseEntity<Void> response = controler.removeUsuario(MATRICULA);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private UsuarioEntidade createUsuarioEntidade(){
        UsuarioEntidade usuario = new UsuarioEntidade(MATRICULA, CURSO_ENTIDADE, NOME, EMAIL, SENHA, TIPO, DATA);
        return usuario;
    }

    private UsuarioDto createUsuarioDto(){
        UsuarioDto usuario = new UsuarioDto();
        usuario.setMatricula(null);
        usuario.setCursoSigla(CURSO_ID);
        usuario.setNome(NOME);
        usuario.setEmail(EMAIL);
        usuario.setSenha(SENHA);
        usuario.setTipo(TIPO);
        usuario.setCreatedAt(DATA);
        return usuario;
    }
}