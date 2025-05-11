package tg.codigo.models;

import java.time.LocalDateTime;

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
    @Column(length = 100)
    private String usuNome;
    @Column(length = 100)
    private String usuEmail;
    @Column(length = 100)
    private String usuLogin;
    @Column(length = 100)
    private String usuSenha;
    @Column(length = 100)
    private String resetToken;
    @Column
    private LocalDateTime tokenExpiration;
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
    public String getUsuLogin() {
        return usuLogin;
    }
    public void setUsuLogin(String usuLogin) {
        this.usuLogin = usuLogin;
    }
    public String getUsuSenha() {
        return usuSenha;
    }
    public void setUsuSenha(String usuSenha) {
        this.usuSenha = usuSenha;
    }
    public String getResetToken() {
        return resetToken;
    }
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }
    public void setTokenExpiration(LocalDateTime tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    
}
