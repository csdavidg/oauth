package com.david.oauth.demo.client.service;

import com.david.oauth.demo.oauthcommons.entity.Employee;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final static Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Value("${api.protected.employees}")
    public String employeesEndpoint;

    private final OauthService oauthService;

    @Autowired
    public EmployeeService(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    public List<Employee> getEmployeesFromAPI() {
        try {
            ResponseToken responseToken = oauthService.getAccessTokenFromKeyStore();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(responseToken.getAccessToken());

            HttpEntity<?> request = new HttpEntity<>("", headers);
            ResponseEntity<Employee[]> response = new RestTemplate().exchange(employeesEndpoint, HttpMethod.GET, request, Employee[].class);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            logger.info("Exception " + e.getMessage());
            return null;
        }
    }

}
