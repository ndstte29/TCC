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
        // Verifica se já existe um fornecedor com este CNPJ
        Fornecedor fornecedorExistente = repositoryFornecedor.findByForCnpj(objeto.getForCnpj());
        
        if (fornecedorExistente != null && !fornecedorExistente.getForId().equals(objeto.getForId())) {
            throw new RuntimeException("Já existe um fornecedor cadastrado com este CNPJ.");
        }
        
        return repositoryFornecedor.save(objeto);
    }

    @Override
    public List<Fornecedor> listarTodos() {
        return repositoryFornecedor.findAll();
    }

    @Override
    public Fornecedor localizar(Long id) {
        return repositoryFornecedor.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
    }

    @Override
    public void excluir(Fornecedor objeto) {
        try {
            repositoryFornecedor.delete(objeto);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Este fornecedor não pode ser excluído.");
        }
    }

    @Override
    public Fornecedor Atualizar(Fornecedor objeto) {
        // Verifica se o fornecedor existe antes de tentar atualizar
        Fornecedor fornecedorExistente = repositoryFornecedor.findById(objeto.getForId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        // Verifica se já existe um fornecedor com o mesmo CNPJ (mas que não seja o fornecedor atual)
        Fornecedor fornecedorComMesmoCnpj = repositoryFornecedor.findByForCnpj(objeto.getForCnpj());
        if (fornecedorComMesmoCnpj != null && !fornecedorComMesmoCnpj.getForId().equals(fornecedorExistente.getForId())) {
            throw new RuntimeException("Já existe um fornecedor cadastrado com este CNPJ.");
        }

        // Se passar nas validações, atualiza o fornecedor
        return repositoryFornecedor.save(objeto);
    }
}
