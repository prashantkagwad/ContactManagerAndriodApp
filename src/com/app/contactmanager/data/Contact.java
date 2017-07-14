/**
 * @Author - Prashant Kagwad
 * @Date - 10/01/2014
 * @Project Description : This application helps a user to save, edit and
 * delete a contact. It has a multiple screens on user interface side to accomplish this.
 * Along with contact name (first name & last name) a user can store phone number and email address of the contact.
 */

package com.app.contactmanager.data;

/**
 * @info : Contact class - helps in storing information about contact like first
 *       name, last name, phone number and email.
 */
public class Contact {

	private int cid;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
