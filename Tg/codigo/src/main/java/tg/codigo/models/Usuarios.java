package tg.codigo.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(includeFieldNames = true)
@EqualsAndHashCode
@Entity
@Table(name = "TabelaUsuarios")

public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuId;
    @Column(length = 100, nullable = false)
    private String usuNome;
    @Column(length = 100, unique = true, nullable = false)
    private String usuEmail;
    @Column(nullable = false)
    private String usuPermissao;
    @Column(length = 15, unique = true, nullable = false)
    private String usuCpf;
    @Column(length = 100, nullable = false)
    private String usuSenha;


    public Long getUsuId() {
        return usuId;
    }

    public void setUsuId(Long usuId) {
        this.usuId = usuId;
    }

    public String getUsuNome() {
        return usuNome;
    }

    public void setUsuNome(String usuNome) {
        this.usuNome = usuNome;
    }

    public String getUsuEmail() {
        return usuEmail;
    }

    public void setUsuEmail(String usuEmail) {
        this.usuEmail = usuEmail;
    }

    public String getUsuPermissao() {
        return usuPermissao;
    }

    public void setUsuPermissao(String usuPermissao) {
        this.usuPermissao = usuPermissao;
    }

    public String getUsuCpf() {
        return usuCpf;
    }

    public void setUsuCpf(String usuCpf) {
        this.usuCpf = usuCpf;
    }

    public String getUsuSenha() {
        return usuSenha;
    }

    public void setUsuSenha(String usuSenha) {
        this.usuSenha = usuSenha;
    }

}
