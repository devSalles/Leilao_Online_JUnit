package LeilaoOnlineJUnit.entity;

import LeilaoOnlineJUnit.Enum.StatusItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorInicial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusItem statusItem;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario = new Usuario();

    @OneToOne
    private Item item;
}
