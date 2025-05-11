package tg.codigo.models;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString(includeFieldNames = true)
@EqualsAndHashCode
@Entity
@Table(name = "tabela_produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proId;

    @Column(length = 100, nullable = false)
    private String proNome;

    @Column(length = 100, nullable = false)
    private String proCategoria;

    @Column(nullable = false)
    private int proUnidade;

    @Column(nullable = false)
    private int proEstoqueminimo;

    @Column(nullable = false)
    private int proEstoquemaximo;

    @Column(nullable = false)
    private LocalDate proVencimento; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "forId", referencedColumnName = "forId")
    private Fornecedor fornecedor;

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public String getProNome() {
        return proNome;
    }

    public void setProNome(String proNome) {
        this.proNome = proNome;
    }

    public String getProCategoria() {
        return proCategoria;
    }

    public void setProCategoria(String proCategoria) {
        this.proCategoria = proCategoria;
    }

    public int getProUnidade() {
        return proUnidade;
    }

    public void setProUnidade(int proUnidade) {
        this.proUnidade = proUnidade;
    }

    public int getProEstoqueminimo() {
        return proEstoqueminimo;
    }

    public void setProEstoqueminimo(int proEstoqueminimo) {
        this.proEstoqueminimo = proEstoqueminimo;
    }

    public int getProEstoquemaximo() {
        return proEstoquemaximo;
    }

    public void setProEstoquemaximo(int proEstoquemaximo) {
        this.proEstoquemaximo = proEstoquemaximo;
    }

    public LocalDate getProVencimento() {
        return proVencimento;
    }

    public void setProVencimento(LocalDate proVencimento) {
        this.proVencimento = proVencimento;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}