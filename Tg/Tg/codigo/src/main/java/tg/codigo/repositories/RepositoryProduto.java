package tg.codigo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tg.codigo.models.Produtos;

@Repository
public interface RepositoryProduto extends JpaRepository<Produtos,Long> {
  
} 
