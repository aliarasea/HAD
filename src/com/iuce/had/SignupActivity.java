/**
 * 
 */
package com.iuce.had;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

/**
 * @author AliAras
 *
 */
public class SignupActivity extends Activity implements OnClickListener {
	private String TAG = "SignupActivity: ";
//    private String username, password, firstname, lastname, email, gender, birthdate, height, weight;
    private Button btnSave;
    private RadioButton radiobtnWoman, radiobtnMan;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText txtUsername, txtPassword, txtFirstname, txtLastname, txtEmail, txtBirthDate, txtHeight,txtWeight;
	
    private String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    
    private void initElement() {
    	
        btnSave = (Button) findViewById(R.id.btnSave_signup);
        txtUsername = (EditText) findViewById(R.id.txtUsername_signup);
        txtPassword = (EditText) findViewById(R.id.txtPassword_signup);
        txtFirstname = (EditText) findViewById(R.id.txtFirstname_signup);
        txtLastname = (EditText) findViewById(R.id.txtLastname_signup);
        txtEmail = (EditText) findViewById(R.id.txtEmail_signup);
        txtBirthDate = (EditText)findViewById(R.id.txtBirthDate_signup);
        txtHeight = (EditText)findViewById(R.id.txtHeight_signup);
        txtWeight = (EditText)findViewById(R.id.txtWeight_signup);
        radiobtnWoman = (RadioButton)findViewById(R.id.radiobtn_woman_signup);
        radiobtnMan = (RadioButton)findViewById(R.id.radiobtn_man_signup);

    }
    
    public void signin()
    {
    	if(fieldControl())
    	{
    		 RegistrationRequest register = new RegistrationRequest();
             register.username=txtUsername.getText().toString();
             register.password=txtPassword.getText().toString();
             register.firstname=txtFirstname.getText().toString();
             register.lastname=txtLastname.getText().toString();
             register.email=txtEmail.getText().toString();
             register.birthDate=txtBirthDate.getText().toString();

             if(radiobtnMan.isChecked())
             {
            	 register.gender = true;
             }
             else
             {
            	 register.gender = false;
             }
             //register.birthDate= txtBirthDate.getText().toString();
             register.weight=Integer.parseInt(txtWeight.getText().toString());
             register.height = Integer.parseInt(txtHeight.getText().toString());
             
             ServiceConstant.mClient.invokeApi("CustomRegistration",register,RegistrationRequest.class,
                     new ApiOperationCallback<RegistrationRequest>() {
                         @Override
                         public void onCompleted(RegistrationRequest result, Exception exception, ServiceFilterResponse response) {
                            if (exception==null)
                            {
                                Log.w(TAG,"kayit basarili " +result);
                                Intent intent = new Intent(SignupActivity.this, DeviceScanActivity.class);
                                startActivity(intent);
                                finish();
                   
                            }
                             else
                            {
                                Log.e(TAG,"kayit basarisiz " +exception);
                                alert("Kayit Islemi","Basarisiz\n"+response.getContent());
                            }
                         }
                     });

    	}

    }
    
    private Boolean fieldControl()
    { 
    	if(txtUsername.getText().toString().equals("")||
    	   txtPassword.getText().toString().equals("")||
    	   txtFirstname.getText().toString().equals("")||
    	   txtLastname.getText().toString().equals("")||
    	   txtEmail.getText().toString().equals("")||
    	   txtBirthDate.getText().toString().equals("")||
    	   txtHeight.getText().toString().equals("")||
    	   txtWeight.getText().toString().equals(""))
    	{

    		alert("Zorunlu Alanlar","Tum Alanlari doldurunuz");
    		return false;
    	}
    	else
    	{
    		if(txtEmail.getText().toString().matches(EMAIL_REGEX))
    		{
    			return true;
    		}
    		else
    		{
    			alert(TAG, "Email alaný uygun formatta deðil.");
    			return false;
    		}
    	}
    }
    
    private void alert(String title,String message)
    {
		AlertDialog.Builder alertMessage = new AlertDialog.Builder(this);
		alertMessage.setTitle(title);
		alertMessage.setMessage(message);
		alertMessage.setIcon(android.R.drawable.ic_dialog_alert);
		alertMessage.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int which) {			      	
    	    	closeContextMenu();
    	    }
    	});
		alertMessage.show();
    }
    
    private void setDateTimeField() {

    	txtBirthDate.setOnClickListener(this);
        
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
 
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtBirthDate.setText(dateFormatter.format(newDate.getTime()));
            }
 
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
  
    }
    
       
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        initElement();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        setDateTimeField();
        btnSave.setOnClickListener(this);
        ActionBar actionBar = getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


    }
	
	@Override
	public void onClick(View v) {

		if (v==btnSave) 
		{
			signin();	
		}
		else if(v.equals(txtBirthDate))
		{
			fromDatePickerDialog.show();
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id==android.R.id.home)
		{
			 Intent intent = new Intent(SignupActivity.this,MainActivity.class);
			 startActivity(intent);
			 finish();
			 return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
