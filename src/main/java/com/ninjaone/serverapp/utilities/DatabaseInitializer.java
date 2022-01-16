package com.ninjaone.serverapp.utilities;

import com.ninjaone.serverapp.enums.DeviceOperatingSystem;
import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.ServiceCost;
import com.ninjaone.serverapp.repository.ServiceCostRepository;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Bean
    CommandLineRunner initDatabase(ServiceCostRepository serviceCostRepository) {
        return args -> {
            log.info("Preloading " + serviceCostRepository.save(
                new ServiceCost("Windows Antivirus", ServiceType.ANTIVIRUS,
                    DeviceOperatingSystem.WINDOWS, new BigDecimal(5))));

            log.info("Preloading " + serviceCostRepository.save(
                new ServiceCost("Cloudberry", ServiceType.CLOUDBERRY,
                    DeviceOperatingSystem.WINDOWS, new BigDecimal(3))));

            log.info("Preloading " + serviceCostRepository.save(
                new ServiceCost("PSA", ServiceType.PSA,
                    DeviceOperatingSystem.WINDOWS, new BigDecimal(2))));

            log.info("Preloading " + serviceCostRepository.save(
                new ServiceCost("Teamviewer", ServiceType.TEAMVIEWER,
                    DeviceOperatingSystem.WINDOWS, new BigDecimal(1))));

            log.info("Preloading " + serviceCostRepository.save(
                new ServiceCost("Mac Antivirus", ServiceType.ANTIVIRUS,
                    DeviceOperatingSystem.MAC, new BigDecimal(7))));

            log.info("Preloading " + serviceCostRepository.save(
                new ServiceCost("Cloudberry", ServiceType.CLOUDBERRY,
                    DeviceOperatingSystem.MAC, new BigDecimal(3))));

            log.info("Preloading " + serviceCostRepository.save(
                new ServiceCost("PSA", ServiceType.PSA,
                    DeviceOperatingSystem.MAC, new BigDecimal(2))));

            log.info("Preloading " + serviceCostRepository.save(
                new ServiceCost("Teamviewer", ServiceType.TEAMVIEWER,
                    DeviceOperatingSystem.MAC, new BigDecimal(1))));

            serviceCostRepository.findAll()
                .forEach(deviceService -> log.info("Preloaded " + deviceService));
        };
    }
}
