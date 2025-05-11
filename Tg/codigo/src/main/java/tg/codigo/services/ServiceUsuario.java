package tg.codigo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tg.codigo.interfaces.IService;
import tg.codigo.models.Usuarios;
import tg.codigo.repositories.RepositoryUsuario;

@Service
public class ServiceUsuario implements IService<Usuarios, Long> {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    public List<Usuarios> listarTodos() {
        return repositoryUsuario.findAll();
    }

    @Override
    public Usuarios salvar(Usuarios objeto) {
        return repositoryUsuario.save(objeto);
    }

    @Override
    public Usuarios localizar(Long atributo) {
        return repositoryUsuario.findById(atributo).get();
    }

    @Override
    public void excluir(Usuarios objeto) {
        try {
            repositoryUsuario.delete(objeto);
        } catch (Exception e) {
            throw new RuntimeException("Este registro não pode ser excluido.");
        }
    }

    @Override
    public Usuarios Atualizar(Usuarios objeto) {
        throw new UnsupportedOperationException("Unimplemented method 'Atualizar'");
    }

    public Usuarios buscarPorLoginESenha(String login, String senha) {
        return repositoryUsuario.findByUsuLoginAndUsuSenha(login, senha);
    }

    public void criarTokenRedefinicao(String email) {
        // Busca por usuLogin que é o e-mail no seu sistema
        Usuarios usuario = repositoryUsuario.findByUsuLogin(email);
        
        if (usuario == null) {
            throw new RuntimeException("E-mail não cadastrado no sistema");
        }
        
        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setTokenExpiration(LocalDateTime.now().plusHours(24));
        repositoryUsuario.save(usuario);
        
        // Implemente o envio de e-mail aqui (ou imprima no console para testes)
        System.out.println("Token para " + email + ": " + token);
        System.out.println("Link: http://seusite.com/usuarios/redefinir-senha?token=" + token);
    }

    public boolean validarToken(String token) {
        Usuarios usuario = repositoryUsuario.findByResetToken(token);
        return usuario != null &&
                usuario.getTokenExpiration().isAfter(LocalDateTime.now());
    }

    public void atualizarSenha(String token, String novaSenha) {
        Usuarios usuario = repositoryUsuario.findByResetToken(token);
        if (usuario == null || usuario.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado");
        }

        usuario.setUsuSenha(novaSenha);
        usuario.setResetToken(null);
        usuario.setTokenExpiration(null);
        repositoryUsuario.save(usuario);
    }
}