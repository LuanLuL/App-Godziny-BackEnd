package com.cefet.godziny.domain.casouso.curso;

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
import com.cefet.godziny.api.curso.CursoRecuperarDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.rest.curso.CursoRestConverter;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PesquisarCursoCasoUsoTest {
    private CursoEntidade entidade;

    @Mock
    CursoRepositorioJpa cursoRepositorioJpa;

    private PesquisarCursoCasoUso pesquisarCursoCasoUso;

    @BeforeEach
    void inicializarDados() {
        pesquisarCursoCasoUso = new PesquisarCursoCasoUso(cursoRepositorioJpa, "SIGLA_TESTE", "NOME_TESTE");
    }

    @AfterEach
    void limparDados() {
        this.entidade = null;
        cursoRepositorioJpa.deleteAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should validate and execute a successful course search")
    void testePesquisarCursoCasoUsoSuccess() throws Exception {
        this.entidade = createCursoEntidade();
        Page<CursoEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        CursoRecuperarDto expectedDto = CursoRestConverter.EntidadeToCursoRecuperarDto(this.entidade);

        when(cursoRepositorioJpa.listCursos(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        pesquisarCursoCasoUso.validarPesquisa();
        Page<CursoRecuperarDto> result = pesquisarCursoCasoUso.pesquisarCursos(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return courses filtered by sigla when nome is null")
    void testePesquisarCursosComSiglaQuandoNomeNull() {
        pesquisarCursoCasoUso = new PesquisarCursoCasoUso(cursoRepositorioJpa, "SIGLA_TESTE", null);
        this.entidade = createCursoEntidade();
        Page<CursoEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        CursoRecuperarDto expectedDto = CursoRestConverter.EntidadeToCursoRecuperarDto(this.entidade);

        when(cursoRepositorioJpa.listCursos(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<CursoRecuperarDto> result = pesquisarCursoCasoUso.pesquisarCursos(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return courses filtered by nome when sigla is null")
    void testePesquisarCursosComNomeQuandoSiglaNull() {
        pesquisarCursoCasoUso = new PesquisarCursoCasoUso(cursoRepositorioJpa, null, "NOME_TESTE");
        this.entidade = createCursoEntidade();
        Page<CursoEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        CursoRecuperarDto expectedDto = CursoRestConverter.EntidadeToCursoRecuperarDto(this.entidade);

        when(cursoRepositorioJpa.listCursos(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<CursoRecuperarDto> result = pesquisarCursoCasoUso.pesquisarCursos(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return empty page when no courses match the criteria")
    void testePesquisarCursosSemResultado() {
        pesquisarCursoCasoUso = new PesquisarCursoCasoUso(cursoRepositorioJpa, "SIGLA_TESTE", "NOME_TESTE");
        Page<CursoEntidade> page = new PageImpl<>(List.of());
        Pageable pageable = PageRequest.of(0, 10);

        when(cursoRepositorioJpa.listCursos(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<CursoRecuperarDto> result = pesquisarCursoCasoUso.pesquisarCursos(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();
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
