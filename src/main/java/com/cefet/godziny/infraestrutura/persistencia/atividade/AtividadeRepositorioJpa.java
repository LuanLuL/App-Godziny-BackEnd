package com.cefet.godziny.infraestrutura.persistencia.atividade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.cefet.godziny.domain.porta.atividade.IAtividadeRepositorio;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpaSpring;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpaSpring;
import com.cefet.godziny.infraestrutura.rest.atividade.AtividadeRestConverter;
import com.cefet.godziny.infraestrutura.rest.categoria.CategoriaRestConverter;
import com.cefet.godziny.infraestrutura.rest.usuario.UsuarioRestConverter;

@Component
public class AtividadeRepositorioJpa implements IAtividadeRepositorio {
    @Autowired
    private AtividadeRepositorioJpaSpring atividadeRepositorio;

    @Autowired
    private CategoriaRepositorioJpaSpring categoriaRepositorio;

    @Autowired
    private UsuarioRepositorioJpaSpring usuarioRepositorio;

    public AtividadeRepositorioJpa(AtividadeRepositorioJpaSpring atividadeRepositorio, CategoriaRepositorioJpaSpring categoriaRepositorio, UsuarioRepositorioJpaSpring usuarioRepositorio){
        this.atividadeRepositorio = atividadeRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public AtividadeEntidade findById(UUID id) throws Exception {
        Optional<AtividadeEntidade> atividade = atividadeRepositorio.findById(id);
        return AtividadeRestConverter.OptionalToAtividadeEntidade(atividade);
    }

    @Override
    public Page<AtividadeEntidade> listAtividades(Specification<AtividadeEntidade> specification, Pageable pageable) {
        return atividadeRepositorio.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public List<AtividadeEntidade> findByCategoria(CategoriaEntidade categoria) {
        return atividadeRepositorio.findByCategoria(categoria);
    }

    @Override
    @Transactional
    public List<AtividadeEntidade> findByUsuario(UsuarioEntidade usuario) {
        return atividadeRepositorio.findByUsuario(usuario);
    }

    @Override
    @Transactional
    public Float sumCargaHorarioByUsuarioIdAndCategoriaId(Integer usuarioId, UUID categoriaId, UUID atividadeId) throws Exception {
        UsuarioRestConverter.OptionalToUsuarioEntidade(usuarioRepositorio.findById(usuarioId));
        CategoriaRestConverter.OptionalToCategoriaEntidade(categoriaRepositorio.findById(categoriaId));
        AtividadeRestConverter.OptionalToAtividadeEntidade(atividadeRepositorio.findById(atividadeId));
        return atividadeRepositorio.sumCargaHorariaByUsuarioAndCategoria(usuarioId, categoriaId, atividadeId);
    }

    @Override
    public UUID createAtividade(AtividadeEntidade newAtividade) {
        return atividadeRepositorio.save(newAtividade).getId();
    }

    @Override
    public UUID updateAtividade(AtividadeEntidade newAtividade) throws Exception {
        Optional<AtividadeEntidade> atividade = this.atividadeRepositorio.findById(newAtividade.getId());
        AtividadeRestConverter.OptionalToAtividadeEntidade(atividade);
        return atividadeRepositorio.save(newAtividade).getId();
    }

    @Override
    public void deleteAtividade(UUID id) throws Exception {
        Optional<AtividadeEntidade> atividade = this.atividadeRepositorio.findById(id);
        AtividadeRestConverter.OptionalToAtividadeEntidade(atividade);
        atividadeRepositorio.deleteById(id);
    }

    @Override
    public void deleteAll() {
        atividadeRepositorio.deleteAll();
    }
}
