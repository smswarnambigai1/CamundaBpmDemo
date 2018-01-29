package com.diligent.demo.BpmDemo;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class Test {

	public static final String REST_URL = "http://demo.m8solutions.com/sugar/service/v4/rest.php";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//0987654321
		
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(REST_URL);
        target = target.path("contacts").queryParam("phone_work", "0987654321");
        System.out.println(target.getUri().toString());

        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = builder.get();
        List list = response.readEntity(List.class);

        
		
		
	}
}
