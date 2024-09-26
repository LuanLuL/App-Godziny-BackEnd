package com.cefet.godziny.domain.porta.categoria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;

public interface ICategoriaRepositorio {
    CategoriaEntidade findById(UUID id) throws Exception;

    List<CategoriaEntidade> findByCursoAndNome(CursoEntidade curso, String nome);

    List<CategoriaEntidade> findByCurso(CursoEntidade curso);

    Optional<CategoriaEntidade> findByNomeOptional(String nome);

    Page<CategoriaEntidade> listCategorias(Specification<CategoriaEntidade> specification, Pageable pageable);
    
    UUID createCategoria(CategoriaEntidade categoria);

    UUID updateCategoria(CategoriaEntidade newCategoria) throws Exception;

    void deleteCategoria(UUID id) throws Exception;

    void deleteAll();
}
