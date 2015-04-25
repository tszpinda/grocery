package grocery.health;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Health check endpoint
 *
 * @author   Tomek Szpinda
 */
@Service
public class DatabaseHealthCheck extends com.codahale.metrics.health.HealthCheck
{

    @Autowired
    SessionFactory sessionFactory;

    @Override
    protected Result check() throws Exception
    {
        Session currentSession = sessionFactory.openSession();
        try
        {
            currentSession.createQuery("select f from Fruit f").setMaxResults(1).list();
        }catch (Exception e) {
            return Result.unhealthy(e.getMessage());
        }
        finally
        {
            currentSession.close();
        }
        return Result.healthy();
    }

}
