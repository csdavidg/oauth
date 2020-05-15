package com.david.oauth.demo.client.service;

import com.david.oauth.demo.client.entity.Employee;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class EmployeeService {

    public List<Employee> getEmployeesFromAPI() {

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("cristian", "123456");
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8081/employee/all";
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Employee[]> response = new RestTemplate().exchange(url, HttpMethod.GET, request, Employee[].class);
        return Arrays.asList(response.getBody());
    }

}
