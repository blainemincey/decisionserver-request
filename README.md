decisionServer-request : Demonstrates how to integrate with the Red Hat JBoss BRMS Decision Server.

This project is also useful in illustrating how to generate a payload that can be used to integrate with the Decision Server's REST API with other tools/code.

Steps to run:

1.  Be sure to have installed Red Hat JBoss BRMS.  This was done using BRMS 6.2.

2.  Go through the process of creating a BRMS project and upload the model class (Person.class) to the BRMS artifact repository.  You will need to add this artifact as a dependency to the project.  You may also have to add the model package to project package-names-whitelist file that is located in the repository of the project.

3.  Use the Person class in a rule that is something along the lines of:

import com.redhat.brms.model.Person;

<code>
rule "MyAgeVerification"
	dialect "mvel"
	no-loop true
	when
		person : Person( age >= 21 )
	then
		modify( person ) {
				setApproved( true )
		}
		System.out.println("Verified person is 21 years or older.");
end
</code>


4.  Follow the BRMS documentation to build and deploy this project.

5.  Create a KIE container for the Decision Server and a container for the project.  This is outlined in the BRMS documentation but also be sure to add the following System Properties to your standalone.xml for the BRMS server.  Some of these values are dependent on how you created your container.  Also, be sure to add an app user to the server with the role of kie-server.  If you are a Red Hat customer, this is documented in the KB article https://access.redhat.com/solutions/2106041.

<code>
<system-properties>
        <property name="org.kie.server.repo" value="${jboss.server.data.dir}"/>
        <property name="org.kie.example" value="true"/>
        <property name="org.jbpm.designer.perspective" value="ruleflow"/>
        <property name="org.kie.server.user" value="brmsAdmin"></property>
        <property name="org.kie.server.pwd" value="CHANGEME"></property>
        <property name="org.kie.server.location" value="http://localhost:8080/kie-server/services/rest/server"></property>
        <property name="org.kie.server.controller" value="http://localhost:8080/business-central/rest/controller"></property>
        <property name="org.kie.server.controller.user" value="kieServerUser"></property>
        <property name="org.kie.server.controller.pwd" value="CHANGEME"></property>
        <property name="org.jbpm.server.ext.disabled" value="true"></property>
        <property name="org.kie.server.id" value="myKieServer"></property>
    </system-properties>
</code>
    
6.  With the BRMS 6.2 Decision Server configured with the deployed project, you can run a 'mvn test' to the decisionserver-request.  You can change the property values within the pom.xml to get different results.  For example, you can change the age as well as what payload to use:  JSON, XSTREAM, or JAXB.

