package com.cefet.godziny.domain.porta.curso;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;

public interface ICursoRepositorio {

    CursoEntidade findBySigla(String sigla) throws Exception;

    Optional<CursoEntidade>findBySiglaOptional(String sigla);

    List<UUID> findByCoordenador(Integer matricula);

    Page<CursoEntidade> listCursos(Specification<CursoEntidade> specification, Pageable pageable);
    
    String createCurso(CursoEntidade curso);

    String updateCurso(String cursoSigla, CursoEntidade newCurso) throws Exception;

    void deleteCurso(String id) throws Exception;

    void deleteAll();
}