package com.cefet.godziny.domain.casouso.usuario;

import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import com.cefet.godziny.api.usuario.UsuarioDto;
import com.cefet.godziny.constantes.usuario.EnumRecursos;
import com.cefet.godziny.infraestrutura.exceptions.usuario.CriarUsuarioAdmComCursoException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.CriarUsuarioEmailRepetidoException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.CriarUsuarioIncompletoException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.CriarUsuarioNormalSemCursoException;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoEntidade;
import com.cefet.godziny.infraestrutura.persistencia.curso.CursoRepositorioJpa;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioEntidade;
import com.cefet.godziny.infraestrutura.persistencia.usuario.UsuarioRepositorioJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
    
@SpringBootTest
public class AtualizarUsuarioCasoUsoTest {
    private UsuarioDto usuarioDto;
    private CursoEntidade cursoEntidade;

    @Mock
    CursoRepositorioJpa cursoRepositorioJpa;

    @Mock
    UsuarioRepositorioJpa usuarioRepositorioJpa;

    private AtualizarUsuarioCasoUso atualizarUsuarioCasoUso;

    @BeforeEach
    void inicializarDados() {
        atualizarUsuarioCasoUso = new AtualizarUsuarioCasoUso(999999, "TESTE", "teste@teste.com.br", "teste123", "cursoSIGLA", LocalDateTime.now(), EnumRecursos.NORMAL,  usuarioRepositorioJpa, cursoRepositorioJpa);
    };

    @AfterEach
    void limparDados() {
        this.usuarioDto = null;
        this.cursoEntidade = null;
        usuarioRepositorioJpa.deleteAll();
        cursoRepositorioJpa.deleteAll();
    }
    
    @Test
    @DisplayName("Should valided an AtualizarUsuarioCasoUso ADM successfully")
    void testeAtualizarUsuarioCasoUsoSuccess() throws Exception {
        this.usuarioDto = createUsuarioDto();
        this.cursoEntidade = createCursoEntidade();
        this.atualizarUsuarioCasoUso.setTipo(EnumRecursos.ADM);
        this.atualizarUsuarioCasoUso.setCursoSigla("");

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(cursoEntidade);
        when(usuarioRepositorioJpa.updateUsuario(Mockito.any(UsuarioEntidade.class))).thenReturn(99999);
        when(usuarioRepositorioJpa.findByEmail(Mockito.anyString())).thenReturn(null);
        atualizarUsuarioCasoUso.validarAtualizacao();
        Integer response = atualizarUsuarioCasoUso.AtualizarUsuario(usuarioDto);

        assertThat(response).isInstanceOf(Integer.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Should valided an AtualizarUsuarioCasoUso NORMAL successfully")
    void testeAtualizarUsuarioCasoUsoSuccess2() throws Exception {
        this.usuarioDto = createUsuarioDto();
        this.cursoEntidade = createCursoEntidade();

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(cursoEntidade);
        when(usuarioRepositorioJpa.updateUsuario(Mockito.any(UsuarioEntidade.class))).thenReturn(99999);
        when(usuarioRepositorioJpa.findByEmail(Mockito.anyString())).thenReturn(null);
        atualizarUsuarioCasoUso.validarAtualizacao();
        Integer response = atualizarUsuarioCasoUso.AtualizarUsuario(usuarioDto);

        assertThat(response).isInstanceOf(Integer.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to update an Usuario successfully because is updating himself")
    void testeAtualizarUsuarioCasoUsoSuccessCase2() throws Exception {
        this.usuarioDto = createUsuarioDto();
        this.cursoEntidade = createCursoEntidade();
        UsuarioEntidade usuarioEntidade = new UsuarioEntidade(
            999999, 
            createCursoEntidade(),
            "TESTE",
            "teste@teste.com.br",
            "teste123",
            EnumRecursos.NORMAL,
            LocalDateTime.now()
        );

        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(cursoEntidade);
        when(usuarioRepositorioJpa.updateUsuario(Mockito.any(UsuarioEntidade.class))).thenReturn(99999);
        when(usuarioRepositorioJpa.findByEmail(Mockito.anyString())).thenReturn(usuarioEntidade);
        this.atualizarUsuarioCasoUso.validarAtualizacao();
        Integer response = atualizarUsuarioCasoUso.AtualizarUsuario(usuarioDto);

        assertThat(response).isInstanceOf(Integer.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to update an Usuario and return an excepiton because the NOME is too big")
    void testAtualizarUsuarioCasoUsoExceptionCase1() throws Exception{
        this.atualizarUsuarioCasoUso.setNome("NOME's bigger than 100 letters are not allowed. It's really difficuty someone has a name's length as one hundred letters. But the system stiil has that rule.");

        Exception thrown = assertThrows( CriarUsuarioIncompletoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O nome do usuário deve ter entre 3 e 100 caracteres");
    }

    @Test
    @DisplayName("Try to update an Usuario and return an exception because the NOME is too short")
    void testeAtualizarUsuarioCasoUsoExceptionCase2() {
        this.atualizarUsuarioCasoUso.setNome("N");
        
        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O nome do usuário deve ter entre 3 e 100 caracteres");
    }  

    @Test
    @DisplayName("Try to update an Usuario and return an excepiton because the SENHA is too short")
    void testeAtualizarUsuarioCasoUsoExceptionCase3() throws Exception{
        this.atualizarUsuarioCasoUso.setSenha("123");

        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A senha deve ter no mínimo 6 caracteres");
    }

    @Test
    @DisplayName("Try to update an Usuario and return an exception because the EMAIL is NULL")
    void testeAtualizarUsuarioCasoUsoExceptionCase4() {
        this.atualizarUsuarioCasoUso.setEmail(null);
        
        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O email fornecido para o usuário não é válido");
    }  

    @Test
    @DisplayName("Try to update an Usuario and return an exception because the EMAIL isn't right")
    void testeAtualizarUsuarioCasoUsoExceptionCase5() {
        this.atualizarUsuarioCasoUso.setEmail("testehotmail.com");
        
        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O email fornecido para o usuário não é válido");
    }

    @Test
    @DisplayName("Try to update an Usuario and return an exception because the EMAIL is already in the database") 
    void testeAtualizarUsuarioCasoUsoExceptionCase6() throws Exception {
        UsuarioEntidade usuarioEntidade = new UsuarioEntidade(
            888888, 
            createCursoEntidade(),
            "TESTE",
            "teste@teste.com.br",
            "teste123",
            EnumRecursos.NORMAL,
            LocalDateTime.now()
        );

        when(usuarioRepositorioJpa.findByEmail(Mockito.anyString())).thenReturn(usuarioEntidade);
        
        Exception thrown = assertThrows(CriarUsuarioEmailRepetidoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O email fornecido já está cadastrado no sistema");
    }

    @Test
    @DisplayName("Try to update an Usuario and return an excepiton because the DATA is future")
    void testAtualizarUsuarioCasoUsoExceptionCase7() throws Exception{
        this.atualizarUsuarioCasoUso.setCreatedAt(LocalDateTime.now().plusMonths(1));

        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A data de criação do usuário deve ser menor ou igual à data e hora atuais");
    }

    @Test
    @DisplayName("Try to update an Usuario and return an excepiton because an 'NORMAL' user must have a Curso")
    void testAtualizarUsuarioCasoUsoExceptionCase8() throws Exception{
        this.atualizarUsuarioCasoUso.setCursoSigla("");

        Exception thrown = assertThrows(CriarUsuarioNormalSemCursoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Usuários do tipo 'NORMAL' devem ser associados a um curso");
    }

    
    @Test
    @DisplayName("Try to update an Usuario and return an excepiton because an 'ADM' user mustn't have a Curso")
    void testAtualizarUsuarioCasoUsoExceptionCase9() throws Exception{
        this.atualizarUsuarioCasoUso.setTipo(EnumRecursos.ADM);

        Exception thrown = assertThrows(CriarUsuarioAdmComCursoException.class, () -> {
            this.atualizarUsuarioCasoUso.validarAtualizacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Usuários do tipo 'ADM' não podem ser associados a um curso");
    }

    private UsuarioDto createUsuarioDto(){
        UsuarioDto dto = new UsuarioDto(
            999999, 
            "CURSO_ID_TESTE",
            "TESTE",
            "teste@teste.com.br",
            "teste123",
            EnumRecursos.NORMAL,
            LocalDateTime.now()
        );
        return dto;
    }

    private CursoEntidade createCursoEntidade(){
        CursoEntidade curso = new CursoEntidade(
            UUID.randomUUID(),
            "CURSO_ID_TESTE",
            "TESTE",
            100,
            new UsuarioEntidade(99999, null, "nome TESTE", "teste@test.com", "senha TESTE", EnumRecursos.ADM, LocalDateTime.now())
        );
        return curso;
    }

}