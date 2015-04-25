
package grocery;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



/**
 * Spring App Config
 *
 * @author   Tomek Szpinda
 */
@Configuration
//list packages to scan by spring to discover beans
@ComponentScan({"grocery.persistence", "grocery.resources", "grocery.health"})
public class SpringConfiguration
{

    
    
}
