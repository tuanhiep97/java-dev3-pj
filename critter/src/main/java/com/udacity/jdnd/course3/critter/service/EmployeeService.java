package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public void setAvailability(Set<DayOfWeek> availability, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        employee.setDaysAvailable(availability);
    }

    public Set<Employee> findAvailableEmployee(LocalDate localDate, Set<EmployeeSkill> skills) {
        Set<Employee> employeesHaveDemandSkills = new HashSet<>();
        Set<Employee> availableEmployees = employeeRepository.findEmployeesByDaysAvailable(localDate.getDayOfWeek());

        availableEmployees.forEach(employee -> {
            boolean matchedSkillSet = employee.getSkills().containsAll(skills);
            if (matchedSkillSet) {
                employeesHaveDemandSkills.add(employee);
            }
        });
        return employeesHaveDemandSkills;
    }
}
