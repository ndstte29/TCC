package tg.codigo.services;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import tg.codigo.models.Venda;
import tg.codigo.repositories.RepositoryVenda;

@Service
public class ServiceIndicador {

    private RepositoryVenda repositoriyVenda;

    public ServiceIndicador(RepositoryVenda vendaRepository) {
        this.repositoriyVenda = vendaRepository;
    }

    public List<Venda> buscarPorData(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return repositoriyVenda.findByVenDataBetween(start, end);
        } else if (start != null) {
            return repositoriyVenda.findByVenDataAfter(start.minusDays(1));
        } else if (end != null) {
            return repositoriyVenda.findByVenDataBefore(end.plusDays(1));
        } else {
            return repositoriyVenda.findAll();
        }
    }
}
