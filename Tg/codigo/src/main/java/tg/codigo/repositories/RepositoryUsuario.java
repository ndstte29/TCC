package tg.codigo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tg.codigo.models.Usuarios;

@Repository
public interface RepositoryUsuario extends JpaRepository<Usuarios, Long> {
    // Método para recuperação por e-mail
    Usuarios findByUsuEmail(String email);
    
    // Método para recuperação por token
    Usuarios findByResetToken(String token);
    
    // Métodos adicionais (se necessário)
    Usuarios findByUsuLoginAndUsuSenha(String email, String senha);
}
