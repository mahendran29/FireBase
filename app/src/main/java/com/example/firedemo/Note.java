package com.example.firedemo;

public class Note
{
    private String title;
    private String description;

    public Note()
    {

    }

    Note(String title,String desc)
    {
        this.title=title;
        this.description=desc;
    }


    public String getTitle()
    {
        return title;
    }

    public  String getDescription()
    {
        return description;
    }
}
