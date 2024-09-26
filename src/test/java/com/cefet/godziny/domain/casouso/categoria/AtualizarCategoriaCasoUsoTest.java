package com.cefet.godziny.domain.casouso.categoria;

import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import com.cefet.godziny.api.categoria.CategoriaDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.exceptions.CampoRepetidoNoBancoException;
import com.cefet.godziny.infraestrutura.exceptions.categoria.CriarCategoriaIncompletaException;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaEntidade;
import com.cefet.godziny.infraestrutura.persistencia.categoria.CategoriaRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AtualizarCategoriaCasoUsoTest {
    private CategoriaDto categoriaDto;
    private CategoriaEntidade categoriaEntidade;
    private CursoEntidade cursoEntidade;

    @Mock
    CursoRepositorioJpa cursoRepositorioJpa;

    @Mock
    CategoriaRepositorioJpa categoriaRepositorioJpa;

    private AtualizarCategoriaCasoUso atualizarCategoriaCasoUso;

    @BeforeEach
    void inicializarDados() {
        atualizarCategoriaCasoUso = new AtualizarCategoriaCasoUso(
            categoriaRepositorioJpa, cursoRepositorioJpa,
            UUID.randomUUID(),
            "sigla_TESTE", "nome_TESTE",
            (float) 1.0, (float) 1.0,
            "descrição_TESTE"
        );
    };

    @AfterEach
    void limparDados() {
        this.categoriaDto = null;
        this.categoriaEntidade = null;
        this.cursoEntidade = null;
        categoriaRepositorioJpa.deleteAll();
        cursoRepositorioJpa.deleteAll();
    }
    
    @Test
    @DisplayName("Should valided an AtualizarCategoriaCasoUso successfully cause it's a new Categoria")
    void testeAtualizarCategoriaCasoUsoSuccess() throws Exception {
        this.categoriaDto = createCategoriaDto();
        this.categoriaEntidade = createCategoriaEntidade();
        this.cursoEntidade = createCursoEntidade();
        List<CategoriaEntidade> list = List.of();

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(this.cursoEntidade);
        when(categoriaRepositorioJpa.findByCursoAndNome(Mockito.any(CursoEntidade.class), Mockito.anyString())).thenReturn(list);
        when(categoriaRepositorioJpa.updateCategoria(Mockito.any(CategoriaEntidade.class))).thenReturn(UUID.randomUUID());
        atualizarCategoriaCasoUso.validarAtualizacao();
        UUID response = atualizarCategoriaCasoUso.atualizarCategoria(this.categoriaDto, this.cursoEntidade);

        assertThat(response).isInstanceOf(UUID.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to update a Categoria successfully because is updating itself")
    void testeAtualizarCategoriaCasoUsoSuccessCase2() throws Exception{
        this.categoriaEntidade = createCategoriaEntidade();
        this.cursoEntidade = createCursoEntidade();
        this.categoriaDto = createCategoriaDto();
        List<CategoriaEntidade> list = new ArrayList<>();
        list.add(categoriaEntidade);
        atualizarCategoriaCasoUso.setCategoriaId(categoriaEntidade.getId());

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(this.cursoEntidade);
        when(categoriaRepositorioJpa.updateCategoria(Mockito.any(CategoriaEntidade.class))).thenReturn(UUID.randomUUID());
        when(categoriaRepositorioJpa.findByCursoAndNome(Mockito.any(CursoEntidade.class), Mockito.anyString())).thenReturn(list);
        atualizarCategoriaCasoUso.validarAtualizacao();
        UUID response = atualizarCategoriaCasoUso.atualizarCategoria(this.categoriaDto, this.cursoEntidade);

        assertThat(response).isInstanceOf(UUID.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the CURSOSIGLA is too big")
    void testAtualizarCategoriaCasoUsoExceptionCase1() throws Exception{
        atualizarCategoriaCasoUso.setCursoSigla("SIGLAS bigger than 20 letters are not allowed");

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A sigla do curso na categoria deve ter entre 3 e 20 caracteres");
    }

    @Test
    @DisplayName("Try to update Categoria and return an excepiton because the CURSOSIGLA is too short")
    void testAtualizarCategoriaCasoUsoExceptionCase2() throws Exception{
        atualizarCategoriaCasoUso.setCursoSigla("S");

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A sigla do curso na categoria deve ter entre 3 e 20 caracteres");
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the NOME is too big")
    void testAtualizarCategoriaCasoUsoExceptionCase3() throws Exception{
        atualizarCategoriaCasoUso.setNome("That NOME is too big! The Godziny's rules doesn't let this happen because it's not allowed and Who do want to put a long NOME like this? That NOME is too big! The Godziny's rules doesn't let this happen because it's not allowed and Who do want to put a long NOME like this?");

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O nome da categoria deve ter entre 3 e 250 caracteres");
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the NOME is too short")
    void testAtualizarCategoriaCasoUsoExceptionCase4() throws Exception{
        atualizarCategoriaCasoUso.setNome("N");

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O nome da categoria deve ter entre 3 e 250 caracteres");
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the PORCENTAGEM HORAS MAXIMAS is lesser than 0")
    void testAtualizarCategoriaCasoUsoExceptionCase5() throws Exception{
        atualizarCategoriaCasoUso.setPorcentagemHorasMaximas((float) -5.5);

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A porcentagem de horas máximas da categoria deve ser maior que zero");
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the PORCENTAGEM HORAS MAXIMAS is equal to 0")
    void testAtualizarCategoriaCasoUsoExceptionCase6() throws Exception{
        atualizarCategoriaCasoUso.setPorcentagemHorasMaximas((float) 0.0);

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A porcentagem de horas máximas da categoria deve ser maior que zero");
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the MULTIPLICADOR HORAS is lesser than 0")
    void testAtualizarCategoriaCasoUsoExceptionCase7() throws Exception{
        atualizarCategoriaCasoUso.setHorasMultiplicador((float) -5.5);

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O multiplicador por horas da categoria deve ser maior que zero");
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the MULTIPLICADOR HORAS is equal to 0")
    void testAtualizarCategoriaCasoUsoExceptionCase8() throws Exception{
        atualizarCategoriaCasoUso.setHorasMultiplicador((float) 0.0);

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O multiplicador por horas da categoria deve ser maior que zero");
    }


    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the DESCRICAO is too big")
    void testAtualizarCategoriaCasoUsoExceptionCase9() throws Exception{
        atualizarCategoriaCasoUso.setDescricao(
            "Localizado no coração da cidade, nosso hotel combina conforto moderno com charme histórico. Quartos espaçosos e elegantemente decorados oferecem vistas deslumbrantes da paisagem urbana. Desfrute de refeições requintadas em nosso restaurante premiado, seguido de relaxamento completo em nosso spa de classe mundial. Para reuniões e eventos, temos instalações versáteis e tecnologicamente avançadas. Nossa equipe dedicada está pronta para garantir que sua estadia seja memorável e livre de preocupações. Descubra uma experiência única no nosso hotel."
        );

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A descrição da categoria deve ter entre 10 e 500 caracteres");
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because the DESCRICAO is too short")
    void testAtualizarCategoriaCasoUsoExceptionCase10() throws Exception{
        atualizarCategoriaCasoUso.setDescricao("D");

        Exception thrown = assertThrows(CriarCategoriaIncompletaException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A descrição da categoria deve ter entre 10 e 500 caracteres");
    }

    @Test
    @DisplayName("Try to update a Categoria and return an excepiton because already exists one with the same CURSO and NOME")
    void testAtualizarCategoriaCasoUsoExceptionCase11() throws Exception{
        this.categoriaEntidade = createCategoriaEntidade();
        this.cursoEntidade = createCursoEntidade();
        List<CategoriaEntidade> list = new ArrayList<>();
        list.add(categoriaEntidade);

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(this.cursoEntidade);
        when(categoriaRepositorioJpa.findByCursoAndNome(Mockito.any(CursoEntidade.class), Mockito.anyString())).thenReturn(list);
        Exception thrown = assertThrows(CampoRepetidoNoBancoException.class, () -> {
            atualizarCategoriaCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Já existe uma categoria com este nome no curso selecionado");
    }

    private CategoriaDto createCategoriaDto(){
        return new CategoriaDto(
            UUID.randomUUID(), 
            "sigla_TESTE",
            "nome_TESTE",
            (float) 1.0, (float) 1.0,
            "descrição_TESTE"
        );
    }

    private CursoEntidade createCursoEntidade(){
        return new CursoEntidade(
            UUID.randomUUID(),
            "ODONT_DIV",
            "Odontologia",
            1,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
    }

    private CategoriaEntidade createCategoriaEntidade(){
        return new CategoriaEntidade(
            UUID.randomUUID(),
            createCursoEntidade(), "Categoria_TESTE",
            (float) 1.0, (float) 1.0,
            "Descrição_TESTE");
    }
}