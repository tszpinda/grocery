package grocery.persistence;

import grocery.models.Fruit;
import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import jersey.repackaged.com.google.common.base.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Fruit dao
 *
 * @author   Tomek Szpinda
 */
@Repository
public class FruitDao extends AbstractDAO<Fruit>
{

    @Autowired
    public FruitDao(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public List<Fruit> findAll()
    {
        return list(namedQuery("grocery.Fruit.findAll"));
    }

    public Fruit add(Fruit fruit)
    {
        return persist(fruit);
    }

    public Optional<Fruit> findById(Long id)
    {
        return Optional.fromNullable(get(id));
    }
}