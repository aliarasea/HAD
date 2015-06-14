package com.iuce.had;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;

public class ServiceConstant
{
	public static final String MOBILESERVICE_URL = "https://hadservice.azure-mobile.net/";
	public static final String MOBILESERVICE_APIKEY = "lRSGEboumxQOcyVZaKcHcwVgrvLESZ16";
	public static MobileServiceClient mClient;
	public static MobileServiceUser user;
	
	public static MobileServiceClient getClient(Context context)
	{
		try {
			mClient = new MobileServiceClient(MOBILESERVICE_URL,MOBILESERVICE_APIKEY,context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return mClient;
	}

	public static ArrayList<String> p;
}
