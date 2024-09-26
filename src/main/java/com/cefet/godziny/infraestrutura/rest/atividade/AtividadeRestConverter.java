package com.cefet.godziny.infraestrutura.rest.atividade;

import java.util.Optional;
import org.springframework.beans.BeanUtils;

import com.cefet.godziny.api.atividade.AtividadeAtualizarDto;
import com.cefet.godziny.api.atividade.AtividadeDto;
import com.cefet.godziny.api.atividade.AtividadeRecuperarDto;
import com.cefet.godziny.domain.casouso.atividade.AtualizarAtivdadeCasoUso;
import com.cefet.godziny.domain.casouso.atividade.CriarAtividadeCasoUso;
import com.cefet.godziny.infraestrutura.exceptions.atividade.AtividadeNaoEncontradaException;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import com.cefet.godziny.infraestrutura.rest.categoria.CategoriaRestConverter;
import com.cefet.godziny.infraestrutura.rest.usuario.UsuarioRestConverter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AtividadeRestConverter {
     public static AtividadeEntidade OptionalToAtividadeEntidade(Optional<AtividadeEntidade> optional) throws Exception {
        if(!optional.isPresent()){
            throw new AtividadeNaoEncontradaException();
        }
        var atividadeEntidade = new AtividadeEntidade();
        BeanUtils.copyProperties(optional.get(), atividadeEntidade);
        return atividadeEntidade;
    }

    public static AtividadeRecuperarDto EntidadeToAtividadeRecuperarDto(AtividadeEntidade atividadeEntidade){
        return AtividadeRecuperarDto.builder()
        .id(atividadeEntidade.getId())
        .usuario(UsuarioRestConverter.EntidadeToUsuarioRecuperarDto(atividadeEntidade.getUsuario()))
        .categoria(CategoriaRestConverter.EntidadeToCategoriaRecuperarDto(atividadeEntidade.getCategoria()))
        .titulo(atividadeEntidade.getTitulo())
        .createdAt(atividadeEntidade.getCreatedAt())
        .status(atividadeEntidade.getStatus())
        .arquivoId(atividadeEntidade.getArquivo().getId())
        .cargaHoraria(atividadeEntidade.getCargaHoraria())
        .comentario(atividadeEntidade.getComentario())
        .build();
    }

    public static AtividadeEntidade DtoToEntidadeJpa(
            AtividadeDto dto,
            UsuarioEntidade usuarioEntidade,
            CategoriaEntidade categoriaEntidade,
            ArquivoEntidade arquivoEntidade
        ){
        return AtividadeEntidade.builder()
        .id(dto.getId())
        .usuario(usuarioEntidade)
        .categoria(categoriaEntidade)
        .titulo(dto.getTitulo())
        .createdAt(dto.getCreatedAt())
        .status(dto.getStatus())
        .arquivo(arquivoEntidade)
        .cargaHoraria(dto.getCargaHoraria())
        .comentario(dto.getComentario())
        .build();
    }

    public static AtividadeEntidade DtoToEntidadeJpa(
            AtividadeAtualizarDto dto, 
            UsuarioEntidade usuarioEntidade,
            CategoriaEntidade categoriaEntidade,
            ArquivoEntidade arquivoEntidade
        ){
        return AtividadeEntidade.builder()
        .id(dto.getId())
        .usuario(usuarioEntidade)
        .categoria(categoriaEntidade)
        .titulo(dto.getTitulo())
        .createdAt(dto.getCreatedAt())
        .status(dto.getStatus())
        .arquivo(arquivoEntidade)
        .cargaHoraria(dto.getCargaHoraria())
        .comentario(dto.getComentario())
        .build();
    }

    public static CriarAtividadeCasoUso DtoToCriarAtividadeCasoUso(
        AtividadeDto dto, AtividadeRepositorioJpa atividadeRepositorioJpa, 
        CategoriaRepositorioJpa categoriaRepositorioJpa, 
        UsuarioRepositorioJpa usuarioRepositorioJpa, 
        ArquivoRepositorioJpa arquivoRepositorioJpa
    ){
        return CriarAtividadeCasoUso.builder()
        .atividadeRepositorioJpa(atividadeRepositorioJpa)
        .categoriaRepositorioJpa(categoriaRepositorioJpa)
        .arquivoRepositorioJpa(arquivoRepositorioJpa)
        .usuarioRepositorioJpa(usuarioRepositorioJpa)
        .titulo(dto.getTitulo())
        .createdAt(dto.getCreatedAt())
        .build();
    }

    public static AtualizarAtivdadeCasoUso DtoToUpdateCursoCasoUso(
        AtividadeAtualizarDto dto, AtividadeRepositorioJpa atividadeRepositorioJpa, 
        CategoriaRepositorioJpa categoriaRepositorioJpa, 
        UsuarioRepositorioJpa usuarioRepositorioJpa, 
        ArquivoRepositorioJpa arquivoRepositorioJpa
    ){
        return AtualizarAtivdadeCasoUso.builder()
        .atividadeRepositorioJpa(atividadeRepositorioJpa)
        .categoriaRepositorioJpa(categoriaRepositorioJpa)
        .arquivoRepositorioJpa(arquivoRepositorioJpa)
        .usuarioRepositorioJpa(usuarioRepositorioJpa)
        .atividadeId(dto.getId())
        .usuarioId(dto.getUsuarioId())
        .categoriaId(dto.getCategoriaId())
        .cargaHoraria(dto.getCargaHoraria())
        .comentario(dto.getComentario())
        .status(dto.getStatus())
        .build();
    }
}
