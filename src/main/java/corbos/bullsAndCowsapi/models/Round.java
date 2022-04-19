package corbos.bullsAndCowsapi.models;

public class Round {
    private int GuessId;
    private int GameId;
    private String Attempt;
    private String Outcome;
    private String GuessTimeStamp;

    public int getGuessId() {
        return GuessId;
    }

    public void setGuessId(int guessId) {
        GuessId = guessId;
    }

    public int getGameId() {
        return GameId;
    }

    public void setGameId(int gameId) {
        GameId = gameId;
    }

    public String getAttempt() {
        return Attempt;
    }

    public void setAttempt(String attempt) {
        Attempt = attempt;
    }

    public String getOutcome() {
        return Outcome;
    }

    public void setOutcome(String outcome) {
        Outcome = outcome;
    }

    public String getGuessTimeStamp() {
        return GuessTimeStamp;
    }

    public void setGuessTimeStamp(String guessTimeStamp) {
        GuessTimeStamp = guessTimeStamp;
    }
}
