package com.cefet.godziny.domain.casouso.curso;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.exceptions.curso.RemoverCursoComCategoriasException;
import com.cefet.godziny.infraestrutura.exceptions.curso.RemoverCursoComUsuariosException;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;

@SpringBootTest
public class RemoverCursoCasoUsoTest {
    
    private CursoEntidade entidade;

    @Mock
    CursoRepositorioJpa cursoRepositorioJpa;

    @Mock
    UsuarioRepositorioJpa usuarioRepositorioJpa;

    @Mock
    CategoriaRepositorioJpa categoriaRepositorioJpa;

    private RemoverCursoCasoUso removerCursoCasoUso;

    @BeforeEach
    void inicializarDados() {
        removerCursoCasoUso = new RemoverCursoCasoUso(cursoRepositorioJpa, usuarioRepositorioJpa, categoriaRepositorioJpa, "TESTE");
    };

    @AfterEach
    void limparDados() {
        cursoRepositorioJpa.deleteAll();
        usuarioRepositorioJpa.deleteAll();
        categoriaRepositorioJpa.deleteAll();
    }
    

    @Test
    @DisplayName("Should delete a Curso successfully")
    void testRemoverCursoSuccess() throws Exception {
        this.entidade = new CursoEntidade(
            UUID.randomUUID(),
            "TESTE",
            "TESTE_TESTE",
            100,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
        Page<UsuarioEntidade> pageUsers = new PageImpl<>(List.of());
        List<CategoriaEntidade> listCategoria = List.of();

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(entidade);
        when(usuarioRepositorioJpa.listUsuariosByCurso(Mockito.any(Pageable.class), Mockito.any(CursoEntidade.class))).thenReturn(pageUsers);
        when(categoriaRepositorioJpa.findByCurso(Mockito.any(CursoEntidade.class))).thenReturn(listCategoria);
        removerCursoCasoUso.validarRemocao();
        removerCursoCasoUso.removerCurso();

        verify(cursoRepositorioJpa, times(1)).findBySigla(Mockito.anyString());
        verify(usuarioRepositorioJpa, times(1)).listUsuariosByCurso(Mockito.any(Pageable.class), Mockito.any(CursoEntidade.class));
        verify(cursoRepositorioJpa, times(1)).deleteCurso(Mockito.anyString());
    }

    @Test
    @DisplayName("Try to delete a Curso and return an excepiton because exists users inside it")
    void testRemoverCursoCasoUsoExceptionCase1() throws Exception{
        this.entidade = new CursoEntidade(
            UUID.randomUUID(),
            "TESTE",
            "TESTE_TESTE",
            100,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
        List<UsuarioEntidade> userList = List.of(new UsuarioEntidade(
            9999999,
            entidade,
            "Teste", 
            "teste@teste.com",
            "teste",
            EnumRecursos.NORMAL,
            LocalDateTime.now()
        ));
        Page<UsuarioEntidade> pageUsers = new PageImpl<>(userList);
        List<CategoriaEntidade> listCategoria = List.of();

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(entidade);
        when(usuarioRepositorioJpa.listUsuariosByCurso(Mockito.any(Pageable.class), Mockito.any(CursoEntidade.class))).thenReturn(pageUsers);
        when(categoriaRepositorioJpa.findByCurso(Mockito.any(CursoEntidade.class))).thenReturn(listCategoria);
        Exception thrown = assertThrows(RemoverCursoComUsuariosException.class, () -> {
            removerCursoCasoUso.validarRemocao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Não é possível excluir um curso que possui usuários matriculados");
    }


    @Test
    @DisplayName("Try to delete a Curso and return an excepiton because exists categorias inside it")
    void testRemoverCursoCasoUsoExceptionCase2() throws Exception{
        this.entidade = new CursoEntidade(
            UUID.randomUUID(),
            "TESTE",
            "TESTE_TESTE",
            100,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
        Page<UsuarioEntidade> pageUsers = new PageImpl<>(List.of());
        List<CategoriaEntidade> listCategoria = List.of(new CategoriaEntidade(
            UUID.randomUUID(),
            entidade,
            "CATEGORIA_TESTE",
            (float) 0.5,
            (float) 1.0,
            "DESCRIICAO_TESTE"
        ));

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(entidade);
        when(usuarioRepositorioJpa.listUsuariosByCurso(Mockito.any(Pageable.class), Mockito.any(CursoEntidade.class))).thenReturn(pageUsers);
        when(categoriaRepositorioJpa.findByCurso(Mockito.any(CursoEntidade.class))).thenReturn(listCategoria);
        Exception thrown = assertThrows(RemoverCursoComCategoriasException.class, () -> {
            removerCursoCasoUso.validarRemocao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Não é possível excluir um curso que possui categorias cadastradas");
    }
}
