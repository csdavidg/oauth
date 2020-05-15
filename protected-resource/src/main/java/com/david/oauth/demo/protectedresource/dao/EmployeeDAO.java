package com.david.oauth.demo.protectedresource.dao;

import com.david.oauth.demo.protectedresource.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeDAO extends JpaRepository<Employee, Long> {

}
