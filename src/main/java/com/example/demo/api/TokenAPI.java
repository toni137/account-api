package com.example.demo.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Customer;
import com.example.demo.domain.CustomerFactory;
import com.example.demo.domain.Token;
import com.example.demo.service.TokenService;

@RestController
@RequestMapping("/token")
public class TokenAPI {

    public  static Token appUserToken;

    @GetMapping
    public String getAll(){
        return "fake token";
    }
    

    @PostMapping
    public ResponseEntity<?> createTokenForCustomer(@RequestBody Customer customer) {

        String name = customer.getName();
        String password = customer.getPassword();

        //validate input
        if(name != null && password != null && checkPass(name,password)) {
            Token token = createToken(name);
            return ResponseEntity.ok(token);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public boolean checkPass(String name, String password){
        // special case for application user
		if(name.equals("ApiClientApp") && password.equals("secret")) {
			return true;
		}
		// make call to customer service 
		Customer cust = getCustomerByNameFromCustomerAPI(name);
		
		// compare name and password
		if(cust != null && cust.getName().equals(name) && cust.getPassword().equals(password)) {
			return true;				
		}		
		return false;
    }

    private static Token createToken(String name){

        String token_string = TokenService.generateAccessToken(name);
        return new Token(token_string);
    }

    public static Token getAppUserToken() {
		if(appUserToken == null || appUserToken == null || appUserToken.getToken().length() == 0) {
			appUserToken = createToken("ApiClientApp");
		}
		return appUserToken;
	}

    private Customer getCustomerByNameFromCustomerAPI(String username) {
		try {

			URL url = new URL("http://localhost:8080/api/customers/byname/" + username);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			Token token = getAppUserToken();
			conn.setRequestProperty("authorization", "Bearer " + token.getToken());

			if (conn.getResponseCode() != 200) {
				return null;
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = "";
				String out = "";
				while ((out = br.readLine()) != null) {
					output += out;
				}
				conn.disconnect();
				return CustomerFactory.getCustomer(output);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;

		} catch (java.io.IOException e) {
			e.printStackTrace();
			return null;
		}

	}  	
    
    
}
