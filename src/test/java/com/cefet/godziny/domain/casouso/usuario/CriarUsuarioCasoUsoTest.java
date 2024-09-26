package com.cefet.godziny.domain.casouso.usuario;

import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
public class CriarUsuarioCasoUsoTest {
    private UsuarioDto usuarioDto;
    private CursoEntidade cursoEntidade;

    @Mock
    CursoRepositorioJpa cursoRepositorioJpa;

    @Mock
    UsuarioRepositorioJpa usuarioRepositorioJpa;

    @InjectMocks
    private CriarUsuarioCasoUso criarUsuarioCasoUso;

    @BeforeEach
    void inicializarDados() {
        criarUsuarioCasoUso = new CriarUsuarioCasoUso("TESTE", "teste@teste.com.br", "teste123", "cursoSIGLA", LocalDateTime.now(), EnumRecursos.NORMAL,  usuarioRepositorioJpa, cursoRepositorioJpa);
    };

    @AfterEach
    void limparDados() {
        this.usuarioDto = null;
        this.cursoEntidade = null;
        usuarioRepositorioJpa.deleteAll();
        cursoRepositorioJpa.deleteAll();
    }
    
    @Test
    @DisplayName("Should valided an CriarUsuarioCasoUso ADM successfully")
    void testeCriarUsuarioCasoUsoSuccess() throws Exception {
        this.usuarioDto = createUsuarioDto();
        this.cursoEntidade = createCursoEntidade();
        this.criarUsuarioCasoUso.setTipo(EnumRecursos.ADM);
        this.criarUsuarioCasoUso.setCursoSigla("");
        
        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(cursoEntidade);
        when(usuarioRepositorioJpa.createUsuario(Mockito.any(UsuarioEntidade.class))).thenReturn(99999);
        when(usuarioRepositorioJpa.findByEmailOptional(Mockito.anyString())).thenReturn(Optional.empty());
        this.criarUsuarioCasoUso.validarCriacao();
        Integer response = criarUsuarioCasoUso.createUsuario(usuarioDto);

        assertThat(response).isInstanceOf(Integer.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Should valided an CriarUsuarioCasoUso NORMAL successfully")
    void testeCriarUsuarioCasoUsoSuccess2() throws Exception {
        this.usuarioDto = createUsuarioDto();
        this.cursoEntidade = createCursoEntidade();
        
        when(cursoRepositorioJpa.findBySigla(Mockito.anyString())).thenReturn(cursoEntidade);
        when(usuarioRepositorioJpa.createUsuario(Mockito.any(UsuarioEntidade.class))).thenReturn(99999);
        when(usuarioRepositorioJpa.findByEmailOptional(Mockito.anyString())).thenReturn(Optional.empty());
        this.criarUsuarioCasoUso.validarCriacao();
        Integer response = criarUsuarioCasoUso.createUsuario(usuarioDto);

        assertThat(response).isInstanceOf(Integer.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Try to create an Usuario and return an excepiton because the NOME is too big")
    void testCriarUsuarioCasoUsoExceptionCase1() throws Exception{
        this.criarUsuarioCasoUso.setNome("NOME's bigger than 100 letters are not allowed. It's really difficuty someone has a name's length as one hundred letters. But the system stiil has that rule.");

        Exception thrown = assertThrows( CriarUsuarioIncompletoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O nome do usuário deve ter entre 3 e 100 caracteres");
    }

    @Test
    @DisplayName("Try to update an Usuario and return an exception because the NOME is too short")
    void testeCriarUsuarioCasoUsoExceptionCase2() {
        this.criarUsuarioCasoUso.setNome("N");
        
        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O nome do usuário deve ter entre 3 e 100 caracteres");
    }  

    @Test
    @DisplayName("Try to create an Usuario and return an excepiton because the SENHA is too short")
    void testeCriarUsuarioCasoUsoExceptionCase3() throws Exception{
        this.criarUsuarioCasoUso.setSenha("123");

        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A senha deve ter no mínimo 6 caracteres");
    }

    @Test
    @DisplayName("Try to create an Usuario and return an exception because the EMAIL is NULL")
    void testeCriarUsuarioCasoUsoExceptionCase4() {
        this.criarUsuarioCasoUso.setEmail(null);
        
        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O email fornecido para o usuário não é válido");
    }  

    @Test
    @DisplayName("Try to create an Usuario and return an exception because the EMAIL isn't right")
    void testeCriarUsuarioCasoUsoExceptionCase5() {
        this.criarUsuarioCasoUso.setEmail("testehotmail.com");
        
        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O email fornecido para o usuário não é válido");
    }

    @Test
    @DisplayName("Try to create an Usuario and return an exception because the EMAIL is already in the database")
    void testeCriarUsuarioCasoUsoExceptionCase6() throws Exception {
        Optional<UsuarioEntidade> usuarioEntidade = Optional.of(
            new UsuarioEntidade(
                999999, 
                createCursoEntidade(),
                "TESTE",
                "teste@teste.com.br",
                "teste123",
                EnumRecursos.NORMAL,
                LocalDateTime.now()
            )
        );

        when(usuarioRepositorioJpa.findByEmailOptional(Mockito.anyString())).thenReturn(usuarioEntidade);
        
        Exception thrown = assertThrows(CriarUsuarioEmailRepetidoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("O email fornecido já está cadastrado no sistema");
    }

    @Test
    @DisplayName("Try to create an Usuario and return an excepiton because the DATA is future")
    void testCriarUsuarioCasoUsoExceptionCase7() throws Exception{
        this.criarUsuarioCasoUso.setCreatedAt(LocalDateTime.now().plusMonths(1));

        Exception thrown = assertThrows(CriarUsuarioIncompletoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("A data de criação do usuário deve ser menor ou igual à data e hora atuais");
    }

    @Test
    @DisplayName("Try to create an Usuario and return an excepiton because an 'NORMAL' user must have a Curso")
    void testCriarUsuarioCasoUsoExceptionCase8() throws Exception{
        this.criarUsuarioCasoUso.setCursoSigla("");

        Exception thrown = assertThrows(CriarUsuarioNormalSemCursoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
        });
        
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Usuários do tipo 'NORMAL' devem ser associados a um curso");
    }

    @Test
    @DisplayName("Try to create an Usuario and return an excepiton because an 'ADM' user mustn't have a Curso")
    void testCriarUsuarioCasoUsoExceptionCase9() throws Exception{
        this.criarUsuarioCasoUso.setTipo(EnumRecursos.ADM);

        Exception thrown = assertThrows(CriarUsuarioAdmComCursoException.class, () -> {
            this.criarUsuarioCasoUso.validarCriacao();
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