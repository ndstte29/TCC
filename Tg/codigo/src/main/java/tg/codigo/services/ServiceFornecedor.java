package tg.codigo.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tg.codigo.interfaces.IService;
import tg.codigo.models.Fornecedor;
import tg.codigo.repositories.RepositoryFornecedor;

@Service
public class ServiceFornecedor implements IService<Fornecedor, Long> {

    @Autowired
    private RepositoryFornecedor repositoryFornecedor;

    @Override
    public Fornecedor salvar(Fornecedor objeto) {
        return repositoryFornecedor.save(objeto);
    }

    @Override
    public List<Fornecedor> listarTodos() {
        return repositoryFornecedor.findAll();
    }

    @Override
    public Fornecedor localizar(Long cnpj) {
        return repositoryFornecedor.findById(cnpj).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
    }

    @Override
    public void excluir(Fornecedor objeto) {
        try {
            repositoryFornecedor.delete(objeto);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Este fornecedor não pode ser excluído.");
        }
    }
}