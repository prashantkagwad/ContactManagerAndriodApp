package com.app.contactmanager.io;

/**
 * @author : Prashant Kagwad
 * @info : FileIO class [Tech. Layer] - helps in reading/write records from/to the
 *         text file.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.app.contactmanager.data.Contact;
import com.app.contactmanager.data.Metadata;

@SuppressLint({ "NewApi", "DefaultLocale" })
public class FileIO {

	public FileIO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @function : readFromFile() - function to read all the contact records
	 *           from contact.txt file.
	 * @return : ArrayList<Contact> - array list of contact objects read from
	 *         the file.
	 */
	public ArrayList<Contact> readFromFile() {

		ArrayList<Contact> contacts = new ArrayList<Contact>();
		BufferedReader bufferedReader = null;
		String delimiter = "||";

		try {

			// Get the path for SD card.
			String path = Environment.getExternalStorageDirectory().getPath();
			// String path = "/sdcard";
			File myFile = new File(path + "/contacts.txt");

			// Create a input stream to read the data in.
			FileInputStream inputStream = new FileInputStream(myFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));

			// For each line read the store the contact info in a contact
			// object.
			String singleLine = "";
			while ((singleLine = bufferedReader.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(singleLine, delimiter);

				if (st.countTokens() == 5) {

					Contact contact = new Contact();
					contact.setCid(Integer.parseInt(st.nextToken()));
					contact.setFirstName(checkString(st.nextToken()));
					contact.setLastName(checkString(st.nextToken()));
					contact.setPhoneNumber(checkString(st.nextToken()));
					contact.setEmail(checkString(st.nextToken()));

					// Add the new object to the array list.
					contacts.add(contact);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}
		return contacts;
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

	/**
	 * @function : writeToFile(ArrayList<Contact>) - function to write all the
	 *           contact records to contact.txt file.
	 * @param : contacts - array list of contacts.
	 */
	public void writeToFile(ArrayList<Contact> contacts) {

		OutputStreamWriter writer = null;
		File file = null;
		FileOutputStream fileOutputStream = null;
		String delimiter = "||";
		try {

			// Get the path for SD card.
			String path = Environment.getExternalStorageDirectory().getPath();
			// String path = "/sdcard";
			file = new File(path + "/contacts.txt");

			// If file doesn't exists, then create it
			if (!file.exists()) {
				// file.mkdirs();
				file.createNewFile();
			}

			// Return if the user list is empty
			if (contacts.isEmpty() || contacts.size() <= 0) {
				return;
			}

			fileOutputStream = new FileOutputStream(file);
			writer = new OutputStreamWriter(fileOutputStream);

			// Sort the input array list before writing the file.
			Collections.sort(contacts, new Comparator<Contact>() {

				// Compared only on basics of first name
				@Override
				public int compare(Contact v1, Contact v2) {

					return v1.getFirstName().toLowerCase()
							.compareTo(v2.getFirstName().toLowerCase());
				}

			});

			// For contact object create a line and store the object in text
			// file.
			Iterator<Contact> iterator = contacts.iterator();
			while (iterator.hasNext()) {

				String tempContact = "";
				Contact contact = iterator.next();

				tempContact = tempContact + contact.getCid() + delimiter;
				tempContact = tempContact + contact.getFirstName() + delimiter;

				// Insert placeholder ("NULL") if lastname is empty.
				if (contact.getLastName().isEmpty()
						|| contact.getLastName().equalsIgnoreCase("")) {
					tempContact = tempContact + "NULL" + delimiter;
				} else {
					tempContact = tempContact + contact.getLastName()
							+ delimiter;
				}

				// Insert placeholder ("NULL") if phone number is empty.
				if (contact.getPhoneNumber().isEmpty()
						|| contact.getPhoneNumber().equalsIgnoreCase("")) {
					tempContact = tempContact + "NULL" + delimiter;
				} else {
					tempContact = tempContact + contact.getPhoneNumber()
							+ delimiter;
				}

				// Insert placeholder ("NULL") if email is empty.
				if (contact.getEmail().isEmpty()
						|| contact.getEmail().equalsIgnoreCase("")) {
					tempContact = tempContact + "NULL" + "\n";
				} else {
					tempContact = tempContact + contact.getEmail() + "\n";
				}
				writer.write(tempContact);
			}

			writer.close();
			fileOutputStream.close();

		} catch (Exception e) {

			// TODO Auto-generated method stub
			e.printStackTrace();

		} finally {
			try {
				writer.close();
				fileOutputStream.close();

			} catch (Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}
	}

	/**
	 * @function : printData(ArrayList<Contact>) - function to print details of
	 *           all the records read from contact.txt file.
	 * @param : contacts - array list of contacts.
	 */
	public void printData(ArrayList<Contact> contacts) {

		System.out.println("Printing records...");
		Iterator<Contact> iterator = contacts.iterator();

		int counter = 0;
		while (iterator.hasNext()) {

			counter++;
			System.out.println("User " + counter + " Information : ");

			Contact user = iterator.next();
			System.out.println("FirstName      : " + user.getFirstName());
			System.out.println("LastName       : " + user.getLastName());
			System.out.println("Phone Number   : " + user.getPhoneNumber());
			System.out.println("Email          : " + user.getEmail());
			System.out.println();
		}
	}

	/**
	 * @function : getMetaData() - function to print details of all the records
	 *           read from contact.txt file.
	 * @return : MetaData - object containing details regarding the text file.
	 */
	public Metadata getMetaData() {

		Metadata metaData = new Metadata();
		String delimiter = "||";
		BufferedReader bufferedReader = null;
		try {

			// Get the path for SD card.
			String path = Environment.getExternalStorageDirectory().getPath();
			// String path = "/sdcard";
			File myFile = new File(path + "/contacts.txt");

			// Create a input stream to read the data in.
			FileInputStream inputStream = new FileInputStream(myFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));

			int maxCID = 1000;
			String singleLine = "";

			// Get the max CID stored
			while ((singleLine = bufferedReader.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(singleLine, delimiter);

				if (st.countTokens() == 5) {

					int tempCID = Integer.parseInt(st.nextToken());
					if (tempCID > maxCID) {
						maxCID = tempCID;
					}
				}
			}
			metaData.setMaxCID(maxCID + 1);

		} catch (Exception e) {

			// TODO Auto-generated method stub
			e.printStackTrace();
		} finally {

			try {

				bufferedReader.close();

			} catch (Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}
		return metaData;
	}
}
