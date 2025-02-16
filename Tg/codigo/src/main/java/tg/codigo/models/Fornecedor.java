package tg.codigo.models;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(includeFieldNames=true)
@EqualsAndHashCode
@Entity
@Table(name = "TabelaFornecedores")
public class Fornecedor {
    @Id
    private long forCnpj;
    @Column(length = 100)
    private String forRazaoSocial;
    @Column(length = 200)
    private String forEndereco;

    @OneToMany(mappedBy = "fornecedorProduto", cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    private List<Produtos> funcionarios = new ArrayList<>();
}
