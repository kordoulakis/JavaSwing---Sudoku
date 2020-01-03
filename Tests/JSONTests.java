import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class JSONTests {
    @Test
    public void testLoading() throws FileNotFoundException {
        JSONPuzzles puzzles = JSONPuzzles.deserializeFile();
        Assert.assertNotNull(puzzles);
    }

    @Test
    public void testPuzzleCreation(){
        JSONPuzzles.JSONPuzzle puzzle = new JSONPuzzles.JSONPuzzle();
        puzzle.setId(150);

        Assert.assertEquals(150,puzzle.getId(),0);
        Assert.assertNotNull(puzzle.getGrid());
    }
}
