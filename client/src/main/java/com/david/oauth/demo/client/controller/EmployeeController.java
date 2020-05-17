package com.david.oauth.demo.client.controller;

import com.david.oauth.demo.client.service.EmployeeService;
import com.david.oauth.demo.oauthcommons.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employeesList")
    public String listEmployees(Model model) {
        List<Employee> employeesList = employeeService.getEmployeesFromAPI();
        model.addAttribute("employeesList", employeesList);
        return "list-employees";
    }

}
