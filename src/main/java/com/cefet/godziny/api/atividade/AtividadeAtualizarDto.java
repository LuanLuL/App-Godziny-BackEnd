package com.cefet.godziny.api.atividade;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import com.cefet.godziny.constantes.atividade.EnumStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
public class AtividadeAtualizarDto{

    @NotNull
    private UUID id;

    @NotNull
    @Positive
    private Integer usuarioId;

    @NotNull
    private UUID categoriaId;

    @NotNull
    @NotBlank
    private String titulo;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @NotNull
    private EnumStatus status;

    @NotNull
    private UUID arquivoId;

    @NotNull
    @PositiveOrZero
    private float cargaHoraria;

    @NotNull
    @NotBlank
    private String comentario;
}


