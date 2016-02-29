package com.redhat.brms.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.redhat.brms.decisionserver.DecisionServerService;
import com.redhat.brms.model.Person;

/**
 * 
 */

/**
 * @author bmincey
 *
 */
public class TestDecisionServerService {
	
	/** Property name pulled from system properties in pom.xml */
	private static final String MARSHALLING_FORMAT_PROPERTY = "MarshallingFormat";
	private static final String PERSON_AGE_PROPERTY = "PersonAge";
	
	/** This value is pulled from pom.xml and will be JSON, XSTREAM, or JAXB */
	private static String MARSHALLING_FORMAT;
	private static String PERSON_AGE;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		TestDecisionServerService.MARSHALLING_FORMAT =
				System.getProperty(TestDecisionServerService.MARSHALLING_FORMAT_PROPERTY);
		
		TestDecisionServerService.PERSON_AGE =
				System.getProperty(TestDecisionServerService.PERSON_AGE_PROPERTY);
	}

	/**
	 * Test method for {@link com.redhat.brms.decisionserver.DecisionServerService#process(com.redhat.brms.model.Person, java.lang.String)}.
	 */
	@Test
	public void testProcess() {
		
		DecisionServerService decisionServer = new DecisionServerService();
    	
		int age;
		
		try {
			age = Integer.parseInt(TestDecisionServerService.PERSON_AGE.trim());
		}
		catch (NumberFormatException nfe) {
			System.out.println("Invalid age in property. Using 21 as default age.");
			age = 21;			
		}
		
		System.out.println("   Age: " + age);
		System.out.println("Format: " + TestDecisionServerService.MARSHALLING_FORMAT);
		
    	Person person = new Person(age);
    	
    	Person result = 
    			decisionServer.process(person, TestDecisionServerService.MARSHALLING_FORMAT);
    	
    	if (result != null ) {
    		System.out.println(result);
    	}
    	
    	assertEquals(Boolean.TRUE, result.getApproved());
	}

}
