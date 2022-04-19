package corbos.bullsAndCowsapi.models;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Game {

    private int gameId;
    private String status;
    private String answer;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String createAnswer(){
        Boolean finished = false;
        Random rand = new Random();
        do {
            this.answer = String.valueOf(rand.nextInt(10000));
            Set resultSet = new HashSet();

            for (int i = 0; i < answer.length(); i++) {
                resultSet.add(answer.charAt(i));
            }
            if (resultSet.size() == 4){
                finished = true;
            }
        } while (!finished);
        return answer;
    }

    public String compare(String attempt){
        int exact = 0;
        int partial = 0;
        answer = getAnswer();
        for (int i =0; i<4; i++){
            if(attempt.charAt(i) == (answer.charAt(i))){
                exact += 1;
            }else{
                for (int j = 0; j < 4 ; j ++){

                    if(j!= i && attempt.charAt(i) == answer.charAt(j)) {
                        partial += 1;
                    }
                }
            }
        }
        return "e:" + exact + ":p:" + partial;
    }
}

