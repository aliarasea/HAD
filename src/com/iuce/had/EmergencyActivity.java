package com.iuce.had;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

public class EmergencyActivity extends Activity {
	private String TAG = "EmergencyActivity";

	private MobileServiceTable<PhoneNumbers> mPhonesTable;
	private PhonesAdapter mAdapter;

	private EditText mTextNewPhone;

	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency);
		mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
		mProgressBar.setVisibility(ProgressBar.GONE);
		ServiceConstant.p = new ArrayList<String>();

		try {
			ServiceConstant.mClient = new MobileServiceClient(
					ServiceConstant.MOBILESERVICE_URL,
					ServiceConstant.MOBILESERVICE_APIKEY, this);
			Log.i(TAG, "baglanti basarili");
		} catch (Exception e) {
			Log.i(TAG, "baglanti basarisiz " + e);
			createAndShowDialog(e, TAG);
		}

		mPhonesTable = ServiceConstant.mClient.getTable(PhoneNumbers.class);

		mTextNewPhone = (EditText) findViewById(R.id.txtPhoneNumber);

		mAdapter = new PhonesAdapter(this, R.layout.row_list_phones);
		ListView listViewPhones = (ListView) findViewById(R.id.listViewPhones);
		listViewPhones.setAdapter(mAdapter);
		
		ActionBar actionBar = getActionBar();

		actionBar.setDisplayHomeAsUpEnabled(true);
		

		
	}

	
	public void addItem(View view) {
		if (ServiceConstant.mClient == null) {
			return;
		}

		final PhoneNumbers num = new PhoneNumbers();
		num.PhoneNumber = mTextNewPhone.getText().toString();
		ServiceConstant.p.add(mTextNewPhone.getText().toString());

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							mPhonesTable.insert(num, new TableOperationCallback<PhoneNumbers>() {

												@Override
												public void onCompleted(PhoneNumbers result,
														Exception exception,
														ServiceFilterResponse response) {
													if (exception == null) 
													{
														mAdapter.add(result);
														Log.i(TAG, "eklendi");
													} 
													else 
													{
														Log.i(TAG, "eklenmedi "+ response.getContent());
													}

												}
											});
						}
					});
				} catch (Exception e) {
					createAndShowDialog(e, "Error");

				}

				return null;
			}
		}.execute();

		mTextNewPhone.setText("");
	}

	private void refreshNumsFromTable() {
		
		
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							mPhonesTable.where().field("PhoneNumber").execute(new TableQueryCallback<PhoneNumbers>() {
								
								@Override
								public void onCompleted(final List<PhoneNumbers> result, int count,Exception exception, ServiceFilterResponse response) 
								{
									
				                    runOnUiThread(new Runnable() {
				                        @Override
				                        public void run() {
				                            mAdapter.clear();
				                            for(PhoneNumbers num : result)
				                            {
				                                mAdapter.add(num);
				                            }
				                        }
				                    });
									
								}
							});
						}
					});

				} catch (Exception e) {
					createAndShowDialog(e, "Error");
				}

				return null;
			}
		}.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emergency, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.phones_refresh) 
		{
			return true;
		}
		else if(id==android.R.id.home)
		{
			 onBackPressed();
			 finish();
			 return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if (exception.getCause() != null) {
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}

	private void createAndShowDialog(final String message, final String title) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
}
