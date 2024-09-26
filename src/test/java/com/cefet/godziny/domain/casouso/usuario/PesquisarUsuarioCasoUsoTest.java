package com.cefet.godziny.domain.casouso.usuario;

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
import com.cefet.godziny.api.usuario.UsuarioRecuperarDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import com.cefet.godziny.infraestrutura.rest.usuario.UsuarioRestConverter;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PesquisarUsuarioCasoUsoTest {
    private UsuarioEntidade entidade;

    @Mock
    private CursoRepositorioJpa cursoRepositorioJpa;

    @Mock
    private UsuarioRepositorioJpa usuarioRepositorioJpa;

    private PesquisarUsuarioCasoUso pesquisarUsuarioCasoUso;

    @BeforeEach
    void inicializarDados() {
        pesquisarUsuarioCasoUso = new PesquisarUsuarioCasoUso(usuarioRepositorioJpa, cursoRepositorioJpa, 1, "SIGLA_TESTE", "NOME_TESTE");
    }

    @AfterEach
    void limparDados() {
        this.entidade = null;
        usuarioRepositorioJpa.deleteAll();
        cursoRepositorioJpa.deleteAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should validate and execute a successful user search")
    void testePesquisarUsuarioCasoUsoSuccess() throws Exception {
        this.entidade = createUsuarioEntidade();
        Page<UsuarioEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        UsuarioRecuperarDto expectedDto = UsuarioRestConverter.EntidadeToUsuarioRecuperarDto(this.entidade);

        when(usuarioRepositorioJpa.listUsuarios(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(createCursoEntidade());
        pesquisarUsuarioCasoUso.validarPesquisa();
        Page<UsuarioRecuperarDto> result = pesquisarUsuarioCasoUso.pesquisarUsuarios(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return users filtered by nome when matricula and cursoSigla are null")
    void testePesquisarUsuariosComNomeQuandoMatriculaECursoSiglaNull() throws Exception {
        pesquisarUsuarioCasoUso = new PesquisarUsuarioCasoUso(usuarioRepositorioJpa, cursoRepositorioJpa, null, "SIGLA_TESTE", "NOME_TESTE");
        this.entidade = createUsuarioEntidade();
        Page<UsuarioEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        UsuarioRecuperarDto expectedDto = UsuarioRestConverter.EntidadeToUsuarioRecuperarDto(this.entidade);

        when(usuarioRepositorioJpa.listUsuarios(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(createCursoEntidade());
        pesquisarUsuarioCasoUso.validarPesquisa();
        Page<UsuarioRecuperarDto> result = pesquisarUsuarioCasoUso.pesquisarUsuarios(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return users filtered by matricula when nome and cursoSigla are null")
    void testePesquisarUsuariosComMatriculaQuandoNomeECursoSiglaNull() throws Exception {
        pesquisarUsuarioCasoUso = new PesquisarUsuarioCasoUso(usuarioRepositorioJpa, cursoRepositorioJpa, 999999, null, null);
        this.entidade = createUsuarioEntidade();
        Page<UsuarioEntidade> page = new PageImpl<>(List.of(this.entidade));
        Pageable pageable = PageRequest.of(0, 10);
        UsuarioRecuperarDto expectedDto = UsuarioRestConverter.EntidadeToUsuarioRecuperarDto(this.entidade);

        when(usuarioRepositorioJpa.listUsuarios(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(createCursoEntidade());
        pesquisarUsuarioCasoUso.validarPesquisa();
        Page<UsuarioRecuperarDto> result = pesquisarUsuarioCasoUso.pesquisarUsuarios(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.get().findFirst().get())
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Should return empty page when no users match the criteria")
    void testePesquisarUsuariosSemResultado() throws Exception {
        pesquisarUsuarioCasoUso = new PesquisarUsuarioCasoUso(usuarioRepositorioJpa, cursoRepositorioJpa, 1, "SIGLA_TESTE", "NOME_TESTE");
        Page<UsuarioEntidade> page = new PageImpl<>(List.of());
        Pageable pageable = PageRequest.of(0, 10);

        when(usuarioRepositorioJpa.listUsuarios(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);
        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(createCursoEntidade());
        pesquisarUsuarioCasoUso.validarPesquisa();
        Page<UsuarioRecuperarDto> result = pesquisarUsuarioCasoUso.pesquisarUsuarios(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();
    }

    private UsuarioEntidade createUsuarioEntidade() {
        return new UsuarioEntidade(
            999999,
            null,
            "TESTE",
            "teste@teste.com.br",
            "teste123",
            EnumRecursos.NORMAL,
            LocalDateTime.now()
        );
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
