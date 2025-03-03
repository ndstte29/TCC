package tg.codigo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tg.codigo.models.Fornecedor;

@Repository
public interface RepositoryFornecedor extends JpaRepository<Fornecedor, Long> {
    Fornecedor findByForCnpj(String forCnpj); 
}
