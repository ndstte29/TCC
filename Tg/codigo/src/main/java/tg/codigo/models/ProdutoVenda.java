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
@Table(name = "tabela_produto_venda")
public class ProdutoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proId", referencedColumnName = "proId")
    private Produtos produtos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venId", referencedColumnName = "venId")
    private Venda venda;
}