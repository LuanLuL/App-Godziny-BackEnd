package com.cefet.godziny.api.curso;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
public class CursoDto{
    private UUID id;
    
    @NotNull
    @NotBlank
    private String sigla;

    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    private Integer carga_horaria_complementar;

    @NotNull
    @Positive
    private Integer coordenador_matricula;
}
