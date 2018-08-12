package pvytykac.net.scrape.server;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;
import pvytykac.net.scrape.server.db.IcoRepository;
import pvytykac.net.scrape.server.db.SessionManager;
import pvytykac.net.scrape.server.db.impl.IcoRepositoryImpl;
import pvytykac.net.scrape.server.db.model.Ico;
import pvytykac.net.scrape.server.resources.ScrapeTypeResource;
import pvytykac.net.scrape.server.service.ScrapeTaskService;
import pvytykac.net.scrape.server.service.ScrapeResultService;
import pvytykac.net.scrape.server.service.ScrapeTypeService;
import pvytykac.net.scrape.server.service.TaskDistributionFacade;
import pvytykac.net.scrape.server.service.IcoService;
import pvytykac.net.scrape.server.service.impl.ScrapeTypeServiceImpl;
import pvytykac.net.scrape.server.service.impl.TaskQueueImpl;
import pvytykac.net.scrape.server.service.impl.ScrapeResultServiceImpl;
import pvytykac.net.scrape.server.service.impl.TaskDistributionFacadeImpl;
import pvytykac.net.scrape.server.service.impl.IcoServiceImpl;
import pvytykac.net.scrape.server.resources.ScrapeTaskResource;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeTaskDistributorApplication extends Application<ScrapeTaskDistributorConfiguration> {

    private final HibernateBundle<ScrapeTaskDistributorConfiguration> hibernate = new HibernateBundle<ScrapeTaskDistributorConfiguration>(Ico.class) {
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

        // repositories
        IcoRepository icoRepository = new IcoRepositoryImpl(sessionFactory);

        // services
        IcoService icoService = new IcoServiceImpl(icoRepository, sessionManager);
        ScrapeTaskService scrapeTaskService = new TaskQueueImpl();
        ScrapeResultService scrapeResultService = new ScrapeResultServiceImpl();
        ScrapeTypeService scrapeTypeService = new ScrapeTypeServiceImpl(configuration.getScrapeTaskConfiguration());

        // facades
        TaskDistributionFacade taskDistributionFacade = new TaskDistributionFacadeImpl(icoService, scrapeTypeService,
                scrapeTaskService, scrapeResultService);

        // resources
        environment.jersey().register(new ScrapeTaskResource(taskDistributionFacade));
        environment.jersey().register(new ScrapeTypeResource(scrapeTypeService));

        // bean registration
        environment.getApplicationContext().addBean(sessionFactory);
        environment.getApplicationContext().addBean(icoRepository);
        environment.getApplicationContext().addBean(sessionManager);

        // lifecycle managed
        environment.lifecycle().manage(icoService);
    }

}
