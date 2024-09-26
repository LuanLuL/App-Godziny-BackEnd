package com.cefet.godziny.infraestrutura.persistencia.curso;

import java.io.Serializable;
import java.util.UUID;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_curso")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursoEntidade implements Serializable {
    private static final long serialVersionUID  = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String sigla;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private int carga_horaria_complementar;

    @ManyToOne
    @JoinColumn(name = "coordenador_id", referencedColumnName = "matricula")
    private UsuarioEntidade coordenador;
}

