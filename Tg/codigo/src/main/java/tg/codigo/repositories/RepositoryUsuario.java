package tg.codigo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tg.codigo.models.Usuarios;

@Repository
public interface RepositoryUsuario extends JpaRepository<Usuarios, Long> {
}
