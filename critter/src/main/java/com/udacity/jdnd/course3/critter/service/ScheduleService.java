package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findScheduleByPet(Pet pet) {
        return scheduleRepository.findScheduleByPets(pet);
    }

    public List<Schedule> findScheduleByEmployee(Employee employee) {
        return scheduleRepository.findScheduleByEmployees(employee);
    }

    public List<Schedule> findScheduleByCustomer(Customer customer) {
        List<Pet> pets = customer.getPets();
        List<Schedule> schedules = new ArrayList<>();

        pets.forEach(pet -> {
            List<Schedule> petsOnSchedule = scheduleRepository.findScheduleByPets(pet);
            schedules.addAll(petsOnSchedule);
        });

        return schedules;
    }
}
