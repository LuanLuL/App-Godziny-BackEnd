package com.cefet.godziny.api.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RequestMapping("/auth")
public interface  IAuthApi {
    @PostMapping("/login")
    ResponseEntity<AuthResponseDto> loginUsuario(@RequestBody @Valid AuthDto dto) throws Exception;
}
