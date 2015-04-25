package grocery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import grocery.models.Fruit;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * App integration tests
 *
 * @author   Tomek Szpinda
 */
public class FruitIntegrationTest
{

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("grocery-test.yml");

    @ClassRule
    public static final DropwizardAppRule<ApplicationConfiguration> RULE = new DropwizardAppRule<>(Application.class, CONFIG_PATH);

    private Client client;

    @Before
    public void setUp() throws Exception
    {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception
    {
        client.close();
    }

    @Test
    public void testAddFruit() throws Exception
    {
        Fruit banana = new Fruit("banana", new BigDecimal("12.20"), 20, new Date());

        final Fruit newBanana = createFruitRequest(banana);
        assertNotNull(newBanana.getId());
        assertEquals(newBanana.getName(), banana.getName());
        assertEquals(newBanana.getPrice(), banana.getPrice());
        assertEquals(newBanana.getStock(), banana.getStock());
        assertNotNull(newBanana.getUpdated());
    }

    @Test
    public void testFindFruits() throws Exception
    {
        List<Fruit> list = findAllRequest("name");
        assertNotNull(list);
        int before = list.size();

        Fruit banana = new Fruit("banana", new BigDecimal("12.20"), 20, new Date());
        Fruit blueberry = new Fruit("blueberry", new BigDecimal("13.10"), 40, new Date());
        createFruitRequest(banana);
        createFruitRequest(blueberry);

        list = findAllRequest("name asc");
        assertEquals(2 + before, list.size());
        assertEquals("banana", list.get(0).getName());
    }

    private List<Fruit> findAllRequest(String sortOrder)
    {
        return clientTarget("fruits").queryParam("sortOrder", sortOrder).request().get(new GenericType<List<Fruit>>(){});
    }

    private Fruit createFruitRequest(Fruit banana)
    {
        return clientTarget("fruits").request().post(Entity.entity(banana, MediaType.APPLICATION_JSON_TYPE)).readEntity(Fruit.class);
    }

    private WebTarget clientTarget(String path)
    {
        return client.target("http://localhost:" + RULE.getLocalPort() + "/" + path);
    }
}
