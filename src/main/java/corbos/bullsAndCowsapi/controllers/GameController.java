package corbos.bullsAndCowsapi.controllers;
import corbos.bullsAndCowsapi.models.Round;
import corbos.bullsAndCowsapi.service.Service;
import corbos.bullsAndCowsapi.models.Game;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bullsAndCows")
public class GameController {

    private final Service service;

    public GameController(Service service) {
        this.service = service;
    }

    @GetMapping("/game")
    public List<Game> game() {

        List<Game> games = service.getAll();
        for (Game game:games){
            if(game.getStatus().equals("In progress")){
                game.setAnswer("Not available to see yet");
            }
        }
        return games;
    }

    @GetMapping("/game/{GameId}")
    public ResponseEntity<Game> game(@PathVariable int GameId) {
        Game result = service.findById(GameId);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        if(result.getStatus().equals("In progress")){
            result.setAnswer("Not available to see yet");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rounds/{GameId}")
    public ResponseEntity<List<Round>> rounds(@PathVariable int GameId) {
        Game result = service.findById(GameId);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        List<Round> response = service.getRounds(GameId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int begin() {

        Game thisGame =  service.newGame();
        return thisGame.getGameId();
    }

    @PostMapping("/guess/{GameId}")
    public ResponseEntity<Round> guess(@PathVariable int GameId, @RequestBody String attempt) {
        Game result = service.findById(GameId);
        Boolean valid = service.isValid(result, attempt);
        Round round;
        if (!valid) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        } else {
            String outcome = result.compare(attempt);
            round = service.updateRound(result, outcome, attempt);
        }
        if (result.getStatus().equals("In progress")) {
            result.setAnswer("Not available to see yet");
        }
        return ResponseEntity.ok(round);
    }
}
