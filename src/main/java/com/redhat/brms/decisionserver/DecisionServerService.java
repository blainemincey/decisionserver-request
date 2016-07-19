package com.redhat.brms.decisionserver;

/**
 * 
 */
import org.drools.core.runtime.impl.ExecutionResultImpl;

import org.kie.api.KieServices;

import org.drools.core.command.runtime.rule.AgendaGroupSetFocusCommand;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;

import org.kie.server.api.marshalling.Marshaller;
import org.kie.server.api.marshalling.MarshallerFactory;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;

import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;

import com.redhat.brms.model.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Purpose of class is to illustrate how to interact with the BRMS Decision Server.
 * This class can also be used to generate a "payload" that can be used in other tools.
 * 
 * @author bmincey
 *
 */
public class DecisionServerService {

	/**
	 * Credentials for Decision Server.  Be sure that the User has been granted the
	 * role of kie-server.
	 */
    private static final String SERVER_URL = "http://localhost:8080/kie-server/services/rest/server";
    private static final String USER_NAME  = "kieserver";
    private static final String USER_PWD   = "kieserver1!";

    /** Name of the container that was created in the Decision Server/BRMS. **/
    private static final String CONTAINER_ID = "MyContainer";
    private static final String KIE_SESSION  = "defaultStatelessKieSession";
    
    /** handle for returned object from decision server */
    private static final String PERSON_OUT_ID = "person";
    
    /** Constants for Marshalling Format */
    private static final String XSTREAM_FORMAT = "XSTREAM";
    private static final String JSON_FORMAT = "JSON";
    private static final String JAXB_FORMAT = "JAXB";

   /**
    * 
    * @param person
    * @return
    */
    public Person process(final Person person, final String marshallingFormatType) {
        
    	/** Could pass this in as argument. */
    	String service = CONTAINER_ID;
        
    	/** Create a set of the facts to pass to the Decision Server */
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add( Person.class );

        /** Configure the client to talk to the Decision Server */
        KieServicesConfiguration ksconf = 
        		KieServicesFactory.newRestConfiguration( SERVER_URL, USER_NAME, USER_PWD );
        
        /** 
         * How do you want to set the input/output from the REST service?  
         * Can be either JSON or XSTREAM or JAXB
         */
        if (marshallingFormatType != null && marshallingFormatType.length() > 0) {
	        if (marshallingFormatType.equalsIgnoreCase(DecisionServerService.JAXB_FORMAT)) {
	        	ksconf.setMarshallingFormat(MarshallingFormat.JAXB);
	        }
	        else if (marshallingFormatType.equalsIgnoreCase(DecisionServerService.JSON_FORMAT)) {
	        	ksconf.setMarshallingFormat(MarshallingFormat.JSON);
	        }
	        else if (marshallingFormatType.equalsIgnoreCase(DecisionServerService.XSTREAM_FORMAT)) {
	        	ksconf.setMarshallingFormat(MarshallingFormat.XSTREAM);
	        }
        }
        else {
        	
        	System.out.println("Null or empty Marshalling Format. Use JSON as default.");
        	
        	/** in the event of a null or empty string being passed, just a default of JSON */
        	ksconf.setMarshallingFormat(MarshallingFormat.JSON);
        }
        
        /** add the classes/facts to the ksconf */
        ksconf.addJaxbClasses( classes );
        Marshaller marshaller = MarshallerFactory.getMarshaller( classes, ksconf.getMarshallingFormat(), getClass().getClassLoader() );

        /** Grab handle to the client */
        KieServicesClient client = KieServicesFactory.newKieServicesClient( ksconf );
        KieCommands cf = KieServices.Factory.get().getCommands();
        RuleServicesClient ruleClient = client.getServicesClient( RuleServicesClient.class );

        /** batch the commands to be sent */
        List<Command<?>> commands = new ArrayList<Command<?>>();
        AgendaGroupSetFocusCommand agendaGroup = new AgendaGroupSetFocusCommand("MyAgendaGroup");
        commands.add(agendaGroup);
        BatchExecutionCommand batchExecution = cf.newBatchExecution( commands, KIE_SESSION );
        
        /** fact handle */
        Person p = new Person();
        p.setAge( person.getAge());

        /** add reference to return class and fire rules */
        commands.add( cf.newInsert( p, PERSON_OUT_ID ) );
        commands.add( cf.newFireAllRules() );

        /** marshall the commands */
        String marshalled = marshaller.marshall( batchExecution );
        
        /** Print out the payload that is being sent to the service */
        /** This output could be written to a file to be used in other tools to interact with service */
        System.out.println(marshalled);
        
        /** Response from service */
        ServiceResponse<String> reply = ruleClient.executeCommands( service, batchExecution );
        
        /** Grab the returned class */
        Person result = null;
        
        if( reply.getType() == ServiceResponse.ResponseType.SUCCESS ) {
        	System.out.println("=== Successful Response ===");
            ExecutionResults results = marshaller.unmarshall( reply.getResult(), ExecutionResultImpl.class );
            result = (Person) results.getValue( PERSON_OUT_ID );
        }

        /** return null object for any response other than Success */
        return result;
    }
}
