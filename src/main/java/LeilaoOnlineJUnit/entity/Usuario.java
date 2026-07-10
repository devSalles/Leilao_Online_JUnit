package LeilaoOnlineJUnit.entity;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false,unique = true) @Email
    private String email;

    @Column(nullable = false,unique = true)@CPF
    private String cpf;

    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private StatusUsuario statusUsuario;

    @OneToMany(mappedBy = "usuario")
    private List<Item> itens = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private  List<Lance> lances = new ArrayList<>();
}
