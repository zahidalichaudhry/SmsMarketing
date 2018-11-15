package com.example.thinkgeniux.sms_marketing.PojoClass;

public class ContactItem
{
    private String Contact_Id;
    private String Contact_Name;
    private String Group_Id;

    public ContactItem(String contact_Id, String contact_Name, String group_Id) {
        Contact_Id = contact_Id;
        Contact_Name = contact_Name;
        Group_Id = group_Id;
    }

    public String getContact_Id() {
        return Contact_Id;
    }

    public void setContact_Id(String contact_Id) {
        Contact_Id = contact_Id;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    public String getGroup_Id() {
        return Group_Id;
    }

    public void setGroup_Id(String group_Id) {
        Group_Id = group_Id;
    }
}
