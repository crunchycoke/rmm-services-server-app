package com.ninjaone.serverapp.data;

import com.ninjaone.serverapp.enums.ServiceDeviceType;
import com.ninjaone.serverapp.models.ServiceCost;
import com.ninjaone.serverapp.repository.ServiceCostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Bean
    CommandLineRunner initDatabase(ServiceCostRepository serviceCostRepository) {
        return args -> {
            log.info("Preloading " + serviceCostRepository.save(
                    new ServiceCost("Windows Antivirus", ServiceDeviceType.WINDOWS, new BigDecimal(5))));
            log.info("Preloading " + serviceCostRepository.save(
                    new ServiceCost("Mac Antivirus", ServiceDeviceType.MAC, new BigDecimal(7))));
            log.info("Preloading " + serviceCostRepository.save(
                    new ServiceCost("Cloudberry", ServiceDeviceType.BOTH, new BigDecimal(3))));
            log.info("Preloading " + serviceCostRepository.save(
                    new ServiceCost("PSA", ServiceDeviceType.BOTH, new BigDecimal(2))));
            log.info("Preloading " + serviceCostRepository.save(
                    new ServiceCost("TeamViewer", ServiceDeviceType.BOTH, new BigDecimal(1))));

            serviceCostRepository.findAll().forEach(serviceCost -> log.info("Preloaded " + serviceCost));
        };
    }
}
