package com.cefet.godziny.domain.casouso.usuario;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
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
import com.cefet.godziny.constantes.atividade.EnumStatus;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.exceptions.usuario.RemoverUsuarioComAtividadesException;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;

@SpringBootTest
public class RemoverUsuarioCasoUsoTest {
    
    @Mock
    private UsuarioEntidade entidade;

    @Mock
    UsuarioRepositorioJpa usuarioRepositorioJpa;

    @Mock
    private AtividadeRepositorioJpa atividadeRepositorioJpa;

    private RemoverUsuarioCasoUso removerUsuarioCasoUso;

    @BeforeEach
    void inicializarDados() {
        removerUsuarioCasoUso = new RemoverUsuarioCasoUso(99999, usuarioRepositorioJpa, atividadeRepositorioJpa);
    };

    @AfterEach
    void limparDados() {
        usuarioRepositorioJpa.deleteAll();
    }

    @Test
    @DisplayName("Should delete an Usuario successfully")
    void testRemoverUsuarioSuccess() throws Exception {
        this.entidade = createUsuarioEntidade();

        doNothing().when(usuarioRepositorioJpa).deleteUsuario(Mockito.anyInt());
        removerUsuarioCasoUso.validarRemocao();
        removerUsuarioCasoUso.removerUsuario();

        verify(usuarioRepositorioJpa, times(1)).deleteUsuario(Mockito.anyInt());
    }

    @Test
    @DisplayName("Try to delete an Usuario and return an excepiton because he has Atividades")
    void testRemoverCursoCasoUsoExceptionCase1() throws Exception{
        this.entidade = createUsuarioEntidade();
        List<AtividadeEntidade> atividadeList = List.of(new AtividadeEntidade(
            UUID.randomUUID(),
            this.entidade,
            new CategoriaEntidade(UUID.randomUUID(), createCursoEntidade(), "Categoria TESTE", 20, 2, "descrição TESTE"),
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.APROVADA,
            new ArquivoEntidade(UUID.randomUUID(), "nome TESTE", "tipo TESTE", "dado TESTE".getBytes()),
            (float) 2.4,
            "comentario TESTE"
        ));

        when(usuarioRepositorioJpa.findById(Mockito.anyInt())).thenReturn(entidade);
        when(atividadeRepositorioJpa.findByUsuario(Mockito.any(UsuarioEntidade.class))).thenReturn(atividadeList);
        Exception thrown = assertThrows(RemoverUsuarioComAtividadesException.class, () -> {
            removerUsuarioCasoUso.validarRemocao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Não é possível excluir um usuário que possui atividades cadastradas");
    }


     private UsuarioEntidade createUsuarioEntidade(){
        UsuarioEntidade entidade = new UsuarioEntidade(
            999999, 
            createCursoEntidade(),
            "TESTE",
            "teste@teste.com.br",
            "teste123",
            EnumRecursos.NORMAL,
            LocalDateTime.now()
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
