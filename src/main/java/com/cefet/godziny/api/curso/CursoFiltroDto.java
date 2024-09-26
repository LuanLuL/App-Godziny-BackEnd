package com.cefet.godziny.api.curso;

import io.micrometer.common.lang.Nullable;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
public class CursoFiltroDto{
    
    @Nullable
    private String sigla;
    
    @Nullable
    private String nome;
}
