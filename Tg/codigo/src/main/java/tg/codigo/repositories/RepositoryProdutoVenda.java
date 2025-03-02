package tg.codigo.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tg.codigo.models.ProdutoVenda;

@Repository
public interface RepositoryProdutoVenda extends JpaRepository<ProdutoVenda, Long> {
    
}
