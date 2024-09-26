package com.cefet.godziny.api.categoria;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
public class CategoriaDto {

    private UUID id;

    @NotNull
    @NotBlank
    private String cursoSigla;
    
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
