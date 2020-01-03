import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class ClassicGridTests {

    @Test
    public void gridCreationTest() throws FileNotFoundException {
        ClassicGrid grid = new ClassicGrid();

        Assert.assertTrue(grid.getVisibility());
    }

    @Test
    public void controllerActionsTest() throws FileNotFoundException {
        ClassicController controller = new ClassicController(new ClassicGrid());

        Assert.assertTrue(controller.createGrid(9,9));
    }

    @Test
    public void setTextAtCellTest(){
        Cell cell = new Cell(true,1,1);
        cell.setText("1");

        Assert.assertEquals("1", cell.getText());
    }

    @Test
    public void setTextAtCellOnGridTest() throws FileNotFoundException {
        ClassicGrid me = new ClassicGrid();
        ClassicController c = (ClassicController) me.getController();

        Cell cell = c.getSelectableCellOnGrid();
        cell.setText("7");
        Assert.assertEquals("7",cell.getText());
    }
}
