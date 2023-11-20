package lu.crx.financing.repository;

import lu.crx.financing.entities.FinancedInvoice;
import lu.crx.financing.entities.FinancedInvoiceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancedInvoiceRepository extends JpaRepository<FinancedInvoice, FinancedInvoiceId> {
}
