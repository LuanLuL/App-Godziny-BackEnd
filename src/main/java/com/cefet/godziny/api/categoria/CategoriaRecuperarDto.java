package com.cefet.godziny.api.categoria;

import java.util.UUID;
import com.cefet.godziny.api.curso.CursoDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriaRecuperarDto {

    @NotNull
    private UUID id;
    
    @NotNull
    private CursoDto curso;
    
    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    private float porcentagemHorasMaximas;

    @NotNull
    private float horasMultiplicador;

    @NotNull
    @NotBlank
    private String descricao;
}
