/**
 * @Author - Prashant Kagwad
 * @Date - 10/01/2014
 * @Project Description : This application helps a user to save, edit and
 * delete a contact. It has a multiple screens on user interface side to accomplish this.
 * Along with contact name (first name & last name) a user can store phone number and email address of the contact.
 */

package com.app.contactmanager;

import java.util.ArrayList;
import com.app.contactmanager.data.Contact;
import com.app.contactmanager.data.Metadata;
import com.app.contactmanager.io.FileIO;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @version : 1.0
 * @info : AddContact class to add users
 */
@SuppressLint("NewApi")
public class AddContact extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_contact);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// New intent for Main Page.
		final Intent intentMain = new Intent(this, MainActivity.class);
		Button buttonCancel = (Button) findViewById(R.id.buttonCancel);

		// Button click listener for cancel button.
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				startActivity(intentMain);
			}
		});

		Button buttonAddContact = (Button) findViewById(R.id.buttonAddButton);
		// Button click listener for add contact button.
		buttonAddContact.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Perform action on click

				// Check for first name validation and delimiter entry for each
				// field.
				Boolean firstNameFlag = false, lastNameFlag = false, phoneNumberFlag = false, emailFlag = false;

				// Get and check for first name.
				EditText firstName = (EditText) findViewById(R.id.editTextFirstName);
				String fName = firstName.getText().toString();
				if (!(fName.trim().isEmpty() || fName.trim().equalsIgnoreCase(
						""))) {

					if (!fName.contains("|")) {

						firstNameFlag = true;
					} else {
						firstName.requestFocus();
						Toast.makeText(getBaseContext(),
								"First name field cannot contain '|' character !",
								Toast.LENGTH_SHORT).show();
					}

				} else {

					firstName.requestFocus();
					Toast.makeText(getBaseContext(),
							"First name field cannot be empty !",
							Toast.LENGTH_SHORT).show();
				}

				// Get and check for last name.
				EditText lastName = (EditText) findViewById(R.id.editTextLastName);
				String lName = lastName.getText().toString();

				if (!lName.contains("|")) {
					lastNameFlag = true;
				} else {

					lastName.requestFocus();
					Toast.makeText(getBaseContext(),
							"Last name field cannot contain '|' character !",
							Toast.LENGTH_SHORT).show();
				}

				// Get and check for phone number.
				EditText phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
				String pNumber = phoneNumber.getText().toString();

				if (!pNumber.contains("|")) {
					phoneNumberFlag = true;
				} else {
					phoneNumber.requestFocus();
					Toast.makeText(getBaseContext(),
							"Phone number field cannot contain '|' character !",
							Toast.LENGTH_SHORT).show();
				}

				// Get and check for email.
				EditText email = (EditText) findViewById(R.id.editTextEmail);
				String em = email.getText().toString();

				if (!em.contains("|")) {
					emailFlag = true;
				} else {
					email.requestFocus();
					Toast.makeText(getBaseContext(),
							"Email field cannot contain '|' character !",
							Toast.LENGTH_SHORT).show();
				}

				if (firstNameFlag && lastNameFlag && phoneNumberFlag
						&& emailFlag) {

					// If all the fields are correctly entered, proceed.

					// Get the max contact ID count from MetaData object.
					FileIO fileIO = new FileIO();
					Metadata metaData = fileIO.getMetaData();
					int cID = metaData.getMaxCID();

					// Create a new contact object to add to the list.
					Contact newContact = new Contact();
					newContact.setCid(cID);
					newContact.setFirstName(firstName.getText().toString());
					newContact.setLastName(lastName.getText().toString());
					newContact.setPhoneNumber(phoneNumber.getText().toString());
					newContact.setEmail(email.getText().toString());

					// Add and Write the contact to the file.
					if (addContact(newContact)) {

						// If the Write operation is successful, proceed.
						Toast.makeText(getBaseContext(),
								"New contact added successfully!",
								Toast.LENGTH_SHORT).show();
						startActivity(intentMain);
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();

		if (id == R.id.back) {

			// New intent for AddContact Page.
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_contact,
					container, false);
			return rootView;
		}
	}

	/**
	 * @function : addContact(newContact) - function to add a new contact to the
	 *           text file.
	 * @param : newContact - new contact to be added to text file.
	 * @return : boolean - true if the contact was added successfully else
	 *         false.
	 */
	public boolean addContact(Contact newContact) {

		try {

			final FileIO fileIO = new FileIO();
			final ArrayList<Contact> contactList = fileIO.readFromFile();

			// Write all the contact in the array list to the text file.
			contactList.add(newContact);
			fileIO.writeToFile(contactList);

			// If file write operation works fine, return true.
			return true;
		} catch (Exception e) {
			// TODO Auto-generated method stub

			// e.printStackTrace();
			// If file write operation failed, return false.
			return false;
		}
	}
}
