/**
 * 
 */
package com.iuce.had;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

/**
 * @author AliAras
 *
 */
public class MainActivity extends Activity implements OnClickListener {
    private Button btnLogin, btnSignup;
    private Intent intent;
    SharedPreferences sharedpreferences;
    private String TAG = "MainActivity";
	public boolean bAuthenticating = false;
	public final Object mAuthenticationLock = new Object();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initComponent();
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);      
        
        if(isNetworkAvailable())
        {
            try 
            {
    			ServiceConstant.mClient = new MobileServiceClient(ServiceConstant.MOBILESERVICE_URL, ServiceConstant.MOBILESERVICE_APIKEY, this);
    			Log.i(TAG, "baglanti basarili");  
    			
    		} 
            catch (Exception e) 
            {
                Log.i(TAG, "baglanti basarisiz "+e);
                alert(TAG, "baglanti basarisiz");
    		}
        }
        else
        {
        	alert(TAG, "internet baðlantýsý yok");
        }


	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private void initComponent()
	{
        btnLogin=(Button)findViewById(R.id.mBtnLogin);
        btnSignup=(Button)findViewById(R.id.mBtnSignup);
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
    public void onClick(View v) {
        if(v==btnLogin)
        {
        	intent = new Intent(MainActivity.this, LoginActivity.class);
        	startActivity(intent);
        	finish();

        }
        else if(v==btnSignup)
        {
            intent=new Intent(MainActivity.this,SignupActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
