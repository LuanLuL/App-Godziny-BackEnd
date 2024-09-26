package com.cefet.godziny.domain.casouso.atividade;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
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
import com.cefet.godziny.infraestrutura.exceptions.atividade.RemoverAtividadeSimulandoOuAprovadaException;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;

@SpringBootTest
public class RemoverAtividadeCasoUsoTest {
    
    @Mock
    private AtividadeEntidade entidade;

    @Mock
    private ArquivoRepositorioJpa arquivoRepositorioJpa;

    @Mock
    private AtividadeRepositorioJpa atividadeRepositorioJpa;

    private RemoverAtividadeCasoUso removerAtividadeCasoUso;

    @BeforeEach
    void inicializarDados() {
        removerAtividadeCasoUso = new RemoverAtividadeCasoUso(UUID.randomUUID(), atividadeRepositorioJpa, arquivoRepositorioJpa);
    };

    @AfterEach
    void limparDados() {
      atividadeRepositorioJpa.deleteAll();
      arquivoRepositorioJpa.deleteAll();
    }
    

    @Test
    @DisplayName("Should delete an Atividade successfully")
    void testRemoverAtividadeSuccess() throws Exception {
        this.entidade = createAtividadeEntidade();

        when(atividadeRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.entidade);
        doNothing().when(atividadeRepositorioJpa).deleteAtividade(Mockito.any(UUID.class));
        doNothing().when(arquivoRepositorioJpa).deleteArquivo(Mockito.any(UUID.class));
        removerAtividadeCasoUso.removerAtividade(removerAtividadeCasoUso.validarRemocao());

        verify(atividadeRepositorioJpa, times(1)).deleteAtividade(Mockito.any(UUID.class));
        verify(arquivoRepositorioJpa, times(1)).deleteArquivo(Mockito.any(UUID.class));
    }

    @Test
    @DisplayName("Try to delete an Atividade and return an excepiton because it has STATUS equal SIMULANDO")
    void testRemoverCursoCasoUsoExceptionCase1() throws Exception{
        this.entidade = createAtividadeEntidade();
        this.entidade.setStatus(EnumStatus.SIMULANDO);
    
        when(atividadeRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.entidade);
        Exception thrown = assertThrows(RemoverAtividadeSimulandoOuAprovadaException.class, () -> {
            removerAtividadeCasoUso.validarRemocao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Atividades em simulação ou aprovadas não podem ser removidas");
    }


    private AtividadeEntidade createAtividadeEntidade(){
        CursoEntidade CURSO = new CursoEntidade(
            UUID.randomUUID(),
            "ODONT_DIV",
            "Odontologia",
            1,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );

        return new AtividadeEntidade(
            UUID.randomUUID(),
            new UsuarioEntidade(
                99999,
                CURSO,
                "nome TESTE",
                "teste@test.com",
                "senha TESTE",
                EnumRecursos.NORMAL,
                LocalDateTime.now()
            ),
            new CategoriaEntidade(
                UUID.randomUUID(),
                CURSO,
                "nome TESTE",
                (float) 0.5,
                (float) 0.2, 
                "descriçao TESTE"
            ),
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.REJEITADA,
            new ArquivoEntidade(
                UUID.randomUUID(),
                "nome TESTE",
                "tipo TESTE",
                "dados TESTE".getBytes()
            ),
            (float) 2.4,
            "comentario TESTE"
        );
    }
}
