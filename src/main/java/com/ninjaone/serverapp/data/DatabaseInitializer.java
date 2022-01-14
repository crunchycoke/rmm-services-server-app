package com.ninjaone.serverapp.data;

import com.ninjaone.serverapp.enums.ServiceDeviceType;
import com.ninjaone.serverapp.models.DeviceService;
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
    CommandLineRunner initDatabase(DeviceServiceRepository deviceServiceRepository) {
        return args -> {
            log.info("Preloading " + deviceServiceRepository.save(
                    new DeviceService("Antivirus", ServiceDeviceType.WINDOWS, new BigDecimal(5))));
            log.info("Preloading " + deviceServiceRepository.save(
                    new DeviceService("Antivirus", ServiceDeviceType.MAC, new BigDecimal(7))));
            log.info("Preloading " + deviceServiceRepository.save(
                    new DeviceService("Cloudberry", ServiceDeviceType.BOTH, new BigDecimal(3))));
            log.info("Preloading " + deviceServiceRepository.save(
                    new DeviceService("PSA", ServiceDeviceType.BOTH, new BigDecimal(2))));
            log.info("Preloading " + deviceServiceRepository.save(
                    new DeviceService("TeamViewer", ServiceDeviceType.BOTH, new BigDecimal(1))));

            deviceServiceRepository.findAll().forEach(deviceService -> log.info("Preloaded " + deviceService));
        };
    }
}
