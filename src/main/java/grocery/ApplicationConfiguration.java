package grocery;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Dropwizard Application configuration
 *
 * @author   Tomek Szpinda
 */
public class ApplicationConfiguration extends Configuration
{

    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory()
    {
        return dataSourceFactory;
    }

}
