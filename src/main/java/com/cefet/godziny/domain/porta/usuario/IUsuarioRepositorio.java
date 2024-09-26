package com.cefet.godziny.domain.porta.usuario;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;

public interface IUsuarioRepositorio{

    UsuarioEntidade findById(Integer matricula) throws Exception;

    UsuarioEntidade findByEmail(String email) throws Exception;

    UsuarioEntidade findByNome(String nome) throws Exception;

    Optional<UsuarioEntidade> findByEmailOptional(String email);

    Optional<UsuarioEntidade> findByNomeOptional(String nome);

    Page<UsuarioEntidade>listUsuariosByCurso(Pageable pageable, CursoEntidade curso);

    Page<UsuarioEntidade>listUsuarios(Specification<UsuarioEntidade> specification, Pageable pageable);

    Integer createUsuario(UsuarioEntidade newUsuario);

    Integer updateUsuario(UsuarioEntidade newUsuario) throws Exception;

    void deleteUsuario(Integer matricula) throws Exception;

    void deleteAll();
}
