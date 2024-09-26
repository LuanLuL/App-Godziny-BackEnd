package com.cefet.godziny.domain.casouso.categoria;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import static org.assertj.core.api.Assertions.assertThat;
import com.cefet.godziny.constantes.atividade.EnumStatus;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.exceptions.categoria.RemoverCategoriaComAtividadesException;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;

@SpringBootTest
public class RemoverCategoriaCasoUsoTest {
    
    @Mock
    private CategoriaEntidade entidade;

    @Mock
    private CategoriaRepositorioJpa categoriaRepositorioJpa;

    @Mock
    private AtividadeRepositorioJpa atividadeRepositorioJpa;

    private RemoverCategoriaCasoUso removerCategoriaCasoUso;

    @BeforeEach
    void inicializarDados() {
        removerCategoriaCasoUso = new RemoverCategoriaCasoUso(categoriaRepositorioJpa, atividadeRepositorioJpa, UUID.randomUUID());
    };

    @AfterEach
    void limparDados() {
        categoriaRepositorioJpa.deleteAll();
    }
    

    @Test
    @DisplayName("Should delete a Categoria successfully")
    void testRemoverCategoriaSuccess() throws Exception {
        this.entidade = createCategoriaEntidade();

        doNothing().when(categoriaRepositorioJpa).deleteCategoria(Mockito.any(UUID.class));
        removerCategoriaCasoUso.validarRemocao();
        removerCategoriaCasoUso.removerCategoria();

        verify(categoriaRepositorioJpa, times(1)).deleteCategoria(Mockito.any(UUID.class));
    }

    @Test
    @DisplayName("Try to delete an Categoria and return an excepiton because she has Atividades")
    void testRemoverCursoCasoUsoExceptionCase1() throws Exception{
        this.entidade = createCategoriaEntidade();
        List<AtividadeEntidade> atividadeList = List.of(new AtividadeEntidade(
            UUID.randomUUID(),
            new UsuarioEntidade(99999, createCursoEntidade(), "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.NORMAL, LocalDateTime.now()),
            this.entidade,
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.APROVADA,
            new ArquivoEntidade(UUID.randomUUID(), "nome TESTE", "tipo TESTE", "dados TESTE".getBytes()),
            (float) 2.4,
            "comentario TESTE"
        ));

        when(categoriaRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(entidade);
        when(atividadeRepositorioJpa.findByCategoria(Mockito.any(CategoriaEntidade.class))).thenReturn(atividadeList);
        Exception thrown = assertThrows(RemoverCategoriaComAtividadesException.class, () -> {
            removerCategoriaCasoUso.validarRemocao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Não é possível excluir uma categoria que possui atividades cadastradas");
    }


     private CategoriaEntidade createCategoriaEntidade(){
        CategoriaEntidade entidade = new CategoriaEntidade(
            UUID.randomUUID(), 
            createCursoEntidade(),
            "categoria_TESTE",
            (float) 0.1, (float) 0.1,
            "descricao_TESTE"
        );
        return entidade;
    }

    private CursoEntidade createCursoEntidade(){
        CursoEntidade curso = new CursoEntidade(
            UUID.randomUUID(),
            "CURSO_ID_TESTE",
            "TESTE",
            100,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
        return curso;
    }

}
