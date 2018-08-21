package pvytykac.net.scrape.server;

import org.hibernate.SessionFactory;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import pvytykac.net.scrape.server.db.SessionManager;
import pvytykac.net.scrape.server.db.model.Ico;
import pvytykac.net.scrape.server.db.model.res.ResAttribute;
import pvytykac.net.scrape.server.db.model.res.ResAttributeValue;
import pvytykac.net.scrape.server.db.model.res.ResForm;
import pvytykac.net.scrape.server.db.model.res.ResInstitution;
import pvytykac.net.scrape.server.db.model.res.ResRegion;
import pvytykac.net.scrape.server.db.model.res.ResUnit;
import pvytykac.net.scrape.server.db.repository.RepositoryFacade;
import pvytykac.net.scrape.server.resources.ScrapeTasksResource;
import pvytykac.net.scrape.server.resources.ScrapeTypesResource;
import pvytykac.net.scrape.server.service.IcoService;
import pvytykac.net.scrape.server.service.ScrapeResultService;
import pvytykac.net.scrape.server.service.ScrapeTaskService;
import pvytykac.net.scrape.server.service.ScrapeTypeService;
import pvytykac.net.scrape.server.service.TaskDistributionFacade;
import pvytykac.net.scrape.server.service.impl.IcoServiceImpl;
import pvytykac.net.scrape.server.service.impl.ScrapeResultServiceImpl;
import pvytykac.net.scrape.server.service.impl.ScrapeTypeServiceImpl;
import pvytykac.net.scrape.server.service.impl.TaskDistributionFacadeImpl;
import pvytykac.net.scrape.server.service.impl.TaskQueueImpl;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeTaskDistributorApplication extends Application<ScrapeTaskDistributorConfiguration> {

    private final HibernateBundle<ScrapeTaskDistributorConfiguration> hibernate = new HibernateBundle<ScrapeTaskDistributorConfiguration>(Ico.class,
            ResForm.class, ResRegion.class, ResUnit.class, ResInstitution.class, ResAttribute.class, ResAttributeValue.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ScrapeTaskDistributorConfiguration configuration) {
            return configuration.getDatabase();
        }
    };

    public static void main(String[] args) throws Exception {
        new ScrapeTaskDistributorApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ScrapeTaskDistributorConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ScrapeTaskDistributorConfiguration configuration, Environment environment) throws Exception {
        SessionFactory sessionFactory = hibernate.getSessionFactory();
        SessionManager sessionManager = new SessionManager(sessionFactory);
        RepositoryFacade repositoryFacade = new RepositoryFacade(sessionFactory);

        // services
        IcoService icoService = new IcoServiceImpl(repositoryFacade.getIcoRepository(), sessionManager);
        ScrapeTaskService scrapeTaskService = new TaskQueueImpl();
        ScrapeResultService scrapeResultService = new ScrapeResultServiceImpl(configuration.getScrapeTaskConfigurations(),
                repositoryFacade);
        ScrapeTypeService scrapeTypeService = new ScrapeTypeServiceImpl(configuration.getScrapeTaskConfigurations());

        // facades
        TaskDistributionFacade taskDistributionFacade = new TaskDistributionFacadeImpl(icoService, scrapeTypeService,
                scrapeTaskService, scrapeResultService);

        // resources
        environment.jersey().register(new ScrapeTasksResource(taskDistributionFacade));
        environment.jersey().register(new ScrapeTypesResource(scrapeTypeService));

        // bean registration
        environment.getApplicationContext().addBean(sessionFactory);
        environment.getApplicationContext().addBean(repositoryFacade);
        environment.getApplicationContext().addBean(sessionManager);

        // lifecycle managed
        environment.lifecycle().manage(icoService);
    }

}
