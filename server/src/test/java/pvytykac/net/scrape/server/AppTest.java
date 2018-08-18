package pvytykac.net.scrape.server;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.util.Optional;

import org.junit.ClassRule;

import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;

/**
 * @author Paly
 * @since 2018-08-11
 */
public abstract class AppTest {

    @ClassRule
    public static final DropwizardAppRule<ScrapeTaskDistributorConfiguration> RULE =
            new DropwizardAppRule<>(ScrapeTaskDistributorApplication.class, resourceFilePath("configuration.yaml"),
                    ConfigOverride.config("server.applicationConnectors[0].port", getFreePort()),
                    ConfigOverride.config("server.adminConnectors[0].port", getFreePort()));

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

    private static String getFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return String.valueOf(socket.getLocalPort());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

}
