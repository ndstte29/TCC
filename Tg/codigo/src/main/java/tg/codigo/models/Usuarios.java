package tg.codigo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "TabelaUsuarios")

public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuId;
    @Column(length = 100)
    private String usuNome;
    @Column(length = 100)
    private String usuLogin;
    @Column(length = 100)
    private String usuSenha;
    
}
