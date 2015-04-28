
package grocery.resources;

import static org.junit.Assert.fail;

import org.junit.Test;



/**
 * Test if wercker fails build if a test fails
 *
 * @author   Tomek Szpinda
 */
public class AFailureTest
{

    @Test
    public void failTest()
    {
        fail("Failing as expected");
    }
    
}
