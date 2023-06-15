package com.udacity.jdnd.course3.critter.convert;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PetDTOConvert {
    private final CustomerService customerService;

    public PetDTOConvert(CustomerService customerService) {
        this.customerService = customerService;
    }

    public PetDTO convertPetToDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        Long customerID = pet.getCustomer().getId();

        BeanUtils.copyProperties(pet, petDTO, "customer", "petType");

        Customer customer = customerService.findById(customerID).get();
        petDTO.setOwnerId(customer.getId());
        petDTO.setType(pet.getPetType());

        return petDTO;
    }

    public Pet convertDTOToPet(PetDTO petDTO){
        Pet pet = new Pet();
        Long customerID = petDTO.getOwnerId();

        BeanUtils.copyProperties(petDTO, pet, "ownerId", "type");

        //Adds the customer to the Pet instance
        Customer customer = customerService.findById(customerID).get();
        pet.setCustomer(customer);
        pet.setPetType(petDTO.getType());

        return pet;
    }
}
