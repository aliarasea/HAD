/**
 * 
 */
package com.iuce.had;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

/**
 * @author AliAras
 *
 */
public class LoginActivity extends Activity implements OnClickListener {
	private Button btnAdd;
	private EditText txtUsername, txtPassword;
	private CheckBox chkRemember;
	public boolean bAuthenticating = false;
	public final Object mAuthenticationLock = new Object();
	private String TAG = "LoginActivity";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		chkRemember = (CheckBox) findViewById(R.id.checkRemember_login);
		txtUsername = (EditText) findViewById(R.id.txtUsername_login);
		txtPassword = (EditText) findViewById(R.id.txtUserPassword_login);
		btnAdd = (Button) findViewById(R.id.btnLogin_login);
		btnAdd.setOnClickListener(this);
		
		if(loadUserTokenCache(ServiceConstant.mClient))
		{
			Intent intent = new Intent(LoginActivity.this,DeviceScanActivity.class);
			startActivity(intent);
			finish();
		}
		
		ActionBar actionBar = getActionBar();

		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	private void cacheUserToken(MobileServiceUser user) {
		SharedPreferences prefs = getSharedPreferences(SharedPreferencesConstant.SHAREDPREFFILE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		try
		{
			editor.putString(SharedPreferencesConstant.USERIDPREF, user.getUserId());
			editor.putString(SharedPreferencesConstant.TOKENPREF, user.getAuthenticationToken());
			editor.commit();
		}
		catch(Exception e)
		{
			alert(TAG, e.toString());
		}
	}

	private boolean loadUserTokenCache(MobileServiceClient client)
	{
	    SharedPreferences prefs = getSharedPreferences(SharedPreferencesConstant.SHAREDPREFFILE, Context.MODE_PRIVATE);
	    String userId = prefs.getString(SharedPreferencesConstant.USERIDPREF, "undefined"); 
	    if (userId == "undefined")
	        return false;
	    String token = prefs.getString(SharedPreferencesConstant.TOKENPREF, "undefined"); 
	    if (token == "undefined")
	        return false;

	     ServiceConstant.user = new MobileServiceUser(userId);
	     ServiceConstant.user.setAuthenticationToken(token);
	     client.setCurrentUser(ServiceConstant.user);

	    return true;
	}

	private void authenticate() {
		
		LoginRequest login = new LoginRequest();
		login.setUsername(txtUsername.getText().toString());
		login.setPassword(txtPassword.getText().toString());
		ServiceConstant.mClient.invokeApi("CustomLogin", login, MobileServiceUser.class,
				new ApiOperationCallback<MobileServiceUser>() {
					@Override
					public void onCompleted(MobileServiceUser result,Exception exception, ServiceFilterResponse response) {
						if (exception == null) {
							try 
							{
								if (chkRemember.isChecked()) 
								{
									ServiceConstant.user = new MobileServiceUser(response.getContent());
									ServiceConstant.user.setAuthenticationToken(response.getContent());
									ServiceConstant.mClient.setCurrentUser(ServiceConstant.user);
									
									cacheUserToken(ServiceConstant.mClient.getCurrentUser());
									
									Log.i(TAG,"cache succeed");
								}
							} 
							catch (Exception e) 
							{
								Log.e(TAG, "cache fail");
							}
							
							Intent intent = new Intent(LoginActivity.this, DeviceScanActivity.class);
							startActivity(intent);
							finish();

						} else {
							Log.w(TAG, "giris basarisiz " + exception);
							alert(TAG, "Basarisiz\n" + response.getContent());
						}
					}
				});
	}
	
	
	
	private void alert(String title, String message) {
		AlertDialog.Builder alertMessage = new AlertDialog.Builder(this);
		alertMessage.setTitle(title);
		alertMessage.setMessage(message);
		alertMessage.setIcon(android.R.drawable.ic_dialog_alert);
		alertMessage.setPositiveButton("Tamam",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						closeContextMenu();
					}
				});
		alertMessage.show();
	}

	@Override
	public void onClick(View v) 
	{
		if (v == btnAdd) {
			try 
			{
				authenticate();
			} 
			catch (Exception e) 
			{
				alert("Yetkilendirme", "Hata:\n" + e);
			}

		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id==android.R.id.home)
		{
			 Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			 startActivity(intent);
			 finish();
			 return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}