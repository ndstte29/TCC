package tg.codigo.services;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import tg.codigo.interfaces.IService;
import tg.codigo.models.Fornecedor;
import tg.codigo.models.Usuarios;
import tg.codigo.repositories.RepositoryFornecedor;
import tg.codigo.utils.CnpjValidator;
import tg.codigo.utils.PermissaoNegadaException;

@Service
public class ServiceFornecedor implements IService<Fornecedor, Long> {

    @Autowired
    private RepositoryFornecedor repositoryFornecedor;

    // Constantes de permissão simplificadas
    private static final String PERMISSAO_ADMIN = "ADMIN";

    public boolean validarCnpj(String cnpj) {
        return CnpjValidator.isCnpjValid(cnpj);
    }

    @Override
    public Fornecedor salvar(Fornecedor fornecedor) {
        if (!validarCnpj(fornecedor.getForCnpj())) {
            throw new RuntimeException("CNPJ inválido!");
        }
        
        Fornecedor fornecedorExistente = repositoryFornecedor.findByForCnpj(fornecedor.getForCnpj());

        if (fornecedorExistente != null && !fornecedorExistente.getForId().equals(fornecedor.getForId())) {
            throw new RuntimeException("Já existe um fornecedor cadastrado com este CNPJ.");
        }

        return repositoryFornecedor.save(fornecedor);
    }

    // Método com controle de permissão
    public Fornecedor salvarComPermissao(Fornecedor fornecedor, Usuarios solicitante) {
        verificarPermissao(solicitante, PERMISSAO_ADMIN);
        return salvar(fornecedor);
    }

    @Override
    public List<Fornecedor> listarTodos() {
        return repositoryFornecedor.findAll();
    }

    // Método com controle de permissão
    public List<Fornecedor> listarTodosComPermissao(Usuarios solicitante) {
        // Permite que funcionários listem fornecedores também
        verificarPermissaoBasica(solicitante);
        return listarTodos();
    }

    @Override
    public Fornecedor localizar(Long id) {
        return repositoryFornecedor.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
    }

    // Método com controle de permissão
    public Fornecedor localizarComPermissao(Long id, Usuarios solicitante) {
        // Permite que funcionários vejam detalhes de fornecedores
        verificarPermissaoBasica(solicitante);
        return localizar(id);
    }

    @Override
    public void excluir(Fornecedor fornecedor) {
        try {
            Objects.requireNonNull(fornecedor, "Fornecedor não pode ser nulo");
            Objects.requireNonNull(fornecedor.getForId(), "ID do fornecedor não pode ser nulo");
            
            if (!repositoryFornecedor.existsById(fornecedor.getForId())) {
                throw new EmptyResultDataAccessException("Fornecedor não encontrado", 1);
            }
            
            repositoryFornecedor.delete(fornecedor);
            
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                "Não é possível excluir o fornecedor pois existem registros vinculados a ele.", e);
        }
    }

    // Método com controle de permissão
    public void excluirComPermissao(Fornecedor fornecedor, Usuarios solicitante) {
        verificarPermissao(solicitante, PERMISSAO_ADMIN);
        excluir(fornecedor);
    }

    @Override
    public Fornecedor Atualizar(Fornecedor fornecedor) {
        if (!repositoryFornecedor.existsById(fornecedor.getForId())) {
            throw new RuntimeException("Fornecedor não encontrado para atualização");
        }

        Fornecedor fornecedorComMesmoCnpj = repositoryFornecedor.findByForCnpj(fornecedor.getForCnpj());
        if (fornecedorComMesmoCnpj != null && 
            !fornecedorComMesmoCnpj.getForId().equals(fornecedor.getForId())) {
            throw new RuntimeException("Já existe um fornecedor com este CNPJ");
        }

        return repositoryFornecedor.save(fornecedor);
    }

    // Método com controle de permissão
    public Fornecedor atualizarComPermissao(Fornecedor fornecedor, Usuarios solicitante) {
        verificarPermissao(solicitante, PERMISSAO_ADMIN);
        return Atualizar(fornecedor);
    }

    // Método auxiliar para verificação de permissões
    private void verificarPermissao(Usuarios usuario, String permissaoRequerida) {
        if (usuario == null || usuario.getUsuPermissao() == null) {
            throw new PermissaoNegadaException("Usuário não autenticado");
        }

        if (PERMISSAO_ADMIN.equals(permissaoRequerida) && !PERMISSAO_ADMIN.equals(usuario.getUsuPermissao())) {
            throw new PermissaoNegadaException("Acesso restrito a administradores");
        }
    }

    // Verifica apenas se o usuário está autenticado (qualquer permissão)
    private void verificarPermissaoBasica(Usuarios usuario) {
        if (usuario == null || usuario.getUsuPermissao() == null) {
            throw new PermissaoNegadaException("Usuário não autenticado");
        }
    }
}