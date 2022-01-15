package com.ninjaone.serverapp.data;

import com.ninjaone.serverapp.enums.DeviceOperatingSystem;
import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.ServiceCost;
import com.ninjaone.serverapp.repository.CustomerRepository;
import com.ninjaone.serverapp.repository.DeviceServiceRepository;
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
    CommandLineRunner initDatabase(DeviceServiceRepository deviceServiceRepository, CustomerRepository customerRepository) {
        return args -> {
            log.info("Preloading " + deviceServiceRepository.save(
                    new ServiceCost("Windows Antivirus", ServiceType.ANTIVIRUS,
                            DeviceOperatingSystem.WINDOWS, new BigDecimal(5))));

            log.info("Preloading " + deviceServiceRepository.save(
                    new ServiceCost("Mac Antivirus", ServiceType.ANTIVIRUS,
                            DeviceOperatingSystem.MAC, new BigDecimal(7))));

            log.info("Preloading " + deviceServiceRepository.save(
                    new ServiceCost("Cloudberry", ServiceType.CLOUDBERRY,
                            DeviceOperatingSystem.ALL, new BigDecimal(3))));

            log.info("Preloading " + deviceServiceRepository.save(
                    new ServiceCost("PSA", ServiceType.PSA,
                            DeviceOperatingSystem.ALL, new BigDecimal(2))));

            log.info("Preloading " + deviceServiceRepository.save(
                    new ServiceCost("Teamviewer", ServiceType.TEAMVIEWER,
                            DeviceOperatingSystem.ALL, new BigDecimal(1))));

            deviceServiceRepository.findAll().forEach(deviceService -> log.info("Preloaded " + deviceService));
        };
    }
}
