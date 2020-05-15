package com.david.oauth.demo.protectedresource.service;

import com.david.oauth.demo.protectedresource.entity.Employee;

import java.util.List;


public interface EmployeeInterface {

    public List<Employee> findAll();

    public Employee save(Employee employee);

    public Employee findById(Long id);

    public void deleteById(Long id);

}
