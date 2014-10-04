package mipsmob.tournup.models;

import com.parse.ParseUser;

import java.util.Date;

public class Tournament {

    private String id;
    private String type;
    private String name;
    private Date startTime;
    private String location;
    private ParseUser host;
    private String cost;
    private int maxPeople;

}
