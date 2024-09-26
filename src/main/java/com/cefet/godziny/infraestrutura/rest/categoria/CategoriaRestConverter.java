package com.cefet.godziny.infraestrutura.rest.categoria;

import java.util.Optional;
import org.springframework.beans.BeanUtils;
import com.cefet.godziny.api.categoria.CategoriaDto;
import com.cefet.godziny.api.categoria.CategoriaRecuperarDto;
import com.cefet.godziny.domain.casouso.categoria.AtualizarCategoriaCasoUso;
import com.cefet.godziny.domain.casouso.categoria.CriarCategoriaCasoUso;
import com.cefet.godziny.infraestrutura.exceptions.categoria.CategoriaNaoEncontradaException;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.rest.curso.CursoRestConverter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriaRestConverter {
    public static CategoriaEntidade OptionalToCategoriaEntidade(Optional<CategoriaEntidade> optional) throws Exception {
        if(!optional.isPresent()){
            throw new CategoriaNaoEncontradaException();
        }
        var categoriaEntidade = new CategoriaEntidade();
        BeanUtils.copyProperties(optional.get(), categoriaEntidade);
        return categoriaEntidade;
    }

     public static CategoriaRecuperarDto EntidadeToCategoriaRecuperarDto(CategoriaEntidade categoriaEntidade){
        return CategoriaRecuperarDto.builder()
        .id(categoriaEntidade.getId())
        .curso(CursoRestConverter.EntidadeToCursoDto(categoriaEntidade.getCurso()))
        .nome(categoriaEntidade.getNome())
        .porcentagemHorasMaximas(categoriaEntidade.getPorcentagemHorasMaximas())
        .horasMultiplicador(categoriaEntidade.getHorasMultiplicador())
        .descricao(categoriaEntidade.getDescricao())
        .build();
    }

    public static CategoriaEntidade DtoToEntidadeJpa(CategoriaDto dto, CursoEntidade cursoEntidade){
        return CategoriaEntidade.builder()
        .id(dto.getId())
        .curso(cursoEntidade)
        .nome(dto.getNome())
        .porcentagemHorasMaximas(dto.getPorcentagemHorasMaximas())
        .horasMultiplicador(dto.getHorasMultiplicador())
        .descricao(dto.getDescricao())
        .build();
    }

    public static CriarCategoriaCasoUso DtoToCriarCategoriaCasoUso(CategoriaDto dto, CategoriaRepositorioJpa categoriaRepositorioJpa, CursoRepositorioJpa cursoRepositorioJpa){
        return CriarCategoriaCasoUso.builder()
        .nome(dto.getNome())
        .cursoSigla(dto.getCursoSigla())
        .porcentagemHorasMaximas(dto.getPorcentagemHorasMaximas())
        .horasMultiplicador(dto.getHorasMultiplicador())
        .descricao(dto.getDescricao())
        .categoriaRepositorioJpa(categoriaRepositorioJpa)
        .cursoRepositorioJpa(cursoRepositorioJpa)
        .build();
    }

    public static AtualizarCategoriaCasoUso DtoToUpdateCursoCasoUso(CategoriaDto dto, CategoriaRepositorioJpa categoriaRepositorioJpa, CursoRepositorioJpa cursoRepositorioJpa){
        return AtualizarCategoriaCasoUso.builder()
        .categoriaId(dto.getId())
        .nome(dto.getNome())
        .cursoSigla(dto.getCursoSigla())
        .porcentagemHorasMaximas(dto.getPorcentagemHorasMaximas())
        .horasMultiplicador(dto.getHorasMultiplicador())
        .descricao(dto.getDescricao())
        .categoriaRepositorioJpa(categoriaRepositorioJpa)
        .cursoRepositorioJpa(cursoRepositorioJpa)
        .build();
    }
}
