package com.cefet.godziny.api.usuario;

import io.micrometer.common.lang.Nullable;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
public class UsuarioFiltroDto{
    
    @Nullable
    private Integer matricula;
    
    @Nullable
    private String cursoSigla;
    
    @Nullable
    private String nome;
}
