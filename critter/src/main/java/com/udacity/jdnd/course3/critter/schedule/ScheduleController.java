package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.convert.ScheduleDTOConvert;
import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetService petService;
    private final ScheduleDTOConvert scheduleDTOConvert;

    public ScheduleController(ScheduleService scheduleService, CustomerService customerService, EmployeeService employeeService, PetService petService, ScheduleDTOConvert scheduleDTOConvert) {
        this.scheduleService = scheduleService;
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
        this.scheduleDTOConvert = scheduleDTOConvert;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleDTOConvert.convertDTOToSchedule(scheduleDTO);
        return scheduleDTOConvert.convertScheduleToDTO(scheduleService.saveSchedule(schedule));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules()
                .stream()
                .map(scheduleDTOConvert::convertScheduleToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        if (petService.findById(petId).isEmpty()) {
            throw new UnsupportedOperationException();
        }
        Pet pet = petService.findById(petId).get();
        return scheduleService.findScheduleByPet(pet)
                .stream()
                .map(scheduleDTOConvert::convertScheduleToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        if (employeeService.findById(employeeId).isEmpty()) {
            throw new UnsupportedOperationException();
        }
        Employee employee = employeeService.findById(employeeId).get();
        return scheduleService.findScheduleByEmployee(employee)
                .stream()
                .map(scheduleDTOConvert::convertScheduleToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        if (customerService.findById(customerId).isEmpty()) {
            throw new UnsupportedOperationException();
        }
        Customer customer = customerService.findById(customerId).get();
        return scheduleService.findScheduleByCustomer(customer)
                .stream()
                .map(scheduleDTOConvert::convertScheduleToDTO)
                .collect(Collectors.toList());
    }
}
