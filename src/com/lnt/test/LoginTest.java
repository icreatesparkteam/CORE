package com.lnt.test;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

//Test Client

public class LoginTest {
	public static String UrlConstant = "http://localhost:8080/iControlE-Core/rest/";
	static Client client = Client.create();

	public static void main(String[] args) throws JsonGenerationException,
			JsonMappingException, IOException {

		loginUser();


	}

	
	private static void loginUser() throws JsonGenerationException,
			JsonMappingException, IOException {
		System.out.println("Login user method : ");
		MultivaluedMap<String, String> inputMap = new MultivaluedMapImpl();
		inputMap.add("username", "oem");
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
	}

	

}
