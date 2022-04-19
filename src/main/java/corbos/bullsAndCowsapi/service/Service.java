package corbos.bullsAndCowsapi.service;

import corbos.bullsAndCowsapi.data.GameDatabaseDao;
import corbos.bullsAndCowsapi.models.Game;
import corbos.bullsAndCowsapi.models.Round;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private final GameDatabaseDao dao;

    @Autowired
    public Service(GameDatabaseDao dao) {this.dao = dao;}

    public List<Game> getAll() { return dao.getAll();}

    public Game findById(int id) { return dao.findById(id);}

    public Game newGame() { return dao.newGame();}

    public Boolean isValid(Game result,String attempt){ return dao.isValid(result,attempt);}

    public List<Round> getRounds(int id) { return dao.getRounds(id);}

    public Round updateRound(Game game, String outcome, String attempt) {
        return dao.updateRound(game, outcome, attempt);
    }

}
