package tg.codigo.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@Setter
@ToString(includeFieldNames = true)
@EqualsAndHashCode
@Entity
@Table(name = "tabela_vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venId;

    @Column(nullable = false)
    private LocalDate venData;

    @Column(nullable = false)
    private int venQuantidade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuId", referencedColumnName = "usuId")
    private Usuarios usuarios;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proId", referencedColumnName = "proId")
    private Produtos produtos;
}