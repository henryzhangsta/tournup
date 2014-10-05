package mipsmob.tournup.models;

import com.parse.ParseUser;

public class Tournament {

    private String id;
    private String name;
    private String type;
    private String location;
    private ParseUser host;
    private String cost;
    private Boolean isPaid;
    private int maxPeople;

    public Tournament(String name, String type, String location, ParseUser host, String cost, Boolean isPaid, int maxPeople) {
        this.name = name;
        this.type = type;
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
