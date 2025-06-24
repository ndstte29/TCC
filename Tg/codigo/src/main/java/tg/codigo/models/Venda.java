package tg.codigo.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tabela_vendas")
public class Venda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venId;
    
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate venData;
    
    @Column(nullable = false)
    private int venQuantidade;

    @Column(nullable = false)
    private String venAcao;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuId")
    private Usuarios usuario;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proId")
    private Produtos produto;
    
    @PrePersist
    protected void prePersist() {
        if (this.venData == null) {
            this.venData = LocalDate.now();
        }
    }

    public Long getVenId() {
        return venId;
    }

    public void setVenId(Long venId) {
        this.venId = venId;
    }

    public LocalDate getVenData() {
        return venData;
    }

    public void setVenData(LocalDate venData) {
        this.venData = venData;
    }

    public int getVenQuantidade() {
        return venQuantidade;
    }

    public void setVenQuantidade(int venQuantidade) {
        this.venQuantidade = venQuantidade;
    }

    public String getVenAcao() {
        return venAcao;
    }

    public void setVenAcao(String venAcao) {
        this.venAcao = venAcao;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Produtos getProduto() {
        return produto;
    }

    public void setProduto(Produtos produto) {
        this.produto = produto;
    }
    
}