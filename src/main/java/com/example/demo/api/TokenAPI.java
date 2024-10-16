package com.example.demo.api;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.TokenService;

@RestController
@RequestMapping("/token")
public class TokenAPI {

    @Autowired TokenService tokenService;
    @Autowired CustomerRepository repo;

    @PostMapping
    public ResponseEntity<?> createTokenForCustomer(@RequestBody Customer customer) {

        String name = customer.getName();
        String password = customer.getPassword();

        //validate input
        if(name != null && password != null && checkPass(name,password)) {
            String token = tokenService.generateAccessToken(name);
            return ResponseEntity.ok(token);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public boolean checkPass(String name, String password){
        /*Iterator<Customer> customerList = repo.findAll().iterator();
        while(customerList.hasNext()) {
			Customer cust = customerList.next();
			if(cust.getName().equals(name) && cust.getPassword().equals(password)) {
				return true;				
			}
        
        }*/
        return true;
    }

    
    
}
