package com.cefet.godziny.domain.casouso.atividade;

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
import com.cefet.godziny.api.atividade.AtividadeRecuperarDto;
import com.cefet.godziny.constantes.atividade.EnumStatus;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.rest.atividade.AtividadeRestConverter;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PesquisarAtividadeCasoUsoTest {
    private AtividadeEntidade entidade;

    @Mock
    private AtividadeRepositorioJpa atividadeRepositorioJpa;

    @Mock
    private CursoRepositorioJpa cursoRepositorioJpa;

    private PesquisarAtividadeCasoUso pesquisarAtividadeCasoUso;

    @BeforeEach
    void inicializarDados() {
        pesquisarAtividadeCasoUso = new PesquisarAtividadeCasoUso(atividadeRepositorioJpa, cursoRepositorioJpa, "USUARIO_TESTE", "TITULO_TESTE", EnumStatus.SIMULANDO, "CATEGORIA_TESTE");
    }

    @AfterEach
    void limparDados() {
        this.entidade = null;
        atividadeRepositorioJpa.deleteAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should validate and execute a successful activity search")
    void testePesquisarAtividadeCasoUsoSuccess() throws Exception {
        this.entidade = createAtividadeEntidade();
        Page<AtividadeEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        AtividadeRecuperarDto expectedDto = AtividadeRestConverter.EntidadeToAtividadeRecuperarDto(this.entidade);

        when(atividadeRepositorioJpa.listAtividades(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        pesquisarAtividadeCasoUso.validarPesquisa();
        Page<AtividadeRecuperarDto> result = pesquisarAtividadeCasoUso.pesquisarAtividade(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return activities filtered by user name when other fields are null")
    void testePesquisarAtividadesComUsuarioNomeQuandoOutrosCamposNull() {
        pesquisarAtividadeCasoUso = new PesquisarAtividadeCasoUso(atividadeRepositorioJpa, cursoRepositorioJpa, "USUARIO_TESTE", null, null, null);
        this.entidade = createAtividadeEntidade();
        Page<AtividadeEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        AtividadeRecuperarDto expectedDto = AtividadeRestConverter.EntidadeToAtividadeRecuperarDto(this.entidade);

        when(atividadeRepositorioJpa.listAtividades(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<AtividadeRecuperarDto> result = pesquisarAtividadeCasoUso.pesquisarAtividade(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return activities filtered by title when other fields are null")
    void testePesquisarAtividadesComTituloQuandoOutrosCamposNull() {
        pesquisarAtividadeCasoUso = new PesquisarAtividadeCasoUso(atividadeRepositorioJpa, cursoRepositorioJpa, null, "TITULO_TESTE", null, null);
        this.entidade = createAtividadeEntidade();
        Page<AtividadeEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        AtividadeRecuperarDto expectedDto = AtividadeRestConverter.EntidadeToAtividadeRecuperarDto(this.entidade);

        when(atividadeRepositorioJpa.listAtividades(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<AtividadeRecuperarDto> result = pesquisarAtividadeCasoUso.pesquisarAtividade(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return activities filtered by status when other fields are null")
    void testePesquisarAtividadesComStatusQuandoOutrosCamposNull() {
        pesquisarAtividadeCasoUso = new PesquisarAtividadeCasoUso(atividadeRepositorioJpa, cursoRepositorioJpa, null, null, EnumStatus.APROVADA, null);
        this.entidade = createAtividadeEntidade();
        Page<AtividadeEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        AtividadeRecuperarDto expectedDto = AtividadeRestConverter.EntidadeToAtividadeRecuperarDto(this.entidade);

        when(atividadeRepositorioJpa.listAtividades(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<AtividadeRecuperarDto> result = pesquisarAtividadeCasoUso.pesquisarAtividade(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return activities filtered by category when other fields are null")
    void testePesquisarAtividadesComCategoriaQuandoOutrosCamposNull() {
        pesquisarAtividadeCasoUso = new PesquisarAtividadeCasoUso(atividadeRepositorioJpa, cursoRepositorioJpa, null, null, null, "CATEGORIA_TESTE");
        this.entidade = createAtividadeEntidade();
        Page<AtividadeEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        AtividadeRecuperarDto expectedDto = AtividadeRestConverter.EntidadeToAtividadeRecuperarDto(this.entidade);

        when(atividadeRepositorioJpa.listAtividades(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<AtividadeRecuperarDto> result = pesquisarAtividadeCasoUso.pesquisarAtividade(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return empty page when no activities match the criteria")
    void testePesquisarAtividadesSemResultado() {
        pesquisarAtividadeCasoUso = new PesquisarAtividadeCasoUso(atividadeRepositorioJpa, cursoRepositorioJpa, "USUARIO_TESTE", "TITULO_TESTE", EnumStatus.SIMULANDO, "CATEGORIA_TESTE");
        Page<AtividadeEntidade> page = new PageImpl<>(List.of());
        Pageable pageable = PageRequest.of(0, 10);

        when(atividadeRepositorioJpa.listAtividades(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        Page<AtividadeRecuperarDto> result = pesquisarAtividadeCasoUso.pesquisarAtividade(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();
    }

    private AtividadeEntidade createAtividadeEntidade() {
        CursoEntidade curso = new CursoEntidade(
            UUID.randomUUID(),
            "ODONT_DIV", "Odontologia",
            300,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
        return new AtividadeEntidade(
            UUID.randomUUID(),
            new UsuarioEntidade(99999, curso, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.NORMAL, LocalDateTime.now()),
            new CategoriaEntidade(UUID.randomUUID(), curso, "nome TESTE", 0.5f, 0.2f, "descrição TESTE"),
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.APROVADA,
            new ArquivoEntidade(UUID.randomUUID(), "nome TESTE", "tipo TESTE", "dados TESTE".getBytes()),
            2.4f,
            "comentário TESTE"
        );
    }
}
