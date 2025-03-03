package tg.codigo.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tg.codigo.interfaces.IService;
import tg.codigo.models.ProdutoVenda;
import tg.codigo.repositories.RepositoryProdutoVenda;

@Service
public class ServiceProdutoVenda implements IService<ProdutoVenda, Long> {

    @Autowired
    private RepositoryProdutoVenda repositoryProdutoVenda;

    @Override
    public ProdutoVenda salvar(ProdutoVenda objeto) {
        return repositoryProdutoVenda.save(objeto);
    }

    @Override
    public List<ProdutoVenda> listarTodos() {
        return repositoryProdutoVenda.findAll();
    }

    @Override
    public ProdutoVenda localizar(Long id) {
        return repositoryProdutoVenda.findById(id).orElseThrow(() -> new RuntimeException("Relação Produto-Venda não encontrada"));
    }

    @Override
    public void excluir(ProdutoVenda objeto) {
        try {
            repositoryProdutoVenda.delete(objeto);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Esta relação Produto-Venda não pode ser excluída.");
        }
    }

    @Override
    public ProdutoVenda Atualizar(ProdutoVenda objeto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Atualizar'");
    }
}