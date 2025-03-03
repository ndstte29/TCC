package tg.codigo.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tg.codigo.interfaces.IService;
import tg.codigo.models.Venda;
import tg.codigo.repositories.RepositoryVenda;

@Service
public class ServiceVenda implements IService<Venda, Long> {

    @Autowired
    private RepositoryVenda repositoryVenda;

    @Override
    public Venda salvar(Venda objeto) {
        return repositoryVenda.save(objeto);
    }

    @Override
    public List<Venda> listarTodos() {
        return repositoryVenda.findAll();
    }

    @Override
    public Venda localizar(Long id) {
        return repositoryVenda.findById(id).orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }

    @Override
    public void excluir(Venda objeto) {
        try {
            repositoryVenda.delete(objeto);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Esta venda não pode ser excluída.");
        }
    }

    @Override
    public Venda Atualizar(Venda objeto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Atualizar'");
    }
}