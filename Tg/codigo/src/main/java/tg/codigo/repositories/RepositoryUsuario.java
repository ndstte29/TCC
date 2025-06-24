package tg.codigo.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.codigo.models.Usuarios;

@Repository
public interface RepositoryUsuario extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByUsuEmail(String usuEmail);
    Usuarios findByUsuCpf(String usuCpf);
}
