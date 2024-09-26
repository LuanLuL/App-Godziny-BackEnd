package com.cefet.godziny.domain.porta.atividade;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;

public interface IAtividadeRepositorio {
    AtividadeEntidade findById(UUID id) throws Exception;
    
    Page<AtividadeEntidade> listAtividades(Specification<AtividadeEntidade> specification, Pageable pageable);

    List<AtividadeEntidade> findByCategoria(CategoriaEntidade categoria);

    List<AtividadeEntidade> findByUsuario(UsuarioEntidade usuario);

    Float sumCargaHorarioByUsuarioIdAndCategoriaId(Integer usuarioId, UUID categoriaId, UUID atividadeId) throws Exception;
    
    UUID createAtividade(AtividadeEntidade atividade);

    UUID updateAtividade(AtividadeEntidade newAtividade) throws Exception;

    void deleteAtividade(UUID id) throws Exception;

    void deleteAll();
}
