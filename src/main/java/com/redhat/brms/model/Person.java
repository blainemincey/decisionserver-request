package com.redhat.brms.model;

/**
 * 
 * @author bmincey
 *
 */
public class Person implements java.io.Serializable {

	private static final long serialVersionUID = -7429286693506447246L;

	@org.kie.api.definition.type.Label("age")
	private java.lang.Integer age;

	@org.kie.api.definition.type.Label(value = "approved")
	private java.lang.Boolean approved;

	/**
	 * 
	 */
	public Person() {
	}

	/**
	 * 
	 * @param age
	 */
	public Person (java.lang.Integer age) {
		this.age = age;
	}
	
	/**
	 * 
	 * @param age
	 * @param approved
	 */
	public Person (java.lang.Integer age, java.lang.Boolean approved) {
		this.age = age;
		this.approved = approved;
	}

	/**
	 * 
	 * @return
	 */
	public java.lang.Integer getAge() {
		return this.age;
	}

	/**
	 * 
	 * @param age
	 */
	public void setAge(java.lang.Integer age) {
		this.age = age;
	}

	/**
	 * 
	 * @return
	 */
	public java.lang.Boolean getApproved() {
		return this.approved;
	}

	/**
	 * 
	 * @param approved
	 */
	public void setApproved(java.lang.Boolean approved) {
		this.approved = approved;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "Person [age=" + age + ", approved=" + approved + "]";
	}
}