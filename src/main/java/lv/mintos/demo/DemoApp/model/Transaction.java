package lv.mintos.demo.DemoApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;
    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;
    @Column(nullable = false)
    private Timestamp timestamp;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(length = 3, nullable = false)
    private String sourceCurrency;
    @Column(length = 3, nullable = false)
    private String destinationCurrency;
    @Column(precision = 18, scale = 8)
    private BigDecimal exchangeRate;
    @Column
    private String description;
}
