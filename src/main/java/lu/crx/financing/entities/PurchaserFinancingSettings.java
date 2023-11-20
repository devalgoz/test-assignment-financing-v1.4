package lu.crx.financing.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Financing settings set by the purchaser for a specific creditor.
 */
@Entity
@Getter
@Setter
@ToString(exclude = {"purchaser"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaserFinancingSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(optional = false)
    private Purchaser purchaser;

    @ManyToOne(optional = false)
    private Creditor creditor;

    /**
     * The annual financing rate set by the purchaser for this creditor.
     */
    @Basic(optional = false)
    private int annualRateInBps;

}
