package pvytykac.net.scrape.server;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;

import java.util.Optional;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class AppTest {

    @ClassRule
    public static final DropwizardAppRule<ScrapeTaskDistributorConfiguration> RULE =
            new DropwizardAppRule<>(ScrapeTaskDistributorApplication.class,
                    ResourceHelpers.resourceFilePath("configuration.yaml"));

    protected <T> Optional<T> getBean(Class<T> clazz) {
        T bean = RULE.getTestSupport()
                .getEnvironment()
                .getApplicationContext()
                .getBean(clazz);

        return Optional.ofNullable(bean);
    }

    protected <T> T requireBean(Class<T> clazz) {
        return getBean(clazz)
                .orElseThrow(() -> new IllegalStateException("Required bean '" + clazz.getName() + "' not found."));
    }

}
