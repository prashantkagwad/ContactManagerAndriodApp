/**
 * @Author - Prashant Kagwad
 * @Date - 10/01/2014
 * @Project Description : This application helps a user to save, edit and
 * delete a contact. It has a multiple screens on user interface side to accomplish this.
 * Along with contact name (first name & last name) a user can store phone number and email address of the contact.
 */

package com.app.contactmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TableRow;
import android.widget.Toast;

import com.app.contactmanager.data.Contact;
import com.app.contactmanager.io.FileIO;

/**
 * @info : DisplayContact class to display user details. This class has also
 *       methods to edit and delete a user.
 */
@SuppressLint("NewApi")
public class DisplayContact extends ActionBarActivity {

	private TableRow globalTableRowED;
	private TableRow globalTableRowCS;
	private Contact globalContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_contact);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// Extract the contact details from the Main Page.
		String user = (String) getIntent().getExtras().get("user");

		TableRow tableRowED = (TableRow) findViewById(R.id.tableRowButtonED);
		tableRowED.setVisibility(View.VISIBLE);
		globalTableRowED = tableRowED;

		TableRow tableRowCS = (TableRow) findViewById(R.id.tableRowButtonCS);
		tableRowCS.setVisibility(View.INVISIBLE);
		globalTableRowCS = tableRowCS;

		// Recreate the contact object sent from the main screen.
		final Contact contact = new Contact();
		StringTokenizer st = new StringTokenizer(user, "||");
		if (st.countTokens() == 5) {

			contact.setCid(Integer.parseInt(st.nextToken()));
			contact.setFirstName(checkString(st.nextToken()));
			contact.setLastName(checkString(st.nextToken()));
			contact.setPhoneNumber(checkString(st.nextToken()));
			contact.setEmail(checkString(st.nextToken()));

			displayContact(contact);
			globalContact = contact;

		} else {

			Toast.makeText(getBaseContext(), "Unable to find the contact !",
					Toast.LENGTH_SHORT).show();
		}

		// New intent for Main Page.
		final Intent intent = new Intent(this, MainActivity.class);
		Button buttonEdit = (Button) findViewById(R.id.buttonEdit);

		// Button click listener for edit button.
		buttonEdit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				editContact();
			}
		});

		Button buttonDelete = (Button) findViewById(R.id.buttonDelete);

		// Button click listener for delete button.
		buttonDelete.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				deleteContact(contact);
			}
		});

		Button buttonCancel = (Button) findViewById(R.id.buttonCancel);

		// Button click listener for cancel button.
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				cancel(contact);
			}
		});

		Button buttonSave = (Button) findViewById(R.id.buttonSave);

		// Button click listener for save button.
		buttonSave.setOnClickListener(new View.OnClickListener() {

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
						Toast.makeText(
								getBaseContext(),
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
					Toast.makeText(
							getBaseContext(),
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

					// Create a new contact object to add to the list.
					contact.setFirstName(firstName.getText().toString());
					contact.setLastName(lastName.getText().toString());
					contact.setPhoneNumber(phoneNumber.getText().toString());
					contact.setEmail(email.getText().toString());

					// Add and Write the contact to the file.
					if (saveContact(contact)) {

						// If the Write operation is successful, proceed.
						Toast.makeText(getBaseContext(),
								"Contact modified and saved successfully !",
								Toast.LENGTH_SHORT).show();
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.edit) {

			editContact();

		} else if (id == R.id.delete) {

			deleteContact(globalContact);

		} else if (id == R.id.back) {

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
			View rootView = inflater.inflate(R.layout.fragment_display_contact,
					container, false);
			return rootView;
		}
	}

	/**
	 * @function : displayContact(contact) - function to display the details of
	 *           a contact.
	 * @param : contact - contact object from which the details are shown
	 */
	public void displayContact(Contact contact) {

		try {

			// Display contact details.
			EditText firstName = (EditText) findViewById(R.id.editTextFirstName);
			firstName.setEnabled(false);
			firstName.setText(contact.getFirstName());

			EditText lastName = (EditText) findViewById(R.id.editTextLastName);
			lastName.setEnabled(false);
			lastName.setText(contact.getLastName());

			EditText phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
			phoneNumber.setEnabled(false);
			phoneNumber.setText(contact.getPhoneNumber());

			EditText email = (EditText) findViewById(R.id.editTextEmail);
			email.setEnabled(false);
			email.setText(contact.getEmail());

		} catch (Exception e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
	}

	/**
	 * @function : editContact() - function to edit the details of a contact.
	 */
	public void editContact() {

		try {

			EditText firstName = (EditText) findViewById(R.id.editTextFirstName);
			firstName.setEnabled(true);

			EditText lastName = (EditText) findViewById(R.id.editTextLastName);
			lastName.setEnabled(true);

			EditText phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
			phoneNumber.setEnabled(true);

			EditText email = (EditText) findViewById(R.id.editTextEmail);
			email.setEnabled(true);

			TableRow tableRowED = globalTableRowED;
			tableRowED.setVisibility(View.INVISIBLE);

			// Container contains all the rows, you could keep a variable
			// somewhere else to the container which you can refer to here
			ViewGroup container = ((ViewGroup) tableRowED.getParent());

			// Delete the row and invalidate your view so it gets redrawn
			// container.removeView(tableRowED);
			container.removeAllViews();
			container.invalidate();

			TableRow tableRowCS = globalTableRowCS;
			tableRowCS.setVisibility(View.VISIBLE);

			container.addView(tableRowCS);
			container.invalidate();

		} catch (Exception e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
	}

	/**
	 * @function : deleteContact(contact) - function to delete a contact.
	 * @param : contact - contact object to be deleted.
	 */
	public void deleteContact(final Contact contact) {

		final Intent intent = new Intent(this, MainActivity.class);

		// Create a dialog for delete confirmation.
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				DisplayContact.this);

		dialogBuilder.setMessage("Delete this contact?");
		dialogBuilder.setCancelable(true);
		dialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// If yes is clicked - delete user.
						FileIO fileIO = new FileIO();
						ArrayList<Contact> tempContactList = fileIO
								.readFromFile();
						Iterator<Contact> tempIterator = tempContactList
								.iterator();

						while (tempIterator.hasNext()) {

							Contact tempContact = tempIterator.next();

							if (tempContact.getCid() == contact.getCid()) {

								// undoUser = tempContact;
								tempIterator.remove();
							}
						}

						// Write the user list back to text file.
						fileIO.writeToFile(tempContactList);

						Toast.makeText(getBaseContext(), "Contact Deleted !",
								Toast.LENGTH_SHORT).show();
						dialog.cancel();
						startActivity(intent);
					}
				});
		dialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// If no is clicked - do nothing.
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.show();
	}

	/**
	 * @function : cancel(contact) - function to that simply cancels the edit
	 *           operations performed on the details of a contact.
	 * @param : contact - contact object from which the details are shown.
	 */
	public void cancel(Contact contact) {

		try {

			displayContact(contact);

			TableRow tableRowCS = globalTableRowCS;
			tableRowCS.setVisibility(View.INVISIBLE);

			// container contains all the rows, you could keep a variable
			// somewhere else to the container which you can refer to here
			ViewGroup container = ((ViewGroup) tableRowCS.getParent());

			// delete the row and invalidate your view so it gets redrawn
			container.removeAllViews();
			// container.removeView(tableRowCS);
			container.invalidate();

			TableRow tableRowED = globalTableRowED;
			tableRowED.setVisibility(View.VISIBLE);

			container.addView(tableRowED);
			container.invalidate();

		} catch (Exception e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
	}

	/**
	 * @function : saveContact(contact) - function to save the edited details of
	 *           a contact.
	 * @param : contact - contact object from which the details have to saved.
	 */
	public boolean saveContact(Contact contact) {

		try {

			final FileIO fileIO = new FileIO();
			final ArrayList<Contact> contactList = fileIO.readFromFile();

			Iterator<Contact> tempIterator = contactList.iterator();
			while (tempIterator.hasNext()) {
				Contact tempContact = tempIterator.next();

				if (tempContact.getCid() == contact.getCid()) {

					tempIterator.remove();
				}
			}

			// Write all the contact in the array list to the text file.
			contactList.add(contact);
			fileIO.writeToFile(contactList);

			// If file write operation works fine, return true.
			return true;
		} catch (Exception e) {
			// TODO Auto-generated method stub

			e.printStackTrace();
			// If file write operation failed, return false.
			return false;
		}
	}

	/**
	 * @function : checkString() - filter function which either returns the
	 *           original value or "" if NULL stored for that value in the text
	 *           file.
	 * @param : token - string to check for NULL/empty values.
	 * @return : string - "" if NULL else the original value.
	 */
	public String checkString(String token) {

		String checkText = "";

		if (!token.equalsIgnoreCase("NULL"))
			checkText = token;

		return checkText;
	}
}
