package com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.cefet.godziny.domain.casouso.atividade.arquivo.CriarArquivoCasoUso;
import com.cefet.godziny.domain.porta.atividade.arquivo.IArquivoRepositorio;
import com.cefet.godziny.infraestrutura.rest.atividade.arquivo.ArquivoRestConverter;

@Component
public class ArquivoRepositorioJpa implements IArquivoRepositorio {
    @Autowired
    private ArquivoRepositorioJpaSpring repositorio;

    public ArquivoRepositorioJpa(ArquivoRepositorioJpaSpring repositorio){
        this.repositorio = repositorio;
    }

    @Override
    public ArquivoEntidade findById(UUID id) throws Exception {
        Optional<ArquivoEntidade> arquivo = repositorio.findById(id);
        return ArquivoRestConverter.OptionalToArquivoEntidade(arquivo);
    }

    @Override
    public ArquivoEntidade createArquivo(MultipartFile arquivo, String nomeArquivo) throws Exception{
        CriarArquivoCasoUso casoUso = new CriarArquivoCasoUso(repositorio, nomeArquivo);
        casoUso.validarCriacao();
        return casoUso.createArquivo(arquivo);
    }

    @Override
    public void deleteArquivo(UUID id) throws Exception {
        Optional<ArquivoEntidade> arquivo = this.repositorio.findById(id);
        ArquivoRestConverter.OptionalToArquivoEntidade(arquivo);
        repositorio.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repositorio.deleteAll();
    }
}
