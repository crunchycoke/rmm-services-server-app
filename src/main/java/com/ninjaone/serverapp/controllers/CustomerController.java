package com.ninjaone.serverapp.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ninjaone.serverapp.enums.DeviceOperatingSystem;
import com.ninjaone.serverapp.enums.DeviceType;
import com.ninjaone.serverapp.exceptions.CustomerNotFoundException;
import com.ninjaone.serverapp.exceptions.ServiceCostNotFoundException;
import com.ninjaone.serverapp.modelassemblers.CustomerModelAssembler;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.CustomerBill;
import com.ninjaone.serverapp.models.CustomerDevice;
import com.ninjaone.serverapp.models.CustomerService;
import com.ninjaone.serverapp.repository.CustomerDeviceRepository;
import com.ninjaone.serverapp.repository.CustomerRepository;
import com.ninjaone.serverapp.repository.CustomerServiceRepository;
import com.ninjaone.serverapp.repository.ServiceCostRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerRepository customerRepository;
    private final CustomerDeviceRepository customerDeviceRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final ServiceCostRepository serviceCostRepository;
    private final CustomerModelAssembler customerAssembler;

    public CustomerController(CustomerRepository customerRepository,
        CustomerDeviceRepository customerDeviceRepository,
        CustomerServiceRepository customerServiceRepository,
        ServiceCostRepository serviceCostRepository,
        CustomerModelAssembler customerAssembler) {
        this.customerRepository = customerRepository;
        this.customerDeviceRepository = customerDeviceRepository;
        this.customerServiceRepository = customerServiceRepository;
        this.serviceCostRepository = serviceCostRepository;
        this.customerAssembler = customerAssembler;
    }

    @GetMapping("/customers")
    public CollectionModel<EntityModel<Customer>> getAllCustomers() {
        log.info("Attempting to get all customers.");

        List<EntityModel<Customer>> customers = customerRepository.findAll().stream()
                .map(customerAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(customers,
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
    }

    @GetMapping("/customers/{id}")
    public EntityModel<Customer> getCustomerById(@PathVariable Long id) {
        log.info("Attempting to get customer " + id);

        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));

        return customerAssembler.toModel(customer);
    }

    @GetMapping("/customers/{id}/billing")
    public CustomerBill getCustomerBillingById(@PathVariable Long id) {
        log.info("Attempting to get customer " + id + " billing info");

        List<CustomerDevice> customerDevices =
            customerDeviceRepository.getCustomerDevicesByCustomerId(id);

        List<CustomerService> customerServices =
            customerServiceRepository.getCustomerServicesByCustomerId(id);

        CustomerBill customerBill = calculateCustomerBill(customerDevices, customerServices);

        log.info("Finished calculating customer " + id + " billing info");

        return customerBill;
    }

    private CustomerBill calculateCustomerBill(List<CustomerDevice> customerDevices,
        List<CustomerService> customerServices) {

        CustomerBill customerBill = new CustomerBill();

        for (CustomerDevice customerDevice : customerDevices) {
            customerBill.addDeviceToTotalCost();

            DeviceOperatingSystem selectedDeviceOperatingSystem;
            if (DeviceType.isWindowsDevice(customerDevice.getDeviceType())) {
                selectedDeviceOperatingSystem = DeviceOperatingSystem.WINDOWS;
                customerBill.incrementWindowsDeviceCount();
            } else {
                selectedDeviceOperatingSystem = DeviceOperatingSystem.MAC;
                customerBill.incrementMacDeviceCount();
            }

            for (CustomerService customerService : customerServices) {
                BigDecimal determinedServicePrice =
                    serviceCostRepository.getServicePriceByTypeAndOs(
                            customerService.getServiceType(),
                            selectedDeviceOperatingSystem)
                        .orElseThrow(() ->
                            new ServiceCostNotFoundException(customerService.getServiceType()));

                customerBill.setTotalCost(
                    customerBill.getTotalCost().add(determinedServicePrice));
            }
        }

        return customerBill;
    }
}
