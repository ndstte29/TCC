package tg.codigo.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tg.codigo.interfaces.IService;
import tg.codigo.models.Produtos;
import tg.codigo.models.Usuarios;
import tg.codigo.repositories.RepositoryProduto;
import tg.codigo.utils.PermissaoNegadaException;

@Service
public class ServiceProduto implements IService<Produtos, Long> {

    @Autowired
    private RepositoryProduto repositoryProduto;

    // Constantes de permissão
    private static final String PERMISSAO_ADMIN = "ADMIN";

    @Override
    public Produtos salvar(Produtos objeto) {
        validarProduto(objeto);
        return repositoryProduto.save(objeto);
    }

    // Método com controle de permissão - permite ADMIN e FUNCIONARIO
    public Produtos salvarComPermissao(Produtos produto, Usuarios solicitante) {
        verificarPermissaoBasica(solicitante); // Verifica se está autenticado
        return salvar(produto);
    }

    private void validarProduto(Produtos produto) {
        if (produto.getProEstoquemaximo() < produto.getProEstoqueminimo()) {
            throw new RuntimeException("O estoque máximo deve ser maior que o estoque mínimo.");
        }

        if (produto.getProEstoquemaximo() < produto.getProQuantidadeEstoque()) {
            throw new RuntimeException("A quantidade não pode ser maior que o estoque máximo.");
        }

        if (produto.getProVencimento() != null && produto.getProVencimento().isBefore(LocalDate.now())) {
            throw new RuntimeException("A data de vencimento não pode ser anterior à data atual.");
        }
    }

    public List<Produtos> listarTodos() {
        return repositoryProduto.findAll();
    }

    public List<Produtos> listarTodosComPermissao(Usuarios solicitante) {
        verificarPermissaoBasica(solicitante);
        return listarTodos();
    }

    public String verificarEstoqueBaixo() {
        List<Produtos> produtos = repositoryProduto.findAll();

        List<Produtos> produtosComEstoqueBaixo = produtos.stream()
                .filter(p -> p.getProQuantidadeEstoque() < p.getProEstoqueminimo())
                .collect(Collectors.toList());

        if (!produtosComEstoqueBaixo.isEmpty()) {
            StringBuilder mensagem = new StringBuilder("Os seguintes produtos estão com estoque baixo:<br>");
            produtosComEstoqueBaixo.forEach(p -> mensagem.append("- ")
                    .append(p.getProNome())
                    .append(" (Estoque: ")
                    .append(p.getProQuantidadeEstoque())
                    .append(", Mínimo: ")
                    .append(p.getProEstoqueminimo())
                    .append(")<br>"));

            return mensagem.toString();
        }
        return null;
    }

    @Override
    public Produtos localizar(Long id) {
        return repositoryProduto.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    // Método com controle de permissão - permite ADMIN e FUNCIONARIO
    public Produtos localizarComPermissao(Long id, Usuarios solicitante) {
        verificarPermissaoBasica(solicitante);
        return localizar(id);
    }

    @Override
    public void excluir(Produtos objeto) {
        try {
            repositoryProduto.delete(objeto);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Este produto não pode ser excluído pois existem registros vinculados a ele.");
        }
    }

    // Método com controle de permissão para exclusão (somente ADMIN)
    public void excluirComPermissao(Produtos produto, Usuarios solicitante) {
        verificarPermissaoAdmin(solicitante);
        excluir(produto);
    }

    @Override
    public Produtos Atualizar(Produtos objeto) {
        validarProduto(objeto);
        if (repositoryProduto.existsById(objeto.getProId())) {
            return repositoryProduto.save(objeto);
        }
        throw new RuntimeException("Produto não encontrado para atualização");
    }

    // Método com controle de permissão - permite ADMIN e FUNCIONARIO
    public Produtos atualizarComPermissao(Produtos produto, Usuarios solicitante) {
        verificarPermissaoBasica(solicitante);
        return Atualizar(produto);
    }

    // Métodos auxiliares para verificação de permissões
    private void verificarPermissaoAdmin(Usuarios usuario) {
        if (usuario == null || usuario.getUsuPermissao() == null ||
                !PERMISSAO_ADMIN.equals(usuario.getUsuPermissao())) {
            throw new PermissaoNegadaException("Acesso restrito a administradores");
        }
    }

    private void verificarPermissaoBasica(Usuarios usuario) {
        if (usuario == null || usuario.getUsuPermissao() == null) {
            throw new PermissaoNegadaException("Usuário não autenticado");
        }
    }
}