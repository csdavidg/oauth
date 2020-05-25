package com.david.oauth.demo.client.service;

import com.david.oauth.demo.oauthcommons.entity.Employee;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.util.Arrays;
import java.util.List;

import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_ACCESS_TOKEN;

@Service
public class EmployeeService {

    private final static Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Value("${api.protected.employees}")
    public String employeesEndpoint;

    private final TokenService tokenService;

    @Autowired
    public EmployeeService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public List<Employee> getEmployeesFromAPI() {
        try {
            String valueFromKey = this.tokenService.getValueFromKeyStore(KEY_STORE_ALIAS_ACCESS_TOKEN);
            ResponseToken responseToken = new ObjectMapper().readValue(valueFromKey, ResponseToken.class);
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
