package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.convert.CustomerDTOConvert;
import com.udacity.jdnd.course3.critter.convert.EmployeeDTOConvert;
import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final CustomerService customerService;
    private final PetService petService;
    private final EmployeeService employeeService;
    private final CustomerDTOConvert customerDTOConvert;
    private final EmployeeDTOConvert employeeDTOConvert;

    public UserController(CustomerService customerService, PetService petService, EmployeeService employeeService, CustomerDTOConvert customerDTOConvert, EmployeeDTOConvert employeeDTOConvert) {
        this.customerService = customerService;
        this.petService = petService;
        this.employeeService = employeeService;
        this.customerDTOConvert = customerDTOConvert;
        this.employeeDTOConvert = employeeDTOConvert;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerDTOConvert.convertDTOToCustomer(customerDTO);
        return customerDTOConvert.convertCustomerToDTO(customerService.saveCustomer(customer));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers()
                .stream()
                .map(customerDTOConvert::convertCustomerToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Optional<Pet> optionalPet = petService.findById(petId);
        if (optionalPet.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        Customer customer = customerService.getCustomerByPet(optionalPet.get());
        return customerDTOConvert.convertCustomerToDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeDTOConvert.convertDTOToEmployee(employeeDTO);
        return employeeDTOConvert.convertEmployeeToDTO(employeeService.saveEmployee(employee));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        if (employeeService.findById(employeeId).isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return employeeDTOConvert.convertEmployeeToDTO(employeeService.findById(employeeId).get());
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        if (employeeService.findById(employeeId).isEmpty()) {
            throw new UnsupportedOperationException();
        }
        employeeService.setAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        LocalDate localDate = employeeDTO.getDate();
        HashSet<EmployeeSkill> skills = new HashSet<>(employeeDTO.getSkills());

        return employeeService.findAvailableEmployee(localDate, skills)
                .stream()
                .map(employeeDTOConvert::convertEmployeeToDTO)
                .collect(Collectors.toList());
    }
}
