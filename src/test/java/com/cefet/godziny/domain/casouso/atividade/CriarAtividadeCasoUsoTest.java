package com.cefet.godziny.domain.casouso.atividade;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.cefet.godziny.api.atividade.AtividadeDto;
import com.cefet.godziny.constantes.atividade.EnumStatus;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.exceptions.atividade.CriarAtividadeIncompletaException;
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
public class CriarAtividadeCasoUsoTest {
    private AtividadeEntidade atividadeEntidade;

    @Mock
    private AtividadeRepositorioJpa atividadeRepositorioJpa;

    @Mock
    private CategoriaRepositorioJpa categoriaRepositorioJpa;

    @Mock
    private UsuarioRepositorioJpa usuarioRepositorioJpa;

    @Mock
    private ArquivoRepositorioJpa arquivoRepositorioJpa;

    private CriarAtividadeCasoUso criarAtividadeCasoUso;

    @BeforeEach
    void inicializarDados() {
        criarAtividadeCasoUso = new CriarAtividadeCasoUso(
            atividadeRepositorioJpa, categoriaRepositorioJpa, usuarioRepositorioJpa, arquivoRepositorioJpa,
            "titulo_TESTE", LocalDateTime.now()
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
    @DisplayName("Should valided an CriarAtividadeCasoUso successfully")
    void testeCriarAtividadeCasoUsoSuccess() throws Exception {
        this.atividadeEntidade = createAtividadeEntidade();
        MultipartFile multipartFile = new MockMultipartFile("arquivo.txt", "arquivo.txt", "text/plain", "conteúdo do arquivo".getBytes());
        AtividadeDto atividadeDto = new AtividadeDto(
            this.atividadeEntidade.getId(),
            this.atividadeEntidade.getUsuario().getMatricula(),
            this.atividadeEntidade.getCategoria().getId(),
            "nome atividade TESTE",
            LocalDateTime.now(),
            EnumStatus.APROVADA,
            this.atividadeEntidade.getArquivo().getId(),
            (float) 0,
            null
        );

        when(arquivoRepositorioJpa.createArquivo(Mockito.any(MultipartFile.class), Mockito.anyString())).thenReturn(this.atividadeEntidade.getArquivo());
        when(atividadeRepositorioJpa.createAtividade(Mockito.any(AtividadeEntidade.class))).thenReturn(this.atividadeEntidade.getId());
        criarAtividadeCasoUso.validarCriacao();
        UUID response = criarAtividadeCasoUso.createAtividade(atividadeDto, multipartFile);

        assertThat(response).isInstanceOf(UUID.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to create an Atividade and return an excepiton because the DATA is future")
    void testCriarAtividadeCasoUsoExceptionCase1() throws Exception{
        criarAtividadeCasoUso.setCreatedAt(LocalDateTime.now().plusMonths(1));

        Exception thrown = assertThrows(CriarAtividadeIncompletaException.class, () -> {
            criarAtividadeCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A data de criação da atividade deve ser menor ou igual à data e hora atuais");
    }

    @Test
    @DisplayName("Try to create an Atividade and return an excepiton because the TITULO is too BIG")
    void testCriarAtividadeCasoUsoExceptionCase2() throws Exception{
        criarAtividadeCasoUso.setTitulo(
            "Este componente, SelectGrupoProdutos, é utilizado para selecionar e gerenciar um grupo de produtos. \n" + 
            " * Ele integra a biblioteca Material-UI para a interface do usuário e utiliza a funcionalidade de autocompletar \n" +
            " * para facilitar a seleção de produtos. O componente calcula e atualiza a quantidade total dos produtos \n" + 
            " * selecionados usando um efeito, garantindo que o valor total esteja sempre correto. Além disso, permite a \n" +
            " * adição e remoção de produtos no grupo, evitando duplicatas. A integração com o contexto SysForm assegura \n" + 
            " * que o estado do componente seja gerenciado de forma centralizada."
        );

        Exception thrown = assertThrows(CriarAtividadeIncompletaException.class, () -> {
            criarAtividadeCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O título da atividade deve ter entre 3 e 500 caracteres");
    }

    @Test
    @DisplayName("Try to create an Atividade and return an excepiton because the TITULO is too short")
    void testCriarAtividadeCasoUsoExceptionCase3() throws Exception{
        criarAtividadeCasoUso.setTitulo("T");

        Exception thrown = assertThrows(CriarAtividadeIncompletaException.class, () -> {
            criarAtividadeCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O título da atividade deve ter entre 3 e 500 caracteres");
    }


    private AtividadeEntidade createAtividadeEntidade(){
        CursoEntidade CURSO = new CursoEntidade(
            UUID.randomUUID(),
            "ODONT_DIV",
            "Odontologia",
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
