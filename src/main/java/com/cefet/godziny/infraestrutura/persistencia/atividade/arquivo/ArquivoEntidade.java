package com.cefet.godziny.infraestrutura.persistencia.atividade.arquivo;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "arquivo")
@Table(name = "tb_arquivo")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArquivoEntidade implements Serializable {
    private static final long serialVersionUID  = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo;

    @Lob
    @Column(nullable = false)
    private byte[] dado;
}
