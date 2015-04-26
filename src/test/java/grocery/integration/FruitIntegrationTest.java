package grocery.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import grocery.Application;
import grocery.ApplicationConfiguration;
import grocery.JSONUtil;
import grocery.models.Fruit;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

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
        clearFruitData();
    }

    @After
    public void tearDown() throws Exception
    {
        client.close();
    }

    @Test
    public void addOrOverride_adds()
    {
        //given list of fruits to add
        //@formatter:off
        String json = 
                      "[{" +
                        " 'name' : 'banana'," +
                        " 'price' : 0.29," +
                        " 'stock' : 20," +
                        " 'updated' : '2014-01-02'" +
                        "}, {" +
                        " 'name' : 'melon'," +
                        " 'price' : 1.01," +
                        " 'stock' : 3," +
                        " 'updated' : '2014-03-28'" +
                        "}, {" +
                        " 'name' : 'apple'," +
                        " 'price' : 1.54," +
                        " 'stock' : 22," +
                        " 'updated' : '2014-02-05'" +
                        "}, {" +
                        " 'name' : 'pear'," +
                        " 'price' : 0.41," +
                        " 'stock' : 12," +
                        " 'updated' : '2014-04-19'" +
                        "}, {" +
                        " 'name' : 'kumquat'," +
                        " 'price' : 0.64," +
                        " 'stock' : 32," +
                        " 'updated' : '2014-06-10'" +
                        "}, {" +
                        " 'name' : 'orange'," +
                        " 'price' : 2.04," +
                        " 'stock' : 19," +
                        " 'updated' : '2014-05-25'" +
                        "}, {" +
                        " 'name' : 'lemon'," +
                        " 'price' : 1.56," +
                        " 'stock' : 9," +
                        " 'updated' : '2014-12-30'" +
                        "}]";
        //@formatter:on
        List<Fruit> fruits = jsonToFruitList(json);

        //when - add and override request sent
        List<Fruit> newFruits = addOrOverrideRequest(fruits);

        //then - new fruits returned
        assertEquals(fruits.size(), newFruits.size());
        assertNotNull(newFruits.get(0).getId());
    }

    @Test
    public void addOrOverride_overrides()
    {
        //given list of fruits exists
        //@formatter:off
        String json = 
                      "[{" +
                        " 'name' : 'banana'," +
                        " 'price' : 0.29," +
                        " 'stock' : 20," +
                        " 'updated' : '2014-01-02'" +
                        "}, {" +
                        " 'name' : 'kumquat'," +
                        " 'price' : 0.64," +
                        " 'stock' : 32," +
                        " 'updated' : '2014-06-10'" +
                        "}, {" +
                        " 'name' : 'lemon'," +
                        " 'price' : 1.56," +
                        " 'stock' : 9," +
                        " 'updated' : '2014-12-30'" +
                        "}]";
        //@formatter:on
        addOrOverrideRequest(jsonToFruitList(json));

        //when new list of fruits is given
        //@formatter:off
        String newJson = 
                "[{" +
                  " 'name' : 'banana'," +
                  " 'price' : 0.29," +
                  " 'stock' : 20," +
                  " 'updated' : '2014-01-02'" +
                  "}, {" +
                  " 'name' : 'orange'," +
                  " 'price' : 1.56," +
                  " 'stock' : 9," +
                  " 'updated' : '2014-12-30'" +
                  "}]";
        //@formatter:on

        //when - add and override request sent
        addOrOverrideRequest(jsonToFruitList(newJson));

        //then - only fruits from second request are returned
        List<Fruit> fruits = findAllRequest();
        assertEquals(2, fruits.size());
        assertEquals("banana", fruits.get(0).getName());
        assertEquals("orange", fruits.get(1).getName());
    }

    @Test
    public void testFindFruitsOrderedByUpdateDate() throws Exception
    {
        //given - some test data
        //@formatter:off
        String json = 
                      "[{" +
                        " 'name' : 'banana'," +
                        " 'price' : 0.29," +
                        " 'stock' : 20," +
                        " 'updated' : '2014-01-03'" +
                        "}, {" +
                        " 'name' : 'melon'," +
                        " 'price' : 1.01," +
                        " 'stock' : 3," +
                        " 'updated' : '2014-01-02'" +
                        "}, {" +
                        " 'name' : 'apple'," +
                        " 'price' : 1.54," +
                        " 'stock' : 22," +
                        " 'updated' : '2014-01-04'" +
                        "}]";
        //@formatter:on
        addOrOverrideRequest(jsonToFruitList(json));

        //when - find fruits API gets called
        List<Fruit> list = findAllRequest();

        //then:
        //- number of returned record is correct
        //- fruits are sorted in ascending order by updated date
        assertEquals(3, list.size());
        assertEquals("melon", list.get(0).getName());
        assertEquals("banana", list.get(1).getName());
        assertEquals("apple", list.get(2).getName());
    }

    @Test
    public void testUpdateFruitPrice() throws Exception
    {
        //given a fruit exists
        final Fruit banana = createFruitRequest(new Fruit("banana", new BigDecimal("12.20"), 20, new Date()));

        //when - we send update price request
        updateFruitPriceRequest(banana.getId(), new BigDecimal("13.99"));

        //then - price gets updated
        List<Fruit> list = findAllRequest();
        assertEquals(1, list.size());
        Fruit updatedBanana = list.get(0);
        assertEquals(banana.getId(), updatedBanana.getId());
        assertEquals(new BigDecimal("13.99"), updatedBanana.getPrice());
    }

    @Test
    public void testUpdateFruitPrice_updateInvalid() throws Exception
    {
        //given invalid fruit id
        long id = 9;

        //when - update to price occurs
        Fruit fruit = updateFruitPriceRequest(id, new BigDecimal("13.99"));

        //then - error is returned 
        assertNull("should have failed with 'Not Found error'", fruit);
    }

    @Test
    public void testSearchFruitsByName() throws Exception
    {
        //given - some test data
        //@formatter:off
        String json = 
                      "[{" +
                        " 'name' : 'banana'," +
                        " 'price' : 0.29," +
                        " 'stock' : 20," +
                        " 'updated' : '2014-01-03'" +
                        "}, {" +
                        " 'name' : 'melon'," +
                        " 'price' : 1.01," +
                        " 'stock' : 3," +
                        " 'updated' : '2014-01-02'" +
                        "}, {" +
                        " 'name' : 'blueberry'," +
                        " 'price' : 1.54," +
                        " 'stock' : 22," +
                        " 'updated' : '2014-01-04'" +
                        "}]";
        //@formatter:on
        addOrOverrideRequest(jsonToFruitList(json));

        //when - find fruits API gets called
        List<Fruit> list = searchByNameRequest("b");

        //then:
        //- number of returned records is correct
        //- fruits are sorted in ascending order by updated date
        assertEquals(2, list.size());
        assertEquals("banana", list.get(0).getName());
        assertEquals("blueberry", list.get(1).getName());
    }

    private List<Fruit> searchByNameRequest(String name)
    {
        return clientTarget("fruits/search").queryParam("fruitName", name).request().get(fruitListType());
    }

    private Fruit createFruitRequest(Fruit fruit)
    {
        return addOrOverrideRequest(Arrays.asList(fruit)).get(0);
    }

    private List<Fruit> findAllRequest()
    {
        return clientTarget("fruits").request().get(fruitListType());
    }

    private Fruit updateFruitPriceRequest(Long id, BigDecimal newPrice)
    {
        Response response = clientTarget("fruits/" + id + "/price").request().post(Entity.entity(newPrice, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
            return null;

        return response.readEntity(Fruit.class);
    }

    private List<Fruit> addOrOverrideRequest(List<Fruit> fruits)
    {
        return clientTarget("fruits/addOrOverride").request().put(Entity.entity(fruits, MediaType.APPLICATION_JSON_TYPE)).readEntity(fruitListType());
    }

    private WebTarget clientTarget(String path)
    {
        return client.target("http://localhost:" + RULE.getLocalPort() + "/" + path);
    }

    private List<Fruit> jsonToFruitList(String json)
    {
        return JSONUtil.deserializeJSON(json, new TypeReference<List<Fruit>>()
        {
        });
    }

    private GenericType<List<Fruit>> fruitListType()
    {
        return new GenericType<List<Fruit>>()
        {
        };
    }

    private void clearFruitData()
    {
        addOrOverrideRequest(new ArrayList<Fruit>());
    }

}
