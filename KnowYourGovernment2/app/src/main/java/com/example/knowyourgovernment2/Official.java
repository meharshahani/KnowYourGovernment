package com.example.knowyourgovernment2;

import java.io.Serializable;
import java.util.HashMap;

class Official implements Serializable
{
    private String url;
    private String email;
    private String officeName;

    private String name;
    private String address;
    private String party;
    private String phone;
    private HashMap<String, String> channels = new HashMap<>();

    public Official(){}

    public Official(String officeName)
    {
        this.officeName = officeName;
    }

    public Official(String officeName, String name)
    {
        this.name = name;
        this.officeName = officeName;
    }

    public Official(String officeName, String name, String url)
    {
        this.officeName = officeName;
        this.name = name;
        this.url = url;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void addChannelPair(String key, String value)
    {
        channels.put(key, value);
    }

    public HashMap<String,String> getChannels()
    {
        return channels;
    }

    public String getOfficeName()
    {
        return officeName;
    }

    public void setOfficeName(String officeName)
    {
        this.officeName = officeName;
    }

    public String getOfficialName()
    {
        return name;
    }
    public void setOfficialName(String officialName)
    {
        this.name = officialName;
    }

    public String getImageURL()
    {
        return url;
    }

    public void setImageURL(String imageURL)
    {
        this.url = imageURL;
    }


    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setParty(String party)
    {
        this.party = party;
    }

    public String getParty() {
        return party;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
}
