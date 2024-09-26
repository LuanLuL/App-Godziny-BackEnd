package com.cefet.godziny.api.atividade.arquivo;

import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/arquivo")
public interface  IArquivoApi {
    @GetMapping("/download/{fileId}")
    ResponseEntity<Resource> getArquivo(@PathVariable(value = "fileId") UUID id) throws Exception;
}
