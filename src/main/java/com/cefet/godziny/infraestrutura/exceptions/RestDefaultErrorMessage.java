package com.cefet.godziny.infraestrutura.exceptions;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RestDefaultErrorMessage {
    private HttpStatus status;
    private String detail;
}
