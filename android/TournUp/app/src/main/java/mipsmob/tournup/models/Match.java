package mipsmob.tournup.models;

import com.parse.ParseUser;

public class Match {

    private String id;
    private ParseUser player1;
    private ParseUser player2;
    private String result;
    private ParseUser winner;

    public Match(String id, ParseUser player1, ParseUser player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setResult(String result, ParseUser winner) {
        this.result = result;
        this.winner = winner;
    }

    public String getId() {
        return id;
    }

    public ParseUser getPlayer1() {
        return player1;
    }

    public ParseUser getPlayer2() {
        return player2;
    }

    public String getResult() {
        return result;
    }

    public ParseUser getWinner() {
        return winner;
    }

}
