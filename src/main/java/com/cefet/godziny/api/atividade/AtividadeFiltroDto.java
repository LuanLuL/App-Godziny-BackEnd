package com.cefet.godziny.api.atividade;

import com.cefet.godziny.constantes.atividade.EnumStatus;
import io.micrometer.common.lang.Nullable;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
public class AtividadeFiltroDto {

    @Nullable
    private String usuarioNome;
    
    @Nullable
    private String titulo;

    @Nullable
    private EnumStatus status;

    @Nullable
    private String categoria;
}
