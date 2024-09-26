package com.cefet.godziny.domain.casouso.atividade;

import static org.mockito.ArgumentMatchers.anyInt;
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
import com.cefet.godziny.api.atividade.AtividadeAtualizarDto;
import com.cefet.godziny.constantes.atividade.EnumStatus;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.exceptions.atividade.AtualizarAtividadeStatusErradoException;
import com.cefet.godziny.infraestrutura.exceptions.atividade.CriarAtividadeIncompletaException;
import com.cefet.godziny.infraestrutura.exceptions.atividade.LimiteCargaHorariaExcedidoException;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AtualizarAtividadeCasoUsoTest {
    private AtividadeEntidade atividadeEntidade;

    @Mock
    private AtividadeRepositorioJpa atividadeRepositorioJpa;

    @Mock
    private CategoriaRepositorioJpa categoriaRepositorioJpa;

    @Mock
    private UsuarioRepositorioJpa usuarioRepositorioJpa;

    @Mock
    private ArquivoRepositorioJpa arquivoRepositorioJpa;

    private AtualizarAtivdadeCasoUso atualizarAtividadeCasoUso;

    @BeforeEach
    void inicializarDados() {
        atualizarAtividadeCasoUso = new AtualizarAtivdadeCasoUso(
            atividadeRepositorioJpa, categoriaRepositorioJpa, usuarioRepositorioJpa, arquivoRepositorioJpa,
            UUID.randomUUID(), 99999, UUID.randomUUID(),
            (float) 1.0, "comentario_TESTE", EnumStatus.APROVADA
        );
    };

    @AfterEach
    void limparDados() {
        this.atividadeEntidade = null;
        atividadeRepositorioJpa.deleteAll();
        categoriaRepositorioJpa.deleteAll();
        usuarioRepositorioJpa.deleteAll();
        arquivoRepositorioJpa.deleteAll();
    }
    
    @Test
    @DisplayName("Should valided an AtualizarAtividadeCasoUso successfully")
    void testeAtualizarAtividadeCasoUsoSuccessCase1() throws Exception {
        this.atividadeEntidade = createAtividadeEntidade();
        AtividadeAtualizarDto atividadeAtualizarDto = new AtividadeAtualizarDto(
            UUID.randomUUID(),
            this.atividadeEntidade.getUsuario().getMatricula(),
            this.atividadeEntidade.getCategoria().getId(),
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.APROVADA,
            this.atividadeEntidade.getArquivo().getId(),
            (float) 2.4,
            "comentario TESTE"
        );
        
        when(atividadeRepositorioJpa.sumCargaHorarioByUsuarioIdAndCategoriaId(anyInt(), Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getCargaHoraria());
        when(categoriaRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getCategoria());
        when(arquivoRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getArquivo());
        when(atividadeRepositorioJpa.updateAtividade(Mockito.any(AtividadeEntidade.class))).thenReturn(this.atividadeEntidade.getId());
        atualizarAtividadeCasoUso.validarAtualizacao();
        UUID response = atualizarAtividadeCasoUso.atualizarAtividade(atividadeAtualizarDto);

        assertThat(response).isInstanceOf(UUID.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to update an Atividade with STATUS REJEITADA successfully")
    void testeAtualizarAtividadeCasoUsoSuccessCase2() throws Exception{
        atualizarAtividadeCasoUso.setStatus(EnumStatus.REJEITADA);
        atualizarAtividadeCasoUso.setCargaHoraria(0);
        this.atividadeEntidade = createAtividadeEntidade();

        AtividadeAtualizarDto atividadeAtualizarDto = new AtividadeAtualizarDto(
            UUID.randomUUID(),
            this.atividadeEntidade.getUsuario().getMatricula(),
            this.atividadeEntidade.getCategoria().getId(),
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.APROVADA,
            this.atividadeEntidade.getArquivo().getId(),
            (float) 2.4,
            "comentario TESTE"
        );
        
        when(atividadeRepositorioJpa.sumCargaHorarioByUsuarioIdAndCategoriaId(anyInt(), Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getCargaHoraria());
        when(categoriaRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getCategoria());
        when(arquivoRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getArquivo());
        when(atividadeRepositorioJpa.updateAtividade(Mockito.any(AtividadeEntidade.class))).thenReturn(this.atividadeEntidade.getId());
        atualizarAtividadeCasoUso.validarAtualizacao();
        UUID response = atualizarAtividadeCasoUso.atualizarAtividade(atividadeAtualizarDto);

        assertThat(response).isInstanceOf(UUID.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to update the first Atividade of that Categoria successfully")
    void testeAtualizarAtividadeCasoUsoSuccessCase3() throws Exception{
        this.atividadeEntidade = createAtividadeEntidade();
        AtividadeAtualizarDto atividadeAtualizarDto = new AtividadeAtualizarDto(
            UUID.randomUUID(),
            this.atividadeEntidade.getUsuario().getMatricula(),
            this.atividadeEntidade.getCategoria().getId(),
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.APROVADA,
            this.atividadeEntidade.getArquivo().getId(),
            (float) 2.4,
            "comentario TESTE"
        );
        
        when(atividadeRepositorioJpa.sumCargaHorarioByUsuarioIdAndCategoriaId(anyInt(), Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(null);
        when(categoriaRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getCategoria());
        when(arquivoRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getArquivo());
        when(atividadeRepositorioJpa.updateAtividade(Mockito.any(AtividadeEntidade.class))).thenReturn(this.atividadeEntidade.getId());
        atualizarAtividadeCasoUso.validarAtualizacao();
        UUID response = atualizarAtividadeCasoUso.atualizarAtividade(atividadeAtualizarDto);

        assertThat(response).isInstanceOf(UUID.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to update an Atividade and return an excepton because the CARGAHORARIA isn't big enough")
    void testAtualizarAtividadeCasoUsoExceptionCase1() throws Exception{
        atualizarAtividadeCasoUso.setCargaHoraria(-5);

        Exception thrown = assertThrows(CriarAtividadeIncompletaException.class, () -> {
            atualizarAtividadeCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A carga horária da atividade deve ser maior ou igual que zero");
    }

    @Test
    @DisplayName("Try to update an Atividade and return an excepton because the COMENTARIO is super small")
    void testAtualizarAtividadeCasoUsoExceptionCase2() throws Exception{
        atualizarAtividadeCasoUso.setComentario("A");

        Exception thrown = assertThrows(CriarAtividadeIncompletaException.class, () -> {
            atualizarAtividadeCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O comentário da atividade deve ter entre 2 e 500 caracteres");
    }

    @Test
    @DisplayName("Try to update an Atividade and return an excepton because the COMENTARIO is too big")
    void testAtualizarAtividadeCasoUsoExceptionCase3() throws Exception{
        atualizarAtividadeCasoUso.setComentario(
            "Este componente, SelectGrupoProdutos, é utilizado para selecionar e gerenciar um grupo de produtos. \n" + 
            " * Ele integra a biblioteca Material-UI para a interface do usuário e utiliza a funcionalidade de autocompletar \n" +
            " * para facilitar a seleção de produtos. O componente calcula e atualiza a quantidade total dos produtos \n" + 
            " * selecionados usando um efeito, garantindo que o valor total esteja sempre correto. Além disso, permite a \n" +
            " * adição e remoção de produtos no grupo, evitando duplicatas. A integração com o contexto SysForm assegura \n" + 
            " * que o estado do componente seja gerenciado de forma centralizada."
        );

        Exception thrown = assertThrows(CriarAtividadeIncompletaException.class, () -> {
            atualizarAtividadeCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O comentário da atividade deve ter entre 2 e 500 caracteres");
    }

    @Test
    @DisplayName("Try to update an Atividade and return an excepton because the STATUS is SIMULANDO")
    void testAtualizarAtividadeCasoUsoExceptionCase4() throws Exception{
        atualizarAtividadeCasoUso.setStatus(EnumStatus.SIMULANDO);

        Exception thrown = assertThrows(AtualizarAtividadeStatusErradoException.class, () -> {
            atualizarAtividadeCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Não é possível atualizar atividades em simulação");
    }

    @Test
    @DisplayName("Try to update an Atividade and return an excepton because the STATUS is REJEITADA and yet it's trying to add some hours")
    void testAtualizarAtividadeCasoUsoExceptionCase5() throws Exception{
        atualizarAtividadeCasoUso.setStatus(EnumStatus.REJEITADA);

        Exception thrown = assertThrows(AtualizarAtividadeStatusErradoException.class, () -> {
            atualizarAtividadeCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Não é possível aprovar horas em uma atividade rejeitada");
    }

    @Test
    @DisplayName("Try to update an Atividade and return an excepton because the STATUS is REJEITADA and yet it's trying to add some hours")
    void testAtualizarAtividadeCasoUsoExceptionCase7() throws Exception{
        atualizarAtividadeCasoUso.setStatus(EnumStatus.APROVADA);
        atualizarAtividadeCasoUso.setCargaHoraria(0);

        Exception thrown = assertThrows(AtualizarAtividadeStatusErradoException.class, () -> {
            atualizarAtividadeCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Não é possível aprovar uma atividade com carga de zero horas");
    }

    @Test
    @DisplayName("Try to update an Atividade and return an excepton because the CARGAHORARIA is bigger than the limit")
    void testAtualizarAtividadeCasoUsoExceptionCase8() throws Exception{
        this.atividadeEntidade = createAtividadeEntidade();
        atualizarAtividadeCasoUso.setCargaHoraria(5000);

        when(atividadeRepositorioJpa.sumCargaHorarioByUsuarioIdAndCategoriaId(anyInt(), Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getCargaHoraria());
        when(categoriaRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.atividadeEntidade.getCategoria());
        Exception thrown = assertThrows(LimiteCargaHorariaExcedidoException.class, () -> {
            atualizarAtividadeCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A carga horária total deste usuário para esta categoria irá exceder o limite permitido em 852,4 horas.");
    }

    private AtividadeEntidade createAtividadeEntidade(){
        CursoEntidade CURSO = new CursoEntidade(
            UUID.randomUUID(),
            "ODONT_DIV", "Odontologia",
            300,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
        return new AtividadeEntidade(
            UUID.randomUUID(),
            new UsuarioEntidade(99999, CURSO, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.NORMAL, LocalDateTime.now()),
            new CategoriaEntidade(UUID.randomUUID(), CURSO, "nome TESTE", (float) 0.5,  (float) 0.2, "descriçao TESTE"),
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.APROVADA,
            new ArquivoEntidade(UUID.randomUUID(), "nome TESTE", "tipo TESTE", "dados TESTE".getBytes()),
            (float) 2.4,
            "comentario TESTE"
        );
    }
}
