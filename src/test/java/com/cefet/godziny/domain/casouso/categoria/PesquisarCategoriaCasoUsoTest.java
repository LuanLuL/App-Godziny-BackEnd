package com.cefet.godziny.domain.casouso.categoria;

import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.cefet.godziny.api.categoria.CategoriaRecuperarDto;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.rest.categoria.CategoriaRestConverter;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PesquisarCategoriaCasoUsoTest {
    private CategoriaEntidade entidade;

    @Mock
    CategoriaRepositorioJpa categoriaRepositorioJpa;

    private PesquisarCategoriaCasoUso pesquisarCategoriaCasoUso;

    @BeforeEach
    void inicializarDados() {
        pesquisarCategoriaCasoUso = new PesquisarCategoriaCasoUso(categoriaRepositorioJpa, "SIGLA_TESTE", "NOME_TESTE");
    }

    @AfterEach
    void limparDados() {
        this.entidade = null;
        categoriaRepositorioJpa.deleteAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should validate and execute a successful category search")
    void testePesquisarCategoriaCasoUsoSuccess() throws Exception {
        this.entidade = createCategoriaEntidade();
        Page<CategoriaEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        CategoriaRecuperarDto expectedDto = CategoriaRestConverter.EntidadeToCategoriaRecuperarDto(this.entidade);

        when(categoriaRepositorioJpa.listCategorias(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        pesquisarCategoriaCasoUso.validarPesquisa();
        Page<CategoriaRecuperarDto> result = pesquisarCategoriaCasoUso.pesquisarCategoria(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return categories filtered by sigla when nome is null")
    void testePesquisarCategoriasComSiglaQuandoNomeNull() {
        pesquisarCategoriaCasoUso = new PesquisarCategoriaCasoUso(categoriaRepositorioJpa, "SIGLA_TESTE", null);
        this.entidade = createCategoriaEntidade();
        Page<CategoriaEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        CategoriaRecuperarDto expectedDto = CategoriaRestConverter.EntidadeToCategoriaRecuperarDto(this.entidade);

        when(categoriaRepositorioJpa.listCategorias(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<CategoriaRecuperarDto> result = pesquisarCategoriaCasoUso.pesquisarCategoria(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return categories filtered by nome when sigla is null")
    void testePesquisarCategoriasComNomeQuandoSiglaNull() {
        pesquisarCategoriaCasoUso = new PesquisarCategoriaCasoUso(categoriaRepositorioJpa, null, "NOME_TESTE");
        this.entidade = createCategoriaEntidade();
        Page<CategoriaEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        CategoriaRecuperarDto expectedDto = CategoriaRestConverter.EntidadeToCategoriaRecuperarDto(this.entidade);

        when(categoriaRepositorioJpa.listCategorias(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<CategoriaRecuperarDto> result = pesquisarCategoriaCasoUso.pesquisarCategoria(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return empty page when no categories match the criteria")
    void testePesquisarCategoriasSemResultado() {
        pesquisarCategoriaCasoUso = new PesquisarCategoriaCasoUso(categoriaRepositorioJpa, "SIGLA_TESTE", "NOME_TESTE");
        Page<CategoriaEntidade> page = new PageImpl<>(List.of());
        Pageable pageable = PageRequest.of(0, 10);

        when(categoriaRepositorioJpa.listCategorias(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<CategoriaRecuperarDto> result = pesquisarCategoriaCasoUso.pesquisarCategoria(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();
    }

    private CategoriaEntidade createCategoriaEntidade() {
        return new CategoriaEntidade(
            UUID.randomUUID(),
            createCursoEntidade(), "Categoria_TESTE",
            (float) 1.0, (float) 1.0,
            "Descrição_TESTE");
    }

    private CursoEntidade createCursoEntidade() {
        return new CursoEntidade(
            UUID.randomUUID(),
            "ODONT_DIV",
            "Odontologia",
            1,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
    }
}