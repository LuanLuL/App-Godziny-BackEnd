package com.cefet.godziny.domain.casouso.categoria;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.cefet.godziny.infraestrutura.exceptions.categoria.RemoverCategoriaComAtividadesException;
import com.cefet.godziny.infraestrutura.persistencia.atividade.AtividadeRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
public class RemoverCategoriaCasoUso {
    @Autowired
    private final CategoriaRepositorioJpa categoriaRepositorioJpa;

    @Autowired
    private final AtividadeRepositorioJpa atividadeRepositorioJpa;

    @NotNull(message = "O ID da categoria é obrigatório")
    private UUID id;

    public void validarRemocao() throws Exception {
        CategoriaEntidade categoria = categoriaRepositorioJpa.findById(this.id);
        if(!(atividadeRepositorioJpa.findByCategoria(categoria).isEmpty())){
            throw new RemoverCategoriaComAtividadesException();
        }
    }

    public void removerCategoria() throws Exception {
        categoriaRepositorioJpa.deleteCategoria(this.id);
    }
}
