package com.cefet.godziny.infraestrutura.persistencia.atividade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import com.cefet.godziny.constantes.atividade.EnumStatus;
import com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo.ArquivoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "atividade")
@Table(name = "tb_atividade")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeEntidade implements Serializable {
    private static final long serialVersionUID  = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "matricula")
    private UsuarioEntidade usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    private CategoriaEntidade categoria;

    @Column(length = 500, nullable = false, name = "titulo")
    private String titulo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumStatus status;

    @OneToOne
    @JoinColumn(name = "arquivo_id", referencedColumnName = "id")
    private ArquivoEntidade arquivo;

    @Column(name = "carga_horaria", nullable = true)
    private float cargaHoraria;

    @Column(name = "comentario", nullable = true, length = 500)
    private String comentario;
}