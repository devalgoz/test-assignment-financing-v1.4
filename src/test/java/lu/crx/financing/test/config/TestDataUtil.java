package lu.crx.financing.test.config;

import lombok.RequiredArgsConstructor;
import lu.crx.financing.repository.FinancedInvoiceRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

@RequiredArgsConstructor
public class TestDataUtil {

    private final DataSource dataSource;

    private final EntityManager entityManager;

    @PostConstruct
    private void init() {
        ClassPathResource initData = new ClassPathResource("data/init-data.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initData);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
    }

}

