package tg.codigo.repositories;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.codigo.models.Venda;

@Repository
public interface RepositoryVenda extends JpaRepository<Venda, Long> {
    List<Venda> findByVenDataBetween(LocalDate startDate, LocalDate endDate);
    List<Venda> findByVenDataAfter(LocalDate startDate);
    List<Venda> findByVenDataBefore(LocalDate endDate);
}
