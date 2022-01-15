package com.ninjaone.serverapp;

import com.ninjaone.serverapp.enums.DeviceType;
import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.CustomerDevice;
import com.ninjaone.serverapp.models.CustomerService;
import com.ninjaone.serverapp.repository.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ServerAppApplication {

	public static void main(String[] args) {

//		SpringApplication.run(ServerAppApplication.class, args);

		ConfigurableApplicationContext configurableApplicationContext =
				SpringApplication.run(ServerAppApplication.class, args);

		CustomerRepository customerRepository = configurableApplicationContext.getBean(CustomerRepository.class);

		Customer customer = new Customer("Test", "Cust", "Omer", "admin", "password");
		CustomerDevice macBookPro = new CustomerDevice("My MacBook Pro", DeviceType.MAC, customer);
		CustomerDevice windowsServer = new CustomerDevice("Windows Server 123", DeviceType.WINDOWS_SERVER, customer);
		CustomerDevice windowsLaptop = new CustomerDevice("Windows Laptop", DeviceType.WINDOWS_WORKSTATION, customer);

		List<CustomerDevice> customerDevices = Arrays.asList(macBookPro, windowsServer, windowsLaptop);
		customer.setCustomerDevices(customerDevices);

		CustomerService antivirusService = new CustomerService("Antivirus", ServiceType.ANTIVIRUS, customer);
		CustomerService cloudberryService = new CustomerService("Cloudberry", ServiceType.CLOUDBERRY, customer);
		CustomerService psaService = new CustomerService("PSA", ServiceType.PSA, customer);
		CustomerService teamviewerService = new CustomerService("Teamviewer", ServiceType.TEAMVIEWER, customer);

		List<CustomerService> customerServices = Arrays.asList(antivirusService, cloudberryService, psaService, teamviewerService);
		customer.setCustomerServices(customerServices);

		customerRepository.save(customer);
	}
}
