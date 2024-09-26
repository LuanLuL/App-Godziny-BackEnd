package com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoRepositorioJpaSpring extends JpaRepository<ArquivoEntidade, UUID>{}
