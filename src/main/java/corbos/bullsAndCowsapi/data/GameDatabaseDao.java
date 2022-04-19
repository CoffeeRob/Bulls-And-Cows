package corbos.bullsAndCowsapi.data;

import corbos.bullsAndCowsapi.models.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import corbos.bullsAndCowsapi.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Profile("database")
public class GameDatabaseDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game newGame() {
        Game game = new Game();
        final String sql = "INSERT INTO Game(GameStatus, answer) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, "In progress");
            statement.setString(2, game.createAnswer());
            return statement;
        }, keyHolder);
        game.setGameId(keyHolder.getKey().intValue());
        return game;
    }

    @Override
    public List<Game> getAll() {
        final String sql = "SELECT * FROM Game;";
        List<Game> games = jdbcTemplate.query(sql, new GameMapper());
        return games;

    }

    @Override
    public Game findById(int id) {

        final String sql = "SELECT GameId, GameStatus, Answer "
                + "FROM Game WHERE GameId = ?;";

        Game result = jdbcTemplate.queryForObject(sql, new GameMapper(), id);
        return result;
    }

    public List<Round> getRounds(int id) {
        final String sql = "SELECT Guess.GameId, Guess.GuessId, Guess.Attempt, Guess.Outcome, Guess.GuessTimeStamp" +
                           " FROM Game JOIN Guess ON Game.GameId = Guess.GameId WHERE Game.GameId = ?" +
                           " ORDER BY Guess.GuessTimeStamp;";

        List<Round> result = jdbcTemplate.query(sql, new RoundMapper(), id);
        return result;
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setGameId(rs.getInt("GameId"));
            round.setGuessId(rs.getInt("GuessId"));
            round.setAttempt(rs.getString("Attempt"));
            round.setOutcome(rs.getString("Outcome"));
            round.setGuessTimeStamp(rs.getString("GuessTimeStamp"));

            return round;
        }
    }

    @Override
    public Boolean isValid(Game result,String attempt){
        try {
            Integer.parseInt(attempt);
        } catch (NumberFormatException e) {
            return false;
        }
        Set resultSet = new HashSet();
        for (int i = 0; i < attempt.length(); i++) {
            resultSet.add(attempt.charAt(i));
        }
        if (resultSet.size() == 4 && attempt.length()==4){
            if (result.getStatus().equals("In progress")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Round updateRound(Game game, String outcome, String attempt) {
        String column1;
        String column2;
        if (outcome.equals("e:4:p:0")){
            game.setStatus("Finished");
            String gameSQL = "UPDATE Game SET "
                           + "Answer = ?, "
                           + "GameStatus = ? "
                           + "WHERE GameId = ?;";
            jdbcTemplate.update(gameSQL,game.getAnswer(),game.getStatus(),game.getGameId());

        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String timestamp = formatter.format(date);
        Round round = new Round();
        Integer GuessNo = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Guess WHERE gameId = ?",
                Integer.class, game.getGameId());

        if (Objects.isNull(GuessNo)){
            GuessNo = 0;
        }
        GuessNo ++;
        final int guessToPutIn = GuessNo;
        final String sql = "INSERT INTO Guess(GameId, Attempt, Outcome, GuessTimeStamp,GuessId) VALUES(?,?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, game.getGameId());
            statement.setString(2, attempt);
            statement.setString(3, outcome);
            statement.setString(4, timestamp);
            statement.setInt(5,guessToPutIn);
            return statement;
        }, keyHolder);
        round.setGameId(game.getGameId());
        round.setOutcome(outcome);
        round.setAttempt(attempt);
        round.setGuessTimeStamp(timestamp);
        round.setGuessId(GuessNo);
        return round;
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("GameId"));
            game.setStatus(rs.getString("GameStatus"));
            game.setAnswer(rs.getString("Answer"));
            return game;
        }
    }
}
