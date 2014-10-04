package mipsmob.tournup.models;

import com.parse.ParseUser;

public class Player {

    private ParseUser user;
    private int score;

    public Player(ParseUser user, int score) {
        this.user = user;
        this.score = score;
    }

    public ParseUser getParseUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

}
