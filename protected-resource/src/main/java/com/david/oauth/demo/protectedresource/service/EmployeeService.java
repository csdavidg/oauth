package com.david.oauth.demo.protectedresource.service;

import com.david.oauth.demo.protectedresource.dao.EmployeeDAO;
import com.david.oauth.demo.protectedresource.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EmployeeService implements EmployeeInterface {

    private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Transactional
    public List<Employee> findAll() {
        return (List<Employee>) employeeDAO.findAll();
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Transactional
    public Employee findById(Long id) {
        return employeeDAO.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        employeeDAO.deleteById(id);
    }

}
