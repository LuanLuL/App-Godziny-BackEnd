package com.cefet.godziny.infraestrutura.rest.categoria;

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
import com.cefet.godziny.api.categoria.CategoriaDto;
import com.cefet.godziny.api.categoria.CategoriaFiltroDto;
import com.cefet.godziny.api.categoria.CategoriaRecuperarDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class CategoriaControleTest {
    private static final UUID ID = UUID.randomUUID();
    private static final CursoEntidade CURSO = new CursoEntidade(
        UUID.randomUUID(),
        "ENG_ELET_BH",
        "Engenharia Elétrica",
        500,
        new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
    );
    private static final String NOME = "Categoria Teste";
    private static final float PORCENTAGEM_HORAS_MAXIMAS = (float) 0.5;
    private static final float HORAS_MULTIPLICADOR = (float) 0.1;
    private static final String DESCRICAO = "Categoria Descrição Teste";

    private CategoriaEntidade entidade;
    private CategoriaDto dto;

    @InjectMocks
    private CategoriaControle controler;

    @Mock
    private CategoriaRepositorioJpa categoriaRepositorioJpa;

    @Mock
    private CursoRepositorioJpa cursoRepositorioJpa;

    @Mock
    private AtividadeRepositorioJpa atividadeRepositorioJpa;

    @BeforeEach
    void inicializarDados() {
        MockitoAnnotations.openMocks(this);
        controler = new CategoriaControle(categoriaRepositorioJpa, cursoRepositorioJpa, atividadeRepositorioJpa);
    };

    @AfterEach
    void limparDados() {
        this.entidade = null;
        this.dto = null;

        categoriaRepositorioJpa.deleteAll();
        cursoRepositorioJpa.deleteAll();
    }

    @Test
    @DisplayName("Should search a Categoria by Id successfully")
    void testGetCategoriaSuccess() throws Exception {
        this.entidade = createCategoriaEntidade();

        when(categoriaRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(entidade);
        ResponseEntity<CategoriaRecuperarDto> response = controler.getCategoria(ID);

        assertThat(response.getBody()).isInstanceOf(CategoriaRecuperarDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    @DisplayName("Should search all Categorias successfully")
    void testPesquisarCategoriasSuccess() throws Exception {
        this.entidade = createCategoriaEntidade();
        CategoriaFiltroDto filtroDto = new CategoriaFiltroDto();
        Page<CategoriaEntidade> page = new PageImpl<>(List.of(entidade));
        Pageable pageable = PageRequest.of(0, 10);

        when(categoriaRepositorioJpa.listCategorias(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        ResponseEntity<Page<CategoriaRecuperarDto>> response = controler.pesquisarCategorias(pageable, filtroDto);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSizeGreaterThan(0); 
        assertThat(response.getBody().getSize()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should create a new Categoria successfully")
    void testCreateCategoriaSuccess() throws Exception {
        this.dto = createCategoriaDto();

        when(categoriaRepositorioJpa.createCategoria(Mockito.any(CategoriaEntidade.class))).thenReturn(ID);
        ResponseEntity<UUID> response = controler.createCategoria(dto);

        assertThat(response.getBody()).isInstanceOf(UUID.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Should update an existing Categoria successfully")
    void testupdateCategoriaSuccess() throws Exception {
        this.entidade = createCategoriaEntidade();
        this.dto = createCategoriaDto();
        dto.setNome("Engnharia Elétrica Atualizada");

        when(categoriaRepositorioJpa.updateCategoria(Mockito.any(CategoriaEntidade.class))).thenReturn(ID);
        when(categoriaRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(entidade);
        ResponseEntity<UUID> response = controler.updateCategoria(ID, dto);

        assertThat(response.getBody()).isInstanceOf(UUID.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should delete a Categoria successfully")
    void testRemoveCategoriaSuccess() throws Exception {
        this.entidade = createCategoriaEntidade();

        when(categoriaRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(entidade);
        ResponseEntity<Void> response = controler.removeCategoria(ID);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private CategoriaEntidade createCategoriaEntidade(){
        CategoriaEntidade categoria = new CategoriaEntidade(ID, CURSO, NOME, PORCENTAGEM_HORAS_MAXIMAS, HORAS_MULTIPLICADOR, DESCRICAO);
        return categoria;
    }
    private CategoriaDto createCategoriaDto(){
        CategoriaDto categoria = new CategoriaDto();
        categoria.setId(ID);
        categoria.setCursoSigla(CURSO.getSigla());
        categoria.setNome(NOME);
        categoria.setPorcentagemHorasMaximas(PORCENTAGEM_HORAS_MAXIMAS);
        categoria.setHorasMultiplicador(HORAS_MULTIPLICADOR);
        categoria.setDescricao(DESCRICAO);
        return categoria;
    }
}

