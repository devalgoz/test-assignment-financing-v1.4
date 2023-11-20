package lu.crx.financing.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * An invoice issued by the {@link Creditor} to the {@link Debtor} for shipped goods.
 */
@Entity
@Table(indexes = @Index(columnList = "financed"))
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Creditor is the entity that issued the invoice.
     */
    @ManyToOne(optional = false)
    private Creditor creditor;

    /**
     * Debtor is the entity obliged to pay according to the invoice.
     */
    @ManyToOne
    private Debtor debtor;

    /**
     * Maturity date is the date on which the {@link #debtor} is to pay for the invoice.
     * In case the invoice was financed, the money will be paid in full on this date to the purchaser of the invoice.
     */
    @Basic(optional = false)
    private LocalDate maturityDate;

    @Basic(optional = false)
    private long valueInCents;

    /**
     * Indicates whether the invoice has been processed
     */
    @Basic(optional = false)
    private boolean financed = false;
}
