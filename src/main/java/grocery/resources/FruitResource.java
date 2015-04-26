package grocery.resources;

import grocery.models.Fruit;
import grocery.persistence.FruitDao;
import io.dropwizard.hibernate.UnitOfWork;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @UnitOfWork(readOnly = true)
    public List<Fruit> findFruits()
    {
        return fruitDao.findAll();
    }
    
    @Path("/search")
    @GET
    @Timed
    @UnitOfWork(readOnly = true)
    public List<Fruit> findFruits(@QueryParam("fruitName") Optional<String> fruitName)
    {
        if(fruitName.isPresent())
            return fruitDao.findByName(fruitName.get());
        
        return fruitDao.findAll();
    }
    
    
    @Path("/{id}/price")
    @POST
    @Timed
    @UnitOfWork(readOnly = false)
    public Response updateFruitPrice(@PathParam("id") Long fruitId, BigDecimal price)
    {
        Optional<Fruit> optionalFruit = fruitDao.findById(fruitId);
        if (!optionalFruit.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Fruit fruit = optionalFruit.get();
        fruit.setPrice(price);
        fruitDao.update(fruit);
        
        return Response.ok(fruit).build();
    }
    
    @Path("/addOrOverride")
    @PUT
    @Timed
    @UnitOfWork(readOnly=false)
    public List<Fruit> addOrOverride(List<Fruit> fruits)
    {
        fruitDao.removeAll();
        return fruitDao.addAll(fruits);
    }
}
