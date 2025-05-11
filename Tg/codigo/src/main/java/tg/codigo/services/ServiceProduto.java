package tg.codigo.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import tg.codigo.interfaces.IService;
import tg.codigo.models.Produtos;
import tg.codigo.repositories.RepositoryProduto;

@Service
public class ServiceProduto implements IService<Produtos, Long> {

    @Autowired
    private RepositoryProduto repositoryProduto;

    @Override
    public Produtos salvar(Produtos objeto) {
        validarProduto(objeto);
        return repositoryProduto.save(objeto);
    }

    private void validarProduto(Produtos produto) {
        if (produto.getProEstoquemaximo() < produto.getProEstoqueminimo()) {
            throw new RuntimeException("O estoque máximo deve ser maior que o estoque mínimo.");
        }

        if(produto.getProEstoquemaximo() < produto.getProQuantidadeEstoque()){
            throw new RuntimeException("A quantidade não pode ser maior que o estoque maximo.");
        }

        if (produto.getProVencimento() != null && produto.getProVencimento().isBefore(LocalDate.now())) {
            throw new RuntimeException("A data de vencimento não pode ser anterior à data atual.");
        }
    }

    public List<Produtos> listarTodos() {
        return repositoryProduto.findAll();
    }

    @Override
    public Produtos localizar(Long id) {
        return repositoryProduto.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    @Override
    public void excluir(Produtos objeto) {
        try {
            repositoryProduto.delete(objeto);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Este produto não pode ser excluído.");
        }
    }

    @Override
    public Produtos Atualizar(Produtos objeto) {
        throw new UnsupportedOperationException("Unimplemented method 'Atualizar'");
    }
}
