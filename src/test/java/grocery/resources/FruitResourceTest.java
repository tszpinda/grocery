package grocery.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import grocery.models.Fruit;
import grocery.persistence.FruitDao;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Resource test
 *
 * @author   Tomek Szpinda
 */
public class FruitResourceTest
{

    private static final FruitDao fruitDao = mock(FruitDao.class);

    @ClassRule
    public static final ResourceTestRule resources;
    static
    {
        FruitResource fruitResource = new FruitResource();
        fruitResource.fruitDao = fruitDao;

        resources = ResourceTestRule.builder().addResource(fruitResource).build();
    }

    private Fruit banana;

    @Before
    public void init()
    {
        banana = new Fruit("banana", new BigDecimal("12.20"), 20, new Date());
        when(fruitDao.findAll()).thenReturn(Arrays.asList(banana));
        when(fruitDao.add(any(Fruit.class))).thenReturn(banana);
    }

    @After
    public void cleanUp()
    {
        reset(fruitDao);
    }

    @Test
    public void testFindFruits()
    {
        List<Fruit> list = resources.client().target("/fruits").request().get(new GenericType<List<Fruit>>() {});
        assertEquals(1, list.size());
        assertEquals(banana, list.get(0));
        verify(fruitDao, times(1)).findAll();
    }
    
    @Test
    public void testAddFruits()
    {
        final Response response = resources.client().target("/fruits").request().post(Entity.entity(banana, MediaType.APPLICATION_JSON_TYPE));
        
        assertEquals(Response.Status.OK, response.getStatusInfo());
        
        ArgumentCaptor<Fruit> fruitCaptor = ArgumentCaptor.forClass(Fruit.class);
        verify(fruitDao, times(1)).add(fruitCaptor.capture());
        
        assertEquals(banana, fruitCaptor.getValue());
    }
}
