package tg.codigo.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(includeFieldNames = true)
@EqualsAndHashCode
@Entity
@Table(name = "tabela_fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forId;

    @Column(length = 14, nullable = false, unique = true)
    private String forCnpj;

    @Column(length = 100, nullable = false)
    private String forRazaoSocial;

    @Column(length = 100, nullable = false)
    private String forLogradouro;

    @Column(length = 10, nullable = false)
    private String forNumero;

    @Column(length = 100, nullable = false)
    private String forCidade;

    @Column(length = 100, nullable = false)
    private String forBairro;

    @Column(length = 2, nullable = false)
    private String forEstado;

    @Column(length = 9, nullable = false)
    private String forCep;

    @Column(length = 15, nullable = false)
    private String forTelefone;

    // Getters e Setters
    public Long getForId() {
        return forId;
    }

    public void setForId(Long forId) {
        this.forId = forId;
    }

    public String getForCnpj() {
        return forCnpj;
    }

    public void setForCnpj(String forCnpj) {
        this.forCnpj = forCnpj;
    }

    public String getForRazaoSocial() {
        return forRazaoSocial;
    }

    public void setForRazaoSocial(String forRazaoSocial) {
        this.forRazaoSocial = forRazaoSocial;
    }

    public String getForLogradouro() {
        return forLogradouro;
    }

    public void setForLogradouro(String forLogradouro) {
        this.forLogradouro = forLogradouro;
    }

    public String getForNumero() {
        return forNumero;
    }

    public void setForNumero(String forNumero) {
        this.forNumero = forNumero;
    }

    public String getForCidade() {
        return forCidade;
    }

    public void setForCidade(String forCidade) {
        this.forCidade = forCidade;
    }

    public String getForBairro() {
        return forBairro;
    }

    public void setForBairro(String forBairro) {
        this.forBairro = forBairro;
    }

    public String getForEstado() {
        return forEstado;
    }

    public void setForEstado(String forEstado) {
        this.forEstado = forEstado;
    }

    public String getForCep() {
        return forCep;
    }

    public void setForCep(String forCep) {
        this.forCep = forCep;
    }

    public String getForTelefone() {
        return forTelefone;
    }

    public void setForTelefone(String forTelefone) {
        this.forTelefone = forTelefone;
    }
}
