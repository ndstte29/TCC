package tg.codigo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tg.codigo.interfaces.IService;
import tg.codigo.models.Usuarios;
import tg.codigo.repositories.RepositoryUsuario;

@Service
public class ServiceUsuario implements IService<Usuarios,Long>{

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    public List<Usuarios> listarTodos() {
        return repositoryUsuario.findAll(); 
    }

    @Override
    public Usuarios salvar(Usuarios objeto) {
        return repositoryUsuario.save(objeto);
    }

    @Override
    public Usuarios localizar(Long atributo) {
        return repositoryUsuario.findById(atributo).get();
    }

    @Override
    public void excluir(Usuarios objeto) {
        try {
            repositoryUsuario.delete(objeto);
        } catch (Exception e) {
            throw new RuntimeException("Este registro não pode ser excluido.");
        }
    }

    @Override
    public Usuarios Atualizar(Usuarios objeto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Atualizar'");
    }
}
