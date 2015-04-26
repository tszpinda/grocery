package grocery.persistence;

import grocery.models.Fruit;
import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
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

    /**
     * 
     * @return list of fruits ordered by update date
     */
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

    public void removeAll()
    {
        namedQuery("grocery.Fruit.deleteAll").executeUpdate();
    }

    public List<Fruit> addAll(List<Fruit> fruits)
    {
        for(Fruit fruit : fruits)
        {
            add(fruit);
        }
        return fruits;
    }

    public Fruit update(Fruit fruit)
    {
        return persist(fruit);
    }

    public List<Fruit> findByName(String fruitName)
    {
        return list(namedQuery("grocery.Fruit.findByName").setString("name", "%" + fruitName + "%"));
    }
}
