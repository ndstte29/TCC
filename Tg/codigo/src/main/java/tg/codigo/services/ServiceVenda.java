package tg.codigo.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tg.codigo.interfaces.IService;
import tg.codigo.models.Produtos;
import tg.codigo.models.Venda;
import tg.codigo.repositories.RepositoryVenda;

@Service
public class ServiceVenda implements IService<Venda, Long> {

    @Autowired
    private RepositoryVenda repositoryVenda;

    @Autowired
    private ServiceProduto serviceProduto;

    private List<String> alertasEstoque = new ArrayList<>();

    @Override
    public Venda salvar(Venda venda) {
        Produtos produtoCompleto = serviceProduto.localizar(venda.getProduto().getProId());
        venda.setProduto(produtoCompleto);

        validarOperacaoEstoque(venda);
        Venda vendaSalva = repositoryVenda.save(venda);
        atualizarEstoqueProduto(venda);
        this.alertasEstoque = verificarAlertasEstoque(venda.getProduto());
        return vendaSalva;
    }

    public List<String> getAlertasEstoque() {
        return alertasEstoque;
    }

    private void validarOperacaoEstoque(Venda venda) {
        Produtos produto = venda.getProduto();
        int quantidade = venda.getVenQuantidade();

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        if (venda.getVenAcao().equals("saida")) {
            if (produto.getProQuantidadeEstoque() <= quantidade) {
                throw new RuntimeException("A quantidade em estoque é de " +
                        produto.getProQuantidadeEstoque());
            }
        } else if (venda.getVenAcao().equals("entrada") &&
                (produto.getProQuantidadeEstoque() + quantidade) > produto.getProEstoquemaximo()) {
            throw new RuntimeException("Limite máximo de estoque excedido! Máximo: " +
                    produto.getProEstoquemaximo());
        }
    }

    private void atualizarEstoqueProduto(Venda venda) {
        Produtos produto = venda.getProduto();
        int novaQuantidade = venda.getVenAcao().equals("entrada")
                ? produto.getProQuantidadeEstoque() + venda.getVenQuantidade()
                : produto.getProQuantidadeEstoque() - venda.getVenQuantidade();

        produto.setProQuantidadeEstoque(novaQuantidade);
        serviceProduto.salvar(produto);
    }

    private List<String> verificarAlertasEstoque(Produtos produto) {
        List<String> alertas = new ArrayList<>();

        if (produto.getProQuantidadeEstoque() < produto.getProEstoqueminimo()) {
            alertas.add("⚠️ Estoque mínimo atingido para o produto " + produto.getProNome());
        }

        if (produto.getProQuantidadeEstoque() > produto.getProEstoquemaximo() * 0.9) {
            alertas.add("🔔 Estoque próximo ao máximo para o produto " + produto.getProNome());
        }

        if (produto.getProVencimento().isBefore(LocalDate.now().plusMonths(1))) {
            alertas.add("📅 Produto " + produto.getProNome() + " próximo do vencimento");
        }

        return alertas;
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
        throw new UnsupportedOperationException("Unimplemented method 'Atualizar'");
    }
}