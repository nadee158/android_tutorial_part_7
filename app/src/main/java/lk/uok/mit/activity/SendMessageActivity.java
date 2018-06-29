package lk.uok.mit.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lk.uok.mit.database.MyDataBaseOperations;
import lk.uok.mit.helloworld.HelloWorldActivity;
import lk.uok.mit.helloworld.R;
import lk.uok.mit.model.Message;

public class SendMessageActivity extends Activity {

    //declare the button at class level, once its initialized inside onCreate, it can be used in other methods as well
    private Button buttonSend = null;

    //decalae a variable to hold the context at the class level
    private Context context = null;

    //declare a variable at class level to hold the reference to the Contact Number autocomplete field
    private AutoCompleteTextView autoCompleteTextContactNumber = null;

    private int uniqueAppRequestCode = 1234;

    private String[] requiredPermissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};

    private MyDataBaseOperations dataBaseOperations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view of the activity
        setContentView(R.layout.activity_send_message);
        //initialize the context
        this.context = getApplicationContext();
        //initialize the autoCompleteTextContactNumber by geting reference
        this.autoCompleteTextContactNumber = findViewById(R.id.autoCompleteTextContactNumber);

        this.dataBaseOperations = new MyDataBaseOperations(context);

        //get if permission is granted or not to read contacts
        boolean hasPermission = this.hasUserPermissions(requiredPermissions);
        if (!(hasPermission)) {
            //if read contact permission is not already granted, request permission
            this.requestPermissions(requiredPermissions);
        } else {
            //call the setDataSourceForAutoComplete method we wrote to initialize the data source
            this.setDataSourceForAutoComplete();
        }


        //initialize the class variable before using it
        buttonSend = findViewById(R.id.buttonSend);

        //get the java reference to Message Text box
        EditText editTextMessage = findViewById(R.id.editTextMessage);
        // call the addTextChangedListener() method on editText and pass an annonymous inner class
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //inside the listner, when the user changes textvalidateinput
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //vall validateInputs method and get if the fields are valid or not
                boolean isFieldsValid = validateInputs();
                // if the fields are valid, enable the button
                if (isFieldsValid) {
                    buttonSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize the contactNumberText to a null value for now
                String contactNumberText = autoCompleteTextContactNumber.getText().toString();
                //get the java reference to Message Text box
                EditText editTextMessage = findViewById(R.id.editTextMessage);
                //get the current text from Message text box
                String messageText = editTextMessage.getText().toString();
                //get if permission is granted or not to read contacts
                boolean hasPermission = hasUserPermissions(requiredPermissions);
                if (!(hasPermission)) {
                    //if read contact permission is not already granted, request permission
                    requestPermissions(requiredPermissions);
                } else {
                    //call the setDataSourceForAutoComplete method we wrote to initialize the data source
                    boolean sentStatus = sendSMS(contactNumberText, messageText);
                    saveMessage(contactNumberText, messageText, sentStatus);
                }

                //create a new Intent by passing the context of the current Activity
                // and the class of the Next Activity we need to start as parameters
                Intent intent = new Intent(context, HelloWorldActivity.class);
                //add the data to be passed to the next Activity using putExtra method of the intent
                intent.putExtra("CONTACT_NUMBER", contactNumberText);
                intent.putExtra("MESSAGE", messageText);
                //using inherited startActivity method of the Activity class, start the HelloWorldActivity
                startActivity(intent);
            }
        });
    }

    private void saveMessage(String contactNumber, String messageText, boolean sentStatus) {
        Message message = new Message();
        message.setContactNumber(contactNumber);
        message.setMessageText(messageText);
        message.setSentStatus(sentStatus);
        message.setRetryCount((short) 1);
        message.setSentTime(Calendar.getInstance().getTime());
        this.dataBaseOperations.createMessage(message);
    }

    private boolean sendSMS(String contactNumber, String message) {
        boolean sentStatus = true;
        //include the code snippet in a try-catch block as this operation is likely to throw errors
        try {
            //i.	Get a reference to “android.telephony.SmsManager” class
//            SmsManager smsManager = SmsManager.getDefault();
//            //use the sendTextMessage method to send the SMS and pass the contact number and message as parameters
//            smsManager.sendTextMessage(contactNumber, null, message, null, null);
            //if code reached here, it means no exceptions occurred, notify the user of success
            Toast.makeText(this.context, "SMS Sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //if an exception occurs, set the sent status as false
            sentStatus = false;
            //if code reached here, it means an exception has occurred, notify the user of failure
            Toast.makeText(this.context, "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
            //print the error trace to fins the cause
            e.printStackTrace();
        }
        return sentStatus;
    }

    //a method to set the data source (adapter) to the autoCompleteTextContactNumber
    private void setDataSourceForAutoComplete() {
        //it takes three parameters to create an adapter
        //the context
        Context contextParameter = this.context;
        //the layout resource to define how to display a single contact number
        //here a built in resource of android is used
        int resourceParameter = android.R.layout.select_dialog_item;
        //data list to be displayed
        List<String> objectListParameter = this.getContactNumbers();
        //using above three parameters an Array adapter has been created
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contextParameter, resourceParameter, objectListParameter);
        //having declared as a class variable, autoCompleteTextContactNumber is visble to this method too
        //use setAdapter method to set the adapter to the view
        this.autoCompleteTextContactNumber.setAdapter(adapter);
        //Specifies the minimum number of characters the user has to type in the edit box before the drop down list is shown.
        this.autoCompleteTextContactNumber.setThreshold(1);
        //set the background color of drop down to a darker color so it can be easily seen
        this.autoCompleteTextContactNumber.setDropDownBackgroundResource(R.color.colorPrimaryDark);
    }

    //a method to get the contact number list of device as String List
    private List<String> getContactNumbers() {
        //i.	Create a new java.util.ArrayList of String
        List<String> contactNumberList = new ArrayList<String>();
        //write a query to the URI of the data kind representing a telephone number
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        //using the returned cursor from above query, move through each row by using cursor's moveToNext() method
        while (cursor.moveToNext()) {
            //check if the contact has a phone number using the column having an indicator of whether this contact has at least one phone number
            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (hasPhoneNumber > 0) {
                //if the contact has a phone number, get it
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (!(phoneNumber == null || phoneNumber.trim().equals(""))) {
                    //if the reteievd phone number is not null or an empty string, add it to the list
                    contactNumberList.add(phoneNumber);
                }
            }

        }
        //after reading through the result, close the cursor
        cursor.close();

        //iii.	Return the list
        return contactNumberList;
    }


    // Check whether user has all the passed permissions or not.
    private boolean hasUserPermissions(String[] permissions) {
        boolean ret = true;
        for (int i = 0; i < permissions.length; i++) {
            // return permission grant status.
            int hasPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i]);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ret = false;
            }
        }
        return ret;
    }


    // Request runtime permissions from app user.
    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(SendMessageActivity.this, permissions, this.uniqueAppRequestCode);
    }


    // After user select Allow or Deny button in request runtime permission dialog
    // , this method will be invoked.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //declare a variable of type boolean to hold if the user has granted the permission or not
        boolean hasGranted = true;
        //first check if the request code matched with our request
        if (requestCode == this.uniqueAppRequestCode) {
            //check if any results are granted
            int length = grantResults.length;
            if (length > 0) {
                //when permissions are available, iterate through them to check if granted
                for (int i = 0; i < length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        hasGranted = false;
                    }
                }
            }
            //if granted, show a notification accordingly
            if (hasGranted) {
                Toast.makeText(getApplicationContext(), "You allowed permission, please enter contact again.", Toast.LENGTH_LONG).show();
                setDataSourceForAutoComplete();
            } else {
                //if not granted, show a notification accordingly
                Toast.makeText(getApplicationContext(), "You denied permission.", Toast.LENGTH_LONG).show();
            }
        }
    }


    //this is to check if both contact number and a message is enetered
    private boolean validateInputs() {
        boolean isValid = true;
        //initialize the contactNumberText to a null value for now
        String contactNumberText = autoCompleteTextContactNumber.getText().toString();

        //if the text of contact number edittext is an empty value, set the valid status to false
        if (contactNumberText == null || contactNumberText.trim().isEmpty()) {
            isValid = false;
        }

        //get the java reference to Message Text box
        EditText editTextMessage = findViewById(R.id.editTextMessage);
        //get the current text from Message text box
        String messageText = editTextMessage.getText().toString();

        //if the text of Message edittext is an empty value, set the valid status to false
        if (messageText == null || messageText.trim().isEmpty()) {
            isValid = false;
        }

        return isValid;
    }
}