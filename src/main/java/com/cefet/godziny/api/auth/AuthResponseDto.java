package com.cefet.godziny.api.auth;

import com.cefet.godziny.api.usuario.UsuarioRecuperarDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
public class AuthResponseDto{
    @NotNull
    @NotBlank
    private String token;

    @NotNull
    @NotBlank
    private UsuarioRecuperarDto usuario;
}