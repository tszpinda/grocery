package grocery;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.codahale.metrics.health.HealthCheck;

/**
 * Entry point
 *
 * @author   Tomek Szpinda
 */
public class Application extends io.dropwizard.Application<ApplicationConfiguration>
{

    private HibernateConfiguration hibernateConfiguration = new HibernateConfiguration();

    public static void main(String[] args) throws Exception
    {
        new Application().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap)
    {
        //hibernate configuration needs to be ready before anything else
        bootstrap.addBundle(hibernateConfiguration);
    }

    @Override
    public void run(ApplicationConfiguration config, Environment env) throws Exception
    {

        ApplicationContext configContext = createConfigContext(config);
        WebApplicationContext ctx = createWebContext(configContext);

        registerHealthChecks(env, ctx);
        registerResources(env, ctx);

        env.servlets().addServletListeners(new ContextLoaderListener(ctx));
    }

    /**
     * Main application context where resources and health checks going to be included 
     */
    private WebApplicationContext createWebContext(ApplicationContext configContext)
    {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setParent(configContext);
        ctx.register(SpringConfiguration.class);
        ctx.refresh();
        ctx.registerShutdownHook();
        ctx.start();
        return ctx;
    }

    /** 
     * @return - configuration context - containing hibernate session factory 
     * as it needs to be available before dao beans are created
     */
    private ApplicationContext createConfigContext(ApplicationConfiguration config)
    {
        AnnotationConfigWebApplicationContext configContext = new AnnotationConfigWebApplicationContext();
        configContext.refresh();
        configContext.getBeanFactory().registerSingleton("sessionFactory", hibernateConfiguration.getSessionFactory());
        configContext.getBeanFactory().registerSingleton("configuration", config);
        configContext.registerShutdownHook();
        configContext.start();
        return configContext;
    }

    private void registerResources(Environment env, ApplicationContext ctx)
    {
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        for (Entry<String, Object> entry : resources.entrySet())
        {
            env.jersey().register(entry.getValue());
        }
    }

    private void registerHealthChecks(Environment env, ApplicationContext ctx)
    {
        Map<String, HealthCheck> healthChecks = ctx.getBeansOfType(HealthCheck.class);
        for (Entry<String, HealthCheck> entry : healthChecks.entrySet())
        {
            env.healthChecks().register(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String getName()
    {
        return "Grocery";
    }
}
