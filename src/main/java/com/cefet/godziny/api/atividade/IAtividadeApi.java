package com.cefet.godziny.api.atividade;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/atividade")
public interface IAtividadeApi {
    @GetMapping("/{atividadeId}")
    ResponseEntity<AtividadeRecuperarDto> getAtividade(@PathVariable(value = "atividadeId") UUID id) throws Exception;

    @PostMapping("/pesquisar")
        ResponseEntity<Page<AtividadeRecuperarDto>> pesquisarAtividades(
            @PageableDefault(page = 0, size = 10, sort = "titulo", direction = Sort.Direction.ASC)
            Pageable pageable,
            @RequestBody @Valid AtividadeFiltroDto atividadeFiltroDto
        ) throws Exception;


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<UUID> createAtividade(@Valid @RequestPart("dto") AtividadeDto dto, @RequestPart("arquivo") MultipartFile arquivo) throws Exception;

    @PutMapping("/{atividadeId}")
    ResponseEntity<UUID> updateAtividade(@PathVariable(value = "atividadeId") UUID atividadeId, @RequestBody @Valid AtividadeAtualizarDto dto) throws Exception;

    @DeleteMapping("/{atividadeId}")
    ResponseEntity<Void> removeAtividade(@PathVariable(value = "atividadeId") UUID atividadeId) throws Exception;
}
