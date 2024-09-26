package com.cefet.godziny.api.curso;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/curso")
public interface ICursoApi {

    @GetMapping("/{cursoSigla}")
    ResponseEntity<CursoRecuperarDto> getCurso(@PathVariable(value = "cursoSigla") String cursoSigla) throws Exception;

    @PostMapping("/pesquisar")
    ResponseEntity<Page<CursoRecuperarDto>> pesquisarCursos(
        @PageableDefault(page = 0, size = 10, sort = "nome", direction = Sort.Direction.ASC)
        Pageable pageable,
        @RequestBody @Valid CursoFiltroDto cursoFiltroDto
    ) throws Exception;

    @PostMapping("")
    ResponseEntity<String> createCurso(@RequestBody @Valid CursoDto dto) throws Exception;

    @PutMapping("/{cursoSigla}")
    ResponseEntity<String> updateCurso(@PathVariable(value = "cursoSigla") String cursoSigla, @RequestBody @Valid CursoDto dto) throws Exception;

    @DeleteMapping("/{cursoSigla}")
    ResponseEntity<Void> removeCurso(@PathVariable(value = "cursoSigla") String cursoSigla) throws Exception;
}