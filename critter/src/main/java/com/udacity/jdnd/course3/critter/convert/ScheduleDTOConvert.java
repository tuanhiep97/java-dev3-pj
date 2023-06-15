package com.udacity.jdnd.course3.critter.convert;

import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ScheduleDTOConvert {

    private final EmployeeService employeeService;
    private final PetService petService;

    public ScheduleDTOConvert(EmployeeService employeeService, PetService petService) {
        this.employeeService = employeeService;
        this.petService = petService;
    }

    public ScheduleDTO convertScheduleToDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        List<Long> listOfEmployeeIDs = schedule.getEmployees()
                .stream()
                .map(Employee::getId)
                .collect(toList());

        List<Long> listOfPetIDs = schedule.getPets()
                .stream()
                .map(Pet::getId)
                .collect(toList());

        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getSkills());
        scheduleDTO.setEmployeeIds(listOfEmployeeIDs);
        scheduleDTO.setPetIds(listOfPetIDs);

        return scheduleDTO;
    }

    public Schedule convertDTOToSchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();

        BeanUtils.copyProperties(scheduleDTO, schedule);

        schedule.setDate(scheduleDTO.getDate());
        schedule.setSkills(scheduleDTO.getActivities());

        List<Employee> employees = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();
        scheduleDTO.getEmployeeIds().forEach(employeeId -> employees.add(employeeService.findById(employeeId).get()));
        scheduleDTO.getPetIds().forEach(petId -> pets.add(petService.findById(petId).get()));
        schedule.setEmployees(employees);
        schedule.setPets(pets);

        return schedule;
    }
}
