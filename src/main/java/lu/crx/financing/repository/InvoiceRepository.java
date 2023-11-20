package lu.crx.financing.repository;

import lu.crx.financing.entities.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value =
            """
            select invoice.id from Invoice invoice 
            where invoice.financed = false
            """
            ,
            countQuery =
            """
            select count(invoice.id) from Invoice invoice 
            where invoice.financed = false
            """)
    Page<Long> findNonFinancedInvoicesIds(Pageable pageable);

    long countByFinancedFalse();

    long countByFinancedTrue();
}
