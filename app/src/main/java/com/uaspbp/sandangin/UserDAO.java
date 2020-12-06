package com.uaspbp.sandangin;

import com.google.gson.annotations.SerializedName;

public class UserDAO
{
    @SerializedName("id")
    private String id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("email")
    private String email;

    @SerializedName("telpon")
    private String telpon;

    @SerializedName("password")
    private String password;

    public UserDAO(String id, String nama, String email, String telpon, String password)
    {
        this.id= id;
        this.nama = nama;
        this.email = email;
        this.telpon = telpon;
        this.password = password;
    }

    public String getId()
    {
        return id;
    }

    public String getNama()
    {
        return nama;
    }

    public String getEmail()
    {
        return email;
    }

    public String getTelpon()
    {
        return telpon;
    }

    public String getPassword()
    {
        return password;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setTelpon(String telpon)
    {
        this.telpon = telpon;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
