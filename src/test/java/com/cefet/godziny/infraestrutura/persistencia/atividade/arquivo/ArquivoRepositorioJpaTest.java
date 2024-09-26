package com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.cefet.godziny.domain.casouso.atividade.arquivo.CriarArquivoCasoUso;
import com.cefet.godziny.infraestrutura.exceptions.atividade.arquivo.ArquivoNaoEncontradoException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ArquivoRepositorioJpaTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String NOME = "Nome TESTE";
    private static final String TIPO = "Tipo TESTE";
    private static final byte[] DADOS = "conteúdo do arquivo".getBytes();
    
    private Optional<ArquivoEntidade> optional;
    private ArquivoEntidade entidade;

    @Mock
    private CriarArquivoCasoUso criarArquivoCasoUso;

    @Mock
    private ArquivoRepositorioJpaSpring arquivoRepositorioJpaSpring;

    @InjectMocks
    private ArquivoRepositorioJpa arquivoRepositorio;

    @BeforeEach
    void start() {
        MockitoAnnotations.openMocks(this);
        arquivoRepositorio = new ArquivoRepositorioJpa(arquivoRepositorioJpaSpring);
    };

    @AfterEach
    void clean() {
        arquivoRepositorio.deleteAll();
        this.optional = null;
        this.entidade = null;
    }

    @Test
    @DisplayName("Search for an Arquivo and return an existing successfully from DataBase")
    void testFindByIdSuccess() throws Exception {
        this.optional = createOptionalArquivo();

        when(arquivoRepositorioJpaSpring.findById(Mockito.any(UUID.class))).thenReturn(this.optional);
        ArquivoEntidade result = arquivoRepositorio.findById(ID);

        assertThat(result).isInstanceOf(ArquivoEntidade.class);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should create an Arquivo successfully")
    void testCreateArquivoSuccess() throws Exception{
        this.entidade = createArquivoEntidade();
        MultipartFile multipartFile = new MockMultipartFile("arquivo.txt", "arquivo.txt", "text/plain", DADOS);

        doNothing().when(criarArquivoCasoUso).validarCriacao();
        when(arquivoRepositorioJpaSpring.save(Mockito.any(ArquivoEntidade.class))).thenReturn(this.entidade);
        ArquivoEntidade result = arquivoRepositorio.createArquivo(multipartFile, NOME);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(ArquivoEntidade.class);
        assertThat(result).isEqualTo(this.entidade);
    }

    @Test
    @DisplayName("Should delete an Arquivo successfully")
    void testDeleteArquivoSuccess() throws Exception{
        this.optional = createOptionalArquivo();

        when(arquivoRepositorioJpaSpring.findById(Mockito.any(UUID.class))).thenReturn(this.optional);
        doNothing().when(arquivoRepositorioJpaSpring).deleteById(Mockito.any(UUID.class));
        arquivoRepositorio.deleteArquivo(ID);

        verify(arquivoRepositorioJpaSpring, times(1)).deleteById(Mockito.any(UUID.class));
    }

    @Test
    @DisplayName("Try to delete an Arquivo and return an excepiton because there isn't any Arquivo with that ID")
    void testDeleteArquivoArquivoNaoEncontradaException() throws Exception{
        this.optional = Optional.empty();

        when(arquivoRepositorioJpaSpring.findById(Mockito.any(UUID.class))).thenReturn(optional);
        Exception thrown = assertThrows(ArquivoNaoEncontradoException.class, () -> {
            arquivoRepositorio.deleteArquivo(ID);
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Arquivo não encontrado na base de dados");
    }

    @Test
    @DisplayName("Should delete all Arquivos successfully")
    void testDeleteAllSuccess() {
        doNothing().when(arquivoRepositorioJpaSpring).deleteAll();
        arquivoRepositorio.deleteAll();

        verify(arquivoRepositorioJpaSpring, times(1)).deleteAll();
    }


    private Optional<ArquivoEntidade> createOptionalArquivo(){
        ArquivoEntidade arquivo = new ArquivoEntidade(ID, NOME, TIPO, DADOS);
        Optional<ArquivoEntidade> arquivoOptional = Optional.of(arquivo);
        return arquivoOptional;
    }

    private ArquivoEntidade createArquivoEntidade(){
        ArquivoEntidade arquivo = new ArquivoEntidade(ID, NOME, TIPO, DADOS);
        return arquivo;
    }
}
