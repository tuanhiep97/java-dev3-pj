package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByPet(Pet pet) {
        return customerRepository.findCustomerByPets(pet);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public void savePetByCustomer(Pet pet, Customer customer) {
        List<Pet> listOfCustomerPets = customer.getPets();

        if(listOfCustomerPets == null){
            listOfCustomerPets = new ArrayList<>();
        }

        listOfCustomerPets.add(pet);

        customer.setPets(listOfCustomerPets);
        customerRepository.save(customer);
    }
}
