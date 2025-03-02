package tg.codigo.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(includeFieldNames = true)
@EqualsAndHashCode
@Entity
@Table(name = "tabela_fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forId;
    
    @Column(length = 14, nullable = false, unique = true)
    private Long forCnpj;

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
}