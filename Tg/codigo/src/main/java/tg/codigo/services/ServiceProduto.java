    package tg.codigo.services;

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
        return repositoryProduto.save(objeto);
    }

    public List<Produtos> listarTodos() {
        return repositoryProduto.findAll();
    }
    @Override
    public Produtos localizar(Long atributo) {
       
        return repositoryProduto.findById(atributo).get();
    }
    @Override
    public void excluir(Produtos objeto) {
       
         try{
            repositoryProduto.delete(objeto);
        }
        catch (DataIntegrityViolationException e){
            throw new RuntimeException("Este registro não pode ser excluido.");
        }
    }
}
