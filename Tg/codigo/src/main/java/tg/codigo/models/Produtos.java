package tg.codigo.models;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(includeFieldNames = true)
@EqualsAndHashCode
@Entity
@Table(name = "TabelaProdutos")

public class Produtos {
    //Criação dos atributos no banco
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proId;
    @Column(length = 100)
    private String ProNome;
    @Column(length = 100)
    private String proCategoria;
    @Column
    private int proUnidade;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate proVencimento;
    @Column
    private int proEstoqueminimo;
    @Column
    private int proEstoquemaximo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "for_cnpj")
    private Fornecedor fornecedorProduto;
}
