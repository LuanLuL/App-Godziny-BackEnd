package com.cefet.godziny.infraestrutura.persistencia.usuario;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.cefet.godziny.domain.porta.usuario.IUsuarioRepositorio;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.rest.usuario.UsuarioRestConverter;

@Component
public class UsuarioRepositorioJpa implements IUsuarioRepositorio {

    @Autowired
    private UsuarioRepositorioJpaSpring repositorio;

    public UsuarioRepositorioJpa(UsuarioRepositorioJpaSpring repositorio){
        this.repositorio = repositorio;
    }

    @Override
    public UsuarioEntidade findById(Integer matricula) throws Exception {
        Optional<UsuarioEntidade> entidade = repositorio.findById(matricula);
        return UsuarioRestConverter.OptionalToUsuarioEntidade(entidade);
    }

    @Override
    public UsuarioEntidade findByEmail(String email) throws Exception{
        Optional<UsuarioEntidade> entidade = repositorio.findByEmail(email);
        return UsuarioRestConverter.OptionalToUsuarioEntidade(entidade);
    }

    @Override
    public UsuarioEntidade findByNome(String nome) throws Exception {
        Optional<UsuarioEntidade> entidade = repositorio.findByNome(nome);
        return UsuarioRestConverter.OptionalToUsuarioEntidade(entidade);
    }

    @Override
    public Optional<UsuarioEntidade > findByNomeOptional(String nome) {
        return repositorio.findByNome(nome);
    }

    @Override
    public Optional<UsuarioEntidade > findByEmailOptional(String email) {
        return repositorio.findByEmail(email);
    }

    @Override
    public Page<UsuarioEntidade> listUsuarios(Specification<UsuarioEntidade> specification, Pageable pageable) {
        return repositorio.findAll(specification, pageable);
    }

    @Override
    public Page<UsuarioEntidade> listUsuariosByCurso(Pageable pageable, CursoEntidade curso) {
        return repositorio.findByCurso(curso, pageable);
    }

    @Override
    public Integer createUsuario(UsuarioEntidade newUsuario) {
        return repositorio.save(newUsuario).getMatricula();
    }

    @Override
    public Integer updateUsuario(UsuarioEntidade newUsuario) throws Exception {
        Optional<UsuarioEntidade> usuario = this.repositorio.findById(newUsuario.getMatricula());
        UsuarioRestConverter.OptionalToUsuarioEntidade(usuario);
        return repositorio.save(newUsuario).getMatricula();
    }
    
    @Override
    public void deleteUsuario(Integer matricula) throws Exception {
        Optional<UsuarioEntidade> usuario = this.repositorio.findById(matricula);
        UsuarioRestConverter.OptionalToUsuarioEntidade(usuario);
        repositorio.deleteById(matricula);
    }

    @Override
    public void deleteAll(){
        repositorio.deleteAll();
    }
}
