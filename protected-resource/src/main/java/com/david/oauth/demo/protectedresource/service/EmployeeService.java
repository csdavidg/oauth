package com.david.oauth.demo.protectedresource.service;

import com.david.oauth.demo.protectedresource.dao.EmployeeDAO;
import com.david.oauth.demo.protectedresource.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements EmployeeInterface {

    private final EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Override
    public Employee findById(Long id) {
        return employeeDAO.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        employeeDAO.deleteById(id);
    }

}
