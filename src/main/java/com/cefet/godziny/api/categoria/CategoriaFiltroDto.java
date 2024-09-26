package com.cefet.godziny.api.categoria;

import io.micrometer.common.lang.Nullable;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
public class CategoriaFiltroDto {

    @Nullable
    private String cursoSigla;
    
    @Nullable
    private String nome;
}
