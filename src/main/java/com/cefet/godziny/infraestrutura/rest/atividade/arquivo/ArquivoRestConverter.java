package com.cefet.godziny.infraestrutura.rest.atividade.arquivo;

import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;
import com.cefet.godziny.infraestrutura.exceptions.atividade.arquivo.ArquivoNaoEncontradoException;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArquivoRestConverter {
     public static ArquivoEntidade OptionalToArquivoEntidade(Optional<ArquivoEntidade> optional) throws Exception {
        if(!optional.isPresent()){
            throw new ArquivoNaoEncontradoException();
        }
        var arquivoEntidade = new ArquivoEntidade();
        BeanUtils.copyProperties(optional.get(), arquivoEntidade);
        return arquivoEntidade;
    }

    public static ArquivoEntidade MultipartFileToEntidadeJpa(MultipartFile arquivo, String nomeArquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IOException("O arquivo está vazio ou é nulo");
        }
        return ArquivoEntidade.builder()
        .nome(nomeArquivo)
        .tipo(arquivo.getContentType())
        .dado(arquivo.getBytes())
        .build();
    }
}
