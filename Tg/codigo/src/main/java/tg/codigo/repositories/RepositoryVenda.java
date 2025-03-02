package tg.codigo.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tg.codigo.models.Venda;

@Repository
public interface RepositoryVenda extends JpaRepository<Venda, Long> {
    
}
