package pvytykac.net.scrape.server;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import io.dropwizard.validation.ValidationMethod;
import net.pvytykac.scrape.util.CollectionsUtil;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.server.service.ScrapeResultHandler;
import pvytykac.net.scrape.server.service.handlers.ResResultHandler;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class ScrapeTaskConfiguration {

    @NotNull
    private String scrapeType;

    private Set<Integer> supportedForms;

    @NotBlank
    private String handlerClass;

    @Valid
    @NotNull
    @NotEmpty
    private List<ScrapeStep> stepDefinitions;

    public String getScrapeType() {
        return scrapeType;
    }

    public Set<Integer> getSupportedForms() {
        return supportedForms;
    }

	public String getHandlerClass() {
		return handlerClass;
	}

	public List<ScrapeStep> getStepDefinitions() {
        return stepDefinitions;
    }

	@ValidationMethod(message = "class does not exist or doesn't implement ScrapeResultHandler")
    public boolean isHandlerClassValid() {
		if (StringUtils.isNotBlank(handlerClass)) {
			try {
				Class<?> clazz = Class.forName(handlerClass);
				return ScrapeResultHandler.class.isAssignableFrom(clazz);
			} catch (ClassNotFoundException ex) {
				return false;
			}
		}

		return true;
	}

    @ValidationMethod(message = "scrape task steps are not sequential")
    public boolean areStepDefinitionsSequential() {
        List<Integer> stepSequenceList = getStepSequenceList();

        return stepDefinitions == null
                || stepDefinitions.isEmpty()
                || CollectionsUtil.containsDuplicates(stepSequenceList)
                || CollectionsUtil.containsNulls(stepSequenceList)
                || CollectionsUtil.isSequential(stepSequenceList);
    }

    @ValidationMethod(message = "a scrape task steps have duplicate sequence numbers")
    public boolean areStepDefinitionSequenceNumbersUnique() {
        List<Integer> stepsSequenceList = getStepSequenceList();

        return stepDefinitions == null
                || stepDefinitions.isEmpty()
                || CollectionsUtil.containsNulls(stepsSequenceList)
                || !CollectionsUtil.containsDuplicates(stepsSequenceList);
    }

    private List<Integer> getStepSequenceList() {
        return stepDefinitions == null
                ? null
                : stepDefinitions.stream()
                    .map(ScrapeStep::getSequenceNumber)
                    .collect(Collectors.toList());
    }

}
