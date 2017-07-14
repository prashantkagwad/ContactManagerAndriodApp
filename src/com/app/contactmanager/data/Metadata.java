/**
 * @Author - Prashant Kagwad
 * @Date - 10/01/2014
 * @Project Description : This application helps a user to save, edit and
 * delete a contact. It has a multiple screens on user interface side to accomplish this.
 * Along with contact name (first name & last name) a user can store phone number and email address of the contact.
 */

package com.app.contactmanager.data;

/**
 * @info : Metadata class - helps in storing metadata like max ID, etc. of the
 *       contacts stored in the text file.
 */
public class Metadata {

	private int maxCID;

	public Metadata() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getMaxCID() {
		return maxCID;
	}

	public void setMaxCID(int maxCID) {
		this.maxCID = maxCID;
	}

}
