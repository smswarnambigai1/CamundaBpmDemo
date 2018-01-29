package com.diligent.demo.BpmDemo;

import java.util.List;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.sugarcrm.www.sugarcrm.Link_name_to_fields_array;
import com.sugarcrm.www.sugarcrm.SugarsoapLocator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class CheckRecordUsingRest implements JavaDelegate {

	private final Logger LOGGER = Logger.getLogger(CheckRecordUsingRest.class.getName());
	
	public static final String REST_URL = "http://demo.m8solutions.com/sugar/service/v4/rest.php";
	
	public static final String SOAP_URL = "http://demo.m8solutions.com/sugar/service/v4/soap.php?wsdl";
	
	
	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		
        /**
         * SOAP Client
         */
		
        try {
        	
        	String phoneno = (String)delegateExecution.getVariable("phoneno");
        	String firstname = (String)delegateExecution.getVariable("firstname");
        	String lastname = (String)delegateExecution.getVariable("lastname");
        	String title = (String)delegateExecution.getVariable("title");
        	String department = (String)delegateExecution.getVariable("department");
        	
        	//0987654321
        	
			String url = "http://demo.m8solutions.com/sugar/service/v4/soap.php?wsdl";
			SugarsoapLocator loc = new SugarsoapLocator();
			com.sugarcrm.www.sugarcrm.SugarsoapPortType pt = loc.getsugarsoapPort(new java.net.URL(url));
			com.sugarcrm.www.sugarcrm.User_auth user_auth = new com.sugarcrm.www.sugarcrm.User_auth();
			user_auth.setUser_name("admin");
			user_auth.setPassword("fe01ce2a7fbac8fafaed7c982a04e229");

			java.lang.String application_name = null;
			com.sugarcrm.www.sugarcrm.Name_value[] name_value_list = new com.sugarcrm.www.sugarcrm.Name_value[1];
			com.sugarcrm.www.sugarcrm.Name_value nv = new com.sugarcrm.www.sugarcrm.Name_value();
			nv.setName("");
			nv.setValue("contacts");
			com.sugarcrm.www.sugarcrm.Entry_value ev = pt.login(user_auth, application_name, name_value_list);

			String mn = ev.getModule_name();
			System.out.println(mn);

			com.sugarcrm.www.sugarcrm.Name_value[] nvArr = ev.getName_value_list();
			for (int x = 0; x < nvArr.length; x++) {
				System.out.println(nvArr[x].getName() + " = " + nvArr[x].getValue());
			}

			Link_name_to_fields_array[] arr = new Link_name_to_fields_array[1];
			arr[0] = new Link_name_to_fields_array();
			arr[0].setName("phone_work");
			arr[0].setValue(new String[] { "0987654321" });

			/*
			 * get_entries(java.lang.String session, java.lang.String module_name, java.lang.String[] ids, java.lang.String[] select_fields, com.sugarcrm.www.sugarcrm.Link_name_to_fields_array[] link_name_to_fields_array, boolean track_view) throws java.rmi.RemoteException;
			 */
			
			com.sugarcrm.www.sugarcrm.Get_entry_result_version2 getRes = pt.get_entries(null, "contacts", null, null,arr, true);

			com.sugarcrm.www.sugarcrm.Entry_value[] evArr = getRes.getEntry_list();

			for (com.sugarcrm.www.sugarcrm.Entry_value evx : evArr) {
				System.out.println(evx.getId() + " " + evx.getModule_name());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
        
        /** 
		 * Rest Client
		 */
		
		try {
			
			Client client = ClientBuilder.newClient().register(new RestAuthenticator("admin", "fe01ce2a7fbac8fafaed7c982a04e229"));
	        WebTarget target = client.target(REST_URL);
	        target = target.queryParam("module_name", "contacts").queryParam("phone_work", "0987654321");
	        System.out.println(target.getUri().toString());
	
	        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
	        Response response = builder.get();
	        String list = response.readEntity(String.class);
	        System.out.println(""+list);
        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}

}
