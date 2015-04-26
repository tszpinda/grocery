package grocery.persistence;

import static org.junit.Assert.assertEquals;
import grocery.models.Fruit;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * FruitDao tests
 *
 * @author   Tomek Szpinda
 */
public class FruitDaoTest
{

    private static SessionFactory sessionFactory;
    private FruitDao dao;
    private Session session;

    @BeforeClass
    public static void setupSession()
    {
        Configuration config = new Configuration();
        config.setProperty("hibernate.connection.url", "jdbc:h2:mem:DbTest-" + System.currentTimeMillis());
        config.setProperty("hibernate.connection.username", "sa");
        config.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        config.setProperty("hibernate.current_session_context_class", "thread");
        config.setProperty("hibernate.hbm2ddl.auto", "create");
        config.addAnnotatedClass(Fruit.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        sessionFactory = config.buildSessionFactory(serviceRegistry);
    }

    @Before
    public void createSessionAndTransaction() throws Exception
    {
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        dao = new FruitDao(sessionFactory);

        addTestData();
    }

    private void addTestData()
    {
        Fruit banana = new Fruit("banana", new BigDecimal("12.20"), 20, today(0));
        Fruit mango = new Fruit("mango", new BigDecimal("12.30"), 21, today(-1));
        Fruit apple = new Fruit("apple", new BigDecimal("12.40"), 22, today(-2));
        dao.addAll(Arrays.asList(banana, mango, apple));
    }

    private Date today(int daysDiff)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, daysDiff);
        return cal.getTime();
    }

    @After
    public void tidyUp()
    {
        session.getTransaction().rollback();
    }

    @Test
    public void findAll()
    {
        List<Fruit> all = dao.findAll();
        assertEquals(3, all.size());

        //correct sort order (by updated date)
        assertEquals("apple", all.get(0).getName());
        assertEquals("banana", all.get(2).getName());
    }

    @Test
    public void findByName()
    {
        List<Fruit> all = dao.findByName("m");
        assertEquals(1, all.size());
        assertEquals("mango", all.get(0).getName());

        all = dao.findByName("x");
        assertEquals(0, all.size());
    }
}
