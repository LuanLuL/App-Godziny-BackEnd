package com.cefet.godziny.infraestrutura.rest.atividade.arquivo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoRepositorioJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.UUID;

@SpringBootTest
public class ArquivoControleTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String NOME = "exemplo.txt";
    private static final String TIPO = "texto/plain";
    private static final byte[] DADOS = "Este é um exemplo de conteúdo de texto para o arquivo.".getBytes();

    private ArquivoEntidade entidade;

    @InjectMocks
    private ArquivoControle controler;

    @Mock
    private ArquivoRepositorioJpa arquivoRepositorioJpa;

    @BeforeEach
    void inicializarDados() {
        MockitoAnnotations.openMocks(this);
        controler = new ArquivoControle(arquivoRepositorioJpa);
    };

    @AfterEach
    void limparDados() {
        this.entidade = null;
        arquivoRepositorioJpa.deleteAll();
    }

    @Test
    @DisplayName("Should download an arquivo by ID successfully")
    void testGetArquivoSuccess() throws Exception {
        this.entidade = createArquivoEntidade();

        when(arquivoRepositorioJpa.findById(Mockito.any(UUID.class))).thenReturn(this.entidade);
        ResponseEntity<Resource> response = controler.getArquivo(ID);

        assertThat(response.getBody()).isInstanceOf(Resource.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private ArquivoEntidade createArquivoEntidade(){
        ArquivoEntidade arquivo = new ArquivoEntidade(
            ID,
            NOME, 
            TIPO,
            DADOS
        );
        return arquivo;
    }
}
