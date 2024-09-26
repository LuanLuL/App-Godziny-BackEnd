package com.cefet.godziny.infraestrutura.persistencia.categoria;

import java.io.Serializable;
import java.util.UUID;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
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

@Entity(name = "categoria")
@Table(name = "tb_categoria")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaEntidade implements Serializable {
    private static final long serialVersionUID  = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "curso_id", referencedColumnName = "id")
    private CursoEntidade curso;

    @Column(nullable = false)
    private String nome;

    @Column(name = "porcentagem_horas_maximas", nullable = false)
    private float porcentagemHorasMaximas;

    @Column(name = "horas_multiplicador",nullable = false)
    private float horasMultiplicador;

    @Column(nullable = false, length = 500)
    private String descricao;
}

