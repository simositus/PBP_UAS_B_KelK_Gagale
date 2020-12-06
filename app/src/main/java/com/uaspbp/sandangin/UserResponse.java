package com.uaspbp.sandangin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse
{
    @SerializedName("data")
    @Expose
    private List<com.uaspbp.sandangin.UserDAO> users = null;

    @SerializedName("message")
    @Expose
    private String message;

    public List<com.uaspbp.sandangin.UserDAO> getUsers()
    {
        return users;
    }

    public String getMessage()
    {
        return message;
    }

    public void setUsers(List<com.uaspbp.sandangin.UserDAO> users)
    {
        this.users = users;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
