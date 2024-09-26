package com.cefet.godziny.infraestrutura.rest.curso;

import lombok.NoArgsConstructor;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import com.cefet.godziny.api.curso.CursoDto;
import com.cefet.godziny.api.curso.CursoRecuperarDto;
import com.cefet.godziny.domain.casouso.curso.CriarCursoCasoUso;
import com.cefet.godziny.domain.casouso.curso.AtualizarCursoCasoUso;
import com.cefet.godziny.infraestrutura.exceptions.curso.CursoNaoEncontradoException;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import com.cefet.godziny.infraestrutura.rest.usuario.UsuarioRestConverter;

import lombok.AccessLevel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CursoRestConverter {

    public static CursoEntidade OptionalToCursoEntidade(Optional<CursoEntidade> optional) throws Exception{
        if(!optional.isPresent()){
            throw new CursoNaoEncontradoException();
        }
        var cursoEntidade = new CursoEntidade();
        BeanUtils.copyProperties(optional.get(), cursoEntidade);
        return cursoEntidade;
    }

    public static CursoDto EntidadeToCursoDto(CursoEntidade entidade){
        return CursoDto.builder()
        .id(entidade.getId())
        .sigla(entidade.getSigla())
        .nome(entidade.getNome())
        .carga_horaria_complementar(entidade.getCarga_horaria_complementar())
        .coordenador_matricula(entidade.getCoordenador().getMatricula())
        .build();
    }

    public static CursoRecuperarDto EntidadeToCursoRecuperarDto(CursoEntidade entidade){
        if(entidade != null){
            return CursoRecuperarDto.builder()
            .id(entidade.getId())
            .sigla(entidade.getSigla())
            .nome(entidade.getNome())
            .carga_horaria_complementar(entidade.getCarga_horaria_complementar())
            .coordenador(UsuarioRestConverter.EntidadeToUsuarioRecuperarDto(entidade.getCoordenador()))
            .build();
        }
        return null;
    }

    public static CursoEntidade DtoToEntidadeJpa(CursoDto dto, UsuarioEntidade coordenador) {
        return CursoEntidade.builder()
        .id(dto.getId())
        .sigla(dto.getSigla())
        .nome(dto.getNome())
        .carga_horaria_complementar(dto.getCarga_horaria_complementar())
        .coordenador(coordenador)
        .build();
    }

    public static CriarCursoCasoUso DtoToCriarCursoCasoUso(CursoDto dto, CursoRepositorioJpa cursoRepositorioJpa, UsuarioRepositorioJpa usuarioRepositorioJpa) {
        return CriarCursoCasoUso.builder()
        .cursoRepositorioJpa(cursoRepositorioJpa)
        .usuarioRepositorioJpa(usuarioRepositorioJpa)
        .sigla(dto.getSigla())
        .nome(dto.getNome())
        .cargaHorariaComplementar(dto.getCarga_horaria_complementar())
        .coordenador_matricula(dto.getCoordenador_matricula())
        .build();
    }

    public static AtualizarCursoCasoUso DtoToUpdateCursoCasoUso(String cursoSigla, CursoDto dto, CursoRepositorioJpa cursoRepositorioJpa, UsuarioRepositorioJpa usuarioRepositorioJpa) {
        return AtualizarCursoCasoUso.builder()
        .cursoRepositorioJpa(cursoRepositorioJpa)
        .usuarioRepositorioJpa(usuarioRepositorioJpa)
        .id(dto.getId())
        .sigla(dto.getSigla())
        .siglaAntiga(cursoSigla)
        .nome(dto.getNome())
        .cargaHorariaComplementar(dto.getCarga_horaria_complementar())
        .coordenador_matricula(dto.getCoordenador_matricula())
        .build();
    }
}
