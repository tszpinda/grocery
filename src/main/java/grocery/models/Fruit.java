package grocery.models;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Fruit Model
 *
 * @author   Tomek Szpinda
 */
@Entity
@NamedQueries(
{@NamedQuery(name = "grocery.Fruit.findAll", query = "from Fruit f order by f.updated asc"),
 @NamedQuery(name = "grocery.Fruit.deleteAll", query = "delete from Fruit"),
 @NamedQuery(name = "grocery.Fruit.findByName", query = "from Fruit f where f.name like :name order by f.updated asc"),
})
public class Fruit
{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @JsonProperty
    private String name;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private Integer stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty
    private Date updated;

    public Fruit()
    {
    }

    public Fruit(String name, BigDecimal price, Integer stock, Date updated)
    {
        super();
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.updated = updated;
    }

    @PreUpdate
    public void setLastUpdate()
    {
        this.updated = new Date();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public Integer getStock()
    {
        return stock;
    }

    public void setStock(Integer stock)
    {
        this.stock = stock;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated)
    {
        this.updated = updated;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((updated == null) ? 0 : updated.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Fruit other = (Fruit) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Fruit [id=" + id + ", name=" + name + ", price=" + price + ", stock=" + stock + ", updated=" + updated + "]";
    }

}
