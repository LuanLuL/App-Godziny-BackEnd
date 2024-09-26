package com.cefet.godziny.domain.porta.atividade.arquivo;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;

public interface IArquivoRepositorio {
    ArquivoEntidade findById(UUID id) throws Exception;

    ArquivoEntidade createArquivo(MultipartFile arquivo, String nomeArquivo) throws Exception;

    void deleteArquivo(UUID id) throws Exception;

    void deleteAll();
}
