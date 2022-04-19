
import corbos.bullsAndCowsapi.TestApplicationConfiguration;
import corbos.bullsAndCowsapi.data.GameDao;
import corbos.bullsAndCowsapi.models.Game;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class tests extends TestCase {
    @Autowired
    GameDao dao;

    @Before
    public void setUp() throws Exception {
        List<Game> games = dao.getAll();

    }
    @Test
    public void test1() {
        Game game1 = new Game();
        game1.createAnswer();
        game1.setStatus("In progress");
        Boolean valid = dao.isValid(game1,game1.getAnswer());
        assertTrue(valid);
    }
    @Test
    public void test2() {
        Game game2 = new Game();
        game2.setAnswer("1234");
        String outcome = game2.compare("3298");
        assertEquals("e:1:p:1",outcome);
    }
    @Test
    public void test3() {
        Game game3 = new Game();
        game3.setAnswer("1123");
        assertFalse(dao.isValid(game3,game3.getAnswer()));
    }
    @Test
    public void test4() {
        Game game4 = new Game();
        game4.setAnswer("12345");
        assertFalse(dao.isValid(game4,game4.getAnswer()));
    }

    public void tearDown() throws Exception {
    }
}
