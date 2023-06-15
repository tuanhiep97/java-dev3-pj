package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.convert.PetDTOConvert;
import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;
    private final CustomerService customerService;
    private final PetDTOConvert petDTOConvert;

    public PetController(PetService petService, CustomerService customerService, PetDTOConvert petDTOConvert) {
        this.petService = petService;
        this.customerService = customerService;
        this.petDTOConvert = petDTOConvert;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        if (customerService.findById(petDTO.getOwnerId()).isEmpty()) {
            throw new UnsupportedOperationException();
        }
        Customer customer = customerService.findById(petDTO.getOwnerId()).get();
        Pet petToSave = petDTOConvert.convertDTOToPet(petDTO);
        petToSave.setCustomer(customer);
        return petDTOConvert.convertPetToDTO(petService.save(petToSave));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        if (petService.findById(petId).isEmpty()) {
            throw new UnsupportedOperationException();
        }
        Pet pet = petService.findById(petId).get();
        return petDTOConvert.convertPetToDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.getAllPets()
                .stream()
                .map(petDTOConvert::convertPetToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.findByCustomer(ownerId)
                .stream()
                .map(petDTOConvert::convertPetToDTO)
                .collect(Collectors.toList());
    }
}
