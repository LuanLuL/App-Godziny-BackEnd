package com.cefet.godziny.infraestrutura.rest.curso;

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
import com.cefet.godziny.api.curso.CursoDto;
import com.cefet.godziny.api.curso.CursoFiltroDto;
import com.cefet.godziny.api.curso.CursoRecuperarDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
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
public class CursoControleTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String SIGLA = "ENG_ELET_BH";
    private static final Integer CARGA_HORARIA_COMPLEMENTAR = 500;
    private static final String NOME = "Engenharia Elétrica";
    private static final UsuarioEntidade COORDENADOR = new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now());

    private CursoEntidade entidade;
    private CursoDto dto;

    @InjectMocks
    CursoControle controler;

    @Mock
    CursoRepositorioJpa cursoRepositorioJpa;

    @Mock
    UsuarioRepositorioJpa usuarioRepositorioJpa;

    @Mock
    CategoriaRepositorioJpa categoriaRepositorioJpa;

    @BeforeEach
    void inicializarDados() {
        MockitoAnnotations.openMocks(this);
        controler = new CursoControle(cursoRepositorioJpa, usuarioRepositorioJpa, categoriaRepositorioJpa);
    };

    @AfterEach
    void limparDados() {
        this.entidade = null;
        this.dto = null;

        cursoRepositorioJpa.deleteAll();
        usuarioRepositorioJpa.deleteAll();
    }
    
    @Test
    @DisplayName("Should search a Curso by SIGLA successfully")
    void testGetCursoSuccess() throws Exception {
        this.entidade = createCursoEntidade();

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(entidade);
        ResponseEntity<CursoRecuperarDto> response = controler.getCurso(SIGLA);

        assertThat(response.getBody()).isInstanceOf(CursoRecuperarDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    @DisplayName("Should search all Cursos successfully")
    void testPesquisarCursosSuccess() throws Exception {
        this.entidade = createCursoEntidade();
        CursoFiltroDto filtroDto = new CursoFiltroDto();
        Page<CursoEntidade> page = new PageImpl<>(List.of(entidade));
        Pageable pageable = PageRequest.of(0, 10);

        when(cursoRepositorioJpa.listCursos(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        ResponseEntity<Page<CursoRecuperarDto>> response = controler.pesquisarCursos(pageable, filtroDto);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSizeGreaterThan(0); 
        assertThat(response.getBody().getSize()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should create a new Curso successfully")
    void testCreateCursoSuccess() throws Exception {
        this.dto = createCursoDto();

        when(cursoRepositorioJpa.createCurso(Mockito.any(CursoEntidade.class))).thenReturn(SIGLA);
        when(usuarioRepositorioJpa.findById(Mockito.anyInt())).thenReturn(COORDENADOR);
        ResponseEntity<String> response = controler.createCurso(dto);

        assertThat(response.getBody()).isInstanceOf(String.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Should update an existing Curso successfully")
    void testupdateCursoSuccess() throws Exception {
        this.entidade = createCursoEntidade();
        this.dto = createCursoDto();
        dto.setNome("Engnharia Elétrica Atualizada");

        when(cursoRepositorioJpa.updateCurso(Mockito.anyString(), Mockito.any(CursoEntidade.class))).thenReturn(SIGLA);
        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(entidade);
        when(usuarioRepositorioJpa.findById(Mockito.anyInt())).thenReturn(COORDENADOR);
        ResponseEntity<String> response = controler.updateCurso(SIGLA, dto);

        assertThat(response.getBody()).isInstanceOf(String.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should delete a Curso successfully")
    void testRemoveCursoSuccess() throws Exception {
        this.entidade = createCursoEntidade();
        Page<UsuarioEntidade> pageUsers = new PageImpl<>(List.of());

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(entidade);
        when(usuarioRepositorioJpa.listUsuariosByCurso(Mockito.any(Pageable.class), Mockito.any(CursoEntidade.class))).thenReturn(pageUsers);
        ResponseEntity<Void> response = controler.removeCurso(SIGLA);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private CursoEntidade createCursoEntidade(){
        CursoEntidade curso = new CursoEntidade(ID, SIGLA, NOME, CARGA_HORARIA_COMPLEMENTAR, COORDENADOR);
        return curso;
    }

    private CursoDto createCursoDto(){
        CursoDto curso = new CursoDto();
        curso.setId(ID);
        curso.setSigla(SIGLA);
        curso.setNome(NOME);
        curso.setCarga_horaria_complementar(CARGA_HORARIA_COMPLEMENTAR);
        curso.setCoordenador_matricula(COORDENADOR.getMatricula());
        return curso;
    }
}