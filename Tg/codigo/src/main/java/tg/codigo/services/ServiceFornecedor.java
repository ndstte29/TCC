package tg.codigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import tg.codigo.interfaces.IService;
import tg.codigo.models.Fornecedor;
import tg.codigo.repositories.RepositoryFornecedor;

@Service
public class ServiceFornecedor implements IService<Fornecedor,Long>{

    @Autowired
    private RepositoryFornecedor rfor;

    @Override
    public Fornecedor salvar(Fornecedor objeto) {
        return rfor.save(objeto);
    }

    public List<Fornecedor> listarTodos() {
        return rfor.findAll();
    }
    @Override
    public void excluir(Fornecedor objeto) {
       
         try{
            rfor.delete(objeto);
        }
        catch (DataIntegrityViolationException e){
            throw new RuntimeException("Este registro não pode ser excluido.");
        }
    }
    @Override
    public Fornecedor localizar(Long atributo) {
       
        return rfor.findById(atributo).get();
    }
    
}
