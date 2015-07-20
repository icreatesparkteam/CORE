package com.lnt.test;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.lnt.core.common.dto.ServiceProviderRegistrationDto;
import com.lnt.core.common.util.ERole;
import com.lnt.core.common.util.IConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

//Test Client

public class LoginTest {
	public static String UrlConstant = "http://localhost:8080/iControlE-Core/rest/";
	static Client client = Client.create();
	static String token;

	public static void main(String[] args) throws JsonGenerationException,
			JsonMappingException, IOException {

		loginUser();
		createSP(token);

	}

	
	private static void loginUser() throws JsonGenerationException,
			JsonMappingException, IOException {
		System.out.println("Login user method : ");
		MultivaluedMap<String, String> inputMap = new MultivaluedMapImpl();
		inputMap.add("username", "servpro2");
		inputMap.add("password", "Newuser@123");
		System.out.println("Login user method : inputMap " + inputMap);
		WebResource webResource = client.resource(UrlConstant + "auth/login");
		ClientResponse response = webResource.type(
				MediaType.APPLICATION_FORM_URLENCODED).post(
				ClientResponse.class, inputMap);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		System.out.println("Output from Server .... \n" + response.getStatus());
		String output = response.getEntity(String.class);
		System.out.println("login method : token " + output);
		token = output;
	}

	private static void createSP(String token)
			throws JsonGenerationException, JsonMappingException, IOException {
		ServiceProviderRegistrationDto reg = new ServiceProviderRegistrationDto();
		reg.setUserName("servpro2");
		reg.setPassword("Newuser@123");
		reg.setAddress("Munnekolala 2,Banagalore");
		reg.setPhoneNumber("91-7829157989");
		reg.setPrimaryEmailId("atanu.niyogi1@gmail.com");
		reg.setRole(ERole.SERVICE_PROVIDER.getId());
		reg.setAltPhoneNumber("91-7829157444");
		reg.setCity("Bangalore");
		reg.setState("KA");
		reg.setCountry("India");
		reg.setServiceProviderName("Comcast");
		reg.setsQuestion("What is your name?");
		reg.setsAnswer("Atanu");
		
		WebResource webResource = client.resource(UrlConstant + "registration/create");
		ObjectMapper mapper = new ObjectMapper();
		String inputData = mapper.writeValueAsString(reg);
		// String
		// inputJson="{\"userName\": \"TestUser9\",\"password\": \"1234\",\"name\": \"Murali1\", \"phoneNumber1\": \"0000000009\",\"phoneNumber2\": null,\"role\": 1,\"primaryEmailId\": \"murali.dhuli@gmail.com\",\"activationCode\": \"ABC9\",\"address\": \"Address1\"}";

		// String
		// callCenterInputJson="{\"userName\": \"TestUser11\",\"password\": \"1234\",\"name\": \"Murali1\",\"role\": 2,\"primaryEmailId\": \"murali.dhuli@gmail.com\",\"address\": \"Address1\"}";
		System.out.println("URL: "+UrlConstant + "registration/create");
		System.out.println("inputData : " + inputData);
		try{
			
			ClientResponse response = webResource.type("application/json")
					.header(IConstants.TOKEN_HEADER_KEY, token)
					.post(ClientResponse.class, inputData);
	
//			if (response.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : "
//						+ response.getStatus());
//			}
	
			System.out.println("Output from Server .... \n" + response.getStatus());
			String output1 = response.getEntity(String.class);
			System.out.println(output1);
		}catch(Exception e){
			System.out.println("Exception: "+e.toString());
			e.printStackTrace();
		}
	}

}
