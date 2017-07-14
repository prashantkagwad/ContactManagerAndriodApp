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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.app.contactmanager.data.Contact;
import com.app.contactmanager.io.FileIO;

/**
 * @info : MainActivity class to display list on contacts store in the text
 *       file.
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// Suppress keyboard on start
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Read the file and get the contact list
		final FileIO fileIO = new FileIO();
		final ArrayList<Contact> contactList = fileIO.readFromFile();

		// Display the list on the screen.
		displayContacts(contactList);

		// If a search text is entered display new sorted list.
		final ArrayList<Contact> sortedContactList = new ArrayList<Contact>();
		final EditText searchEditText = (EditText) findViewById(R.id.Search);
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				int textLength = searchEditText.getText().length();

				// Clear the list every time a search is performed/text entered.
				sortedContactList.clear();
				for (int itr = 0; itr < contactList.size(); itr++) {

					boolean firstNameFlag = false, lastNameFlag = false;

					// Search for first name matching the search text.
					if (textLength <= contactList.get(itr).getFirstName()
							.length()) {

						if (searchEditText
								.getText()
								.toString()
								.equalsIgnoreCase(
										(String) contactList.get(itr)
												.getFirstName()
												.subSequence(0, textLength))) {

							firstNameFlag = true;
						}

					}

					// Search for last name matching the search text.
					if (textLength <= contactList.get(itr).getLastName()
							.length()) {

						if (searchEditText
								.getText()
								.toString()
								.equalsIgnoreCase(
										(String) contactList.get(itr)
												.getLastName()
												.subSequence(0, textLength))) {
							lastNameFlag = true;
						}
					}

					// If Search text present in either first name or last name
					// then store it.
					if (firstNameFlag || lastNameFlag) {
						sortedContactList.add(contactList.get(itr));
					}
				}

				// Finally display the listed of user that matched search text.
				displayContacts(sortedContactList);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// Depending on the menu item selected, change the intent.
		if (id == R.id.add) {

			// New intent for AddContact Page.
			Intent intent = new Intent(this, AddContact.class);
			startActivity(intent);

		} else if (id == R.id.info) {

			// New intent for Info Page.
			Intent intent = new Intent(this, Info.class);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	/**
	 * @function : displayContacts(contactList) - function to display all the
	 *           contact records from contact.txt file.
	 * @param : ArrayList<Contact> - array list of contact objects read from the
	 *        file.
	 */
	@SuppressLint("NewApi")
	public void displayContacts(final ArrayList<Contact> contactList) {

		// Create an intent to display a single user details.
		final Intent intentDisplayContact = new Intent(this,
				DisplayContact.class);

		// TableLayout in which the list of contacts are displayed.
		TableLayout tableLayout = (TableLayout) findViewById(R.id.contactList);
		tableLayout.removeAllViews();

		// Listener for the click on a single contact from the displayed list.
		OnClickListener tablerowOnClickListener = new OnClickListener() {
			public void onClick(View view) {

				int index = view.getId();
				Contact contact = contactList.get(index);

				int cID = contact.getCid();

				String firstName = "NULL";
				if (!(contact.getFirstName().isEmpty() || contact
						.getFirstName().equalsIgnoreCase(""))) {
					firstName = contact.getFirstName();
				}

				String lastName = "NULL";
				if (!(contact.getLastName().isEmpty() || contact.getLastName()
						.equalsIgnoreCase(""))) {
					lastName = contact.getLastName();
				}

				String phoneNumber = "NULL";
				if (!(contact.getPhoneNumber().isEmpty() || contact
						.getPhoneNumber().equalsIgnoreCase(""))) {
					phoneNumber = contact.getPhoneNumber();
				}

				String email = "NULL";
				if (!(contact.getEmail().isEmpty() || contact.getEmail()
						.equalsIgnoreCase(""))) {
					email = contact.getEmail();
				}

				String user = cID + "||" + firstName + "||" + lastName + "||"
						+ phoneNumber + "||" + email;

				// Toast.makeText(getBaseContext(), user, Toast.LENGTH_SHORT)
				// .show();

				// Send the contact details to the Display User Page.
				intentDisplayContact.putExtra("user", user);
				startActivity(intentDisplayContact);
			}
		};

		// Displaying each contact as a Table Row.
		Iterator<Contact> iterator = contactList.iterator();
		int count = 0;
		while (iterator.hasNext()) {

			// Alternating background color for contact list.
			int backgroundColor = getResources().getColor(R.color.grey);
			if (count % 2 == 0) {
				backgroundColor = getResources().getColor(R.color.white);
			}

			Contact contact = iterator.next();

			// Create a new inner TableLayout to hold two rows.
			TableLayout table = new TableLayout(this);
			TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
			table.setLayoutParams(layoutParams);
			table.setPadding(20, 20, 20, 20);
			table.setClickable(true);
			table.setId(count);
			table.setOnClickListener(tablerowOnClickListener);
			table.setBackgroundColor(backgroundColor);

			String contactName = contact.getFirstName() + " "
					+ contact.getLastName();
			SpannableString spanContactName = new SpannableString(contactName);
			spanContactName.setSpan(new StyleSpan(Typeface.BOLD), 0,
					spanContactName.length(), 0);
			spanContactName.setSpan(new StyleSpan(Typeface.ITALIC), 0,
					spanContactName.length(), 0);

			// Create a new TableRow to hold TextView that has contact name.
			TableRow rowName = new TableRow(this);
			rowName.setLayoutParams(layoutParams);

			// Create a new TextView to hold contact name.
			TextView textViewName = new TextView(this);
			textViewName.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			LayoutParams paramsViewName = (LayoutParams) textViewName
					.getLayoutParams();
			paramsViewName.height = 70;
			textViewName.setLayoutParams(paramsViewName);
			textViewName.setTextSize(20);
			textViewName.setText(spanContactName);
			textViewName.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.contact, 0, 0, 0);
			rowName.addView(textViewName);

			// int cID = contact.getCid();
			// TextView textViewID = new TextView(this);
			// textViewID.setLayoutParams(new LayoutParams(600,
			// android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			// LayoutParams params0 = (LayoutParams)
			// textViewID.getLayoutParams();
			// params0.height = 70;
			// textViewID.setLayoutParams(params0);
			// textViewID.setTextSize(0);
			// textViewID.setText(Integer.toString(cID));
			// rowName.addView(textViewID);

			String phoneNumber = contact.getPhoneNumber();
			SpannableString spanPhoneNumber = new SpannableString(phoneNumber);

			spanPhoneNumber.setSpan(new StyleSpan(Typeface.ITALIC), 0,
					spanPhoneNumber.length(), 0);

			// Create a new TableRow to hold TeaxtView that has contact number.
			TableRow rowPhone = new TableRow(this);
			rowPhone.setLayoutParams(layoutParams);

			// Create a new TextView to hold contact name.
			TextView textViewNumber = new TextView(this);
			textViewNumber.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			LayoutParams paramsViewNumber = (LayoutParams) textViewNumber
					.getLayoutParams();
			paramsViewNumber.height = 70;
			textViewNumber.setLayoutParams(paramsViewNumber);
			textViewNumber.setTextSize(20);
			textViewNumber.setText(spanPhoneNumber);
			textViewNumber.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.call, 0, 0, 0);
			rowPhone.addView(textViewNumber);

			// Add the new created rows to the TableLayout.
			table.addView(rowName);
			table.addView(rowPhone);

			// Add the TableLayout to the Global TableLayout at index count.
			tableLayout.addView(table, count);
			count++;
		}
	}
}
