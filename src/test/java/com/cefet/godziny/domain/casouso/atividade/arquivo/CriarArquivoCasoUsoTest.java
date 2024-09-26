package com.cefet.godziny.domain.casouso.atividade.arquivo;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.cefet.godziny.infraestrutura.exceptions.atividade.arquivo.ArquivoInvalidoException;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoRepositorioJpaSpring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CriarArquivoCasoUsoTest {
	private ArquivoEntidade arquivoEntidade;
    private MultipartFile multipartFile = new MockMultipartFile("arquivo.txt", "arquivo.txt", "text/plain", "Este é um exemplo de conteúdo de texto para o arquivo.".getBytes());

    @Mock
    ArquivoRepositorioJpaSpring arquivoRepositorioJpa;

    @InjectMocks
    private CriarArquivoCasoUso criarArquivoCasoUso;

    @BeforeEach
    void inicializarDados() {
        criarArquivoCasoUso = new CriarArquivoCasoUso(arquivoRepositorioJpa, "nome_TESTE");
    };

    @AfterEach
    void limparDados() {
        arquivoRepositorioJpa.deleteAll();
    }
    
    @Test
    @DisplayName("Should valided an CriarArquivoCasoUso successfully")
    void testeCriarArquivoCasoUsoSuccess() throws Exception {
        this.arquivoEntidade = createArquivoEntidade();

        when(arquivoRepositorioJpa.save(Mockito.any(ArquivoEntidade.class))).thenReturn(this.arquivoEntidade);
        criarArquivoCasoUso.validarCriacao();
        ArquivoEntidade response = criarArquivoCasoUso.createArquivo(this.multipartFile);

        assertThat(response).isInstanceOf(ArquivoEntidade.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Tries to create an Arquivo and return an exception because its name is wrong")
    void testCriarArquivoCasoUsoExceptionCase2() throws Exception{
        this.criarArquivoCasoUso.setNome("../nome_TESTE^^.;");

        Exception thrown = assertThrows(ArquivoInvalidoException.class, () -> {
            criarArquivoCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O nome do arquivo fornecido como comprovante contém um caminho inválido: ../nome_TESTE^^.;");
    }

    @Test
    @DisplayName("Tries to create an Arquivo and return an exception because it is empty")
    void testCriarArquivoCasoUsoExceptionCase3() throws Exception{
        MultipartFile arquivoVazio = new MockMultipartFile("arquivo.txt", "arquivo.txt", "text/plain", new byte[0]);

        Exception thrown = assertThrows(IOException.class, () -> {
            criarArquivoCasoUso.validarCriacao();
            criarArquivoCasoUso.createArquivo(arquivoVazio);
        });
    
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O arquivo está vazio ou é nulo");
    }

    @Test
    @DisplayName("Tries to create an Arquivo and return an exception because the arquivo is null")
    void testCriarArquivoCasoUsoExceptionNullFile() throws Exception {
        criarArquivoCasoUso.setNome("arquivo_null.txt");

        Exception thrown = assertThrows(IOException.class, () -> {
            criarArquivoCasoUso.createArquivo(null);
        });

        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O arquivo está vazio ou é nulo");
    }

    private ArquivoEntidade createArquivoEntidade(){
        ArquivoEntidade arquivo = new ArquivoEntidade(
            UUID.randomUUID(),
            "exemplo.txt",
            "texto/plain",
            "Este é um exemplo de conteúdo de texto para o arquivo.".getBytes()
        );
        return arquivo;
    }
}