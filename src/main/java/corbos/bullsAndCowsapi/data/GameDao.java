package corbos.bullsAndCowsapi.data;
import corbos.bullsAndCowsapi.models.Game;
import corbos.bullsAndCowsapi.models.Round;

import java.util.List;

public interface GameDao {

    Game newGame();

    List<Game> getAll();

    Game findById(int id);

    Round updateRound(Game game, String outcome, String attempt);

    Boolean isValid(Game result, String attempt);
}
