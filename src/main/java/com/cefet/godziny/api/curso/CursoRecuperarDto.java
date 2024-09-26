package com.cefet.godziny.api.curso;

import java.util.UUID;
import com.cefet.godziny.api.usuario.UsuarioRecuperarDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CursoRecuperarDto{
    @NotNull
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
    @NotBlank
    private UsuarioRecuperarDto coordenador;
}
