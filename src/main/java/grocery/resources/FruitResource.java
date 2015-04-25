package grocery.resources;

import grocery.models.Fruit;
import grocery.persistence.FruitDao;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

/**
 * Rest end point
 *
 * @author   Tomek Szpinda
 */

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Service
public class FruitResource
{

    @Autowired
    FruitDao fruitDao;
    
    @GET
    @Timed
    @UnitOfWork(readOnly=true)
    public List<Fruit> findFruits(@QueryParam("sortOrder") Optional<String> sortOrder)
    {
        return fruitDao.findAll();
    }

    @POST
    @Timed
    @UnitOfWork(readOnly=false)
    public Fruit createFruit(Fruit fruit)
    {
        return fruitDao.add(fruit);
    }
}
