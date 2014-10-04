package mipsmob.tournup.models;

import com.parse.ParseUser;

import java.util.Date;

public class Tournament {

    private String id;
    private String name;
    private String type;
    private Date startTime;
    private String location;
    private ParseUser host;
    private String cost;
    private Boolean isPaid;
    private int maxPeople;

    public Tournament(String name, String type, Date startTime, String location, ParseUser host, String cost, Boolean isPaid, int maxPeople) {
        this.name = name;
        this.type = type;
        this.startTime = startTime;
        this.location = location;
        this.host = host;
        this.cost = cost;
        this.isPaid = isPaid;
        this.maxPeople = maxPeople;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getLocation() {
        return location;
    }

    public ParseUser getHost() {
        return host;
    }

    public String getCost() {
        return cost;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public boolean isPaid() {
        return isPaid;
    }

}
