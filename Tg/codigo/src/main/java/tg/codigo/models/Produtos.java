package tg.codigo.models;

import java.time.LocalDate;

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
}