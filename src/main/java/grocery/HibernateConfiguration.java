
package grocery;

import grocery.models.Fruit;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;



/**
 * Hibernate config
 *
 * @author   Tomek Szpinda
 */
public class HibernateConfiguration extends HibernateBundle<ApplicationConfiguration>
{
    protected HibernateConfiguration()
    {
        //list classes which do map to db tables
        super(Fruit.class);
    }

    @Override
    public DataSourceFactory getDataSourceFactory(ApplicationConfiguration configuration)
    {
        return configuration.getDataSourceFactory();
    }
}
