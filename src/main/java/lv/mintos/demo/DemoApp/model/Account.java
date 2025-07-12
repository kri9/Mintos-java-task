package lv.mintos.demo.DemoApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal balance;
}
