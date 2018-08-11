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
import pvytykac.net.scrape.server.facade.TaskQueue;
import pvytykac.net.scrape.server.facade.TaskResultProcessor;
import pvytykac.net.scrape.server.facade.TaskFacade;
import pvytykac.net.scrape.server.facade.IcoQueue;
import pvytykac.net.scrape.server.facade.impl.TaskQueueImpl;
import pvytykac.net.scrape.server.facade.impl.TaskResultProcessorImpl;
import pvytykac.net.scrape.server.facade.impl.TaskFacadeImpl;
import pvytykac.net.scrape.server.facade.impl.IcoQueueImpl;
import pvytykac.net.scrape.server.resources.TaskResource;

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
        IcoRepository icoRepository = new IcoRepositoryImpl(sessionFactory);

        IcoQueue icoQueue = new IcoQueueImpl(icoRepository, sessionManager);
        TaskQueue taskQueue = new TaskQueueImpl(icoQueue, configuration.getScrapeTaskConfiguration());

        TaskResultProcessor taskResultProcessor = new TaskResultProcessorImpl();
        TaskFacade taskFacade = new TaskFacadeImpl(taskQueue, taskResultProcessor);

        TaskResource taskResource = new TaskResource(taskFacade);

        environment.getApplicationContext().addBean(sessionFactory);
        environment.getApplicationContext().addBean(icoRepository);
        environment.getApplicationContext().addBean(sessionManager);

        environment.lifecycle().manage(icoQueue);

        environment.jersey().register(taskResource);
    }

}
