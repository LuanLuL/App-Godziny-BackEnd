package com.cefet.godziny.domain.porta.auth;

import com.cefet.godziny.api.auth.AuthDto;
import com.cefet.godziny.api.auth.AuthResponseDto;

public interface IAuthRepositorio {
    AuthResponseDto loginUsuario(AuthDto dto)throws Exception;
}
