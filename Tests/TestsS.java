import org.junit.Assert;
import org.junit.Test;

public class TestsS {

    //Template
    @Test
    public void helloWorld(){
        String me = "Hello";
        me = me + " World";
        String hello = "Hello World";
        Assert.assertEquals(hello, me);
    }

}
