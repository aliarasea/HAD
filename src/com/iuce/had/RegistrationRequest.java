package com.iuce.had;

import com.google.gson.annotations.SerializedName;
public class RegistrationRequest {

	@SerializedName("Id")
	public String Id;
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;
    @SerializedName("firstname")
    public String firstname;
    @SerializedName("lastname")
    public String lastname;
    @SerializedName("email")
    public String email;
    @SerializedName("gender")
    public boolean gender;
    @SerializedName("birthDate")
    public String birthDate;
    @SerializedName("weight")
    public int weight;
    @SerializedName("height")
    public int height;
}
