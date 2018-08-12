package pvytykac.net.scrape.server;

import io.dropwizard.validation.ValidationMethod;
import net.pvytykac.scrape.util.CollectionsUtil;
import org.hibernate.validator.constraints.NotEmpty;
import pvytykac.net.scrape.model.v1.ScrapeStep;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class ScrapeTaskConfiguration {

    @NotNull
    private String scrapeType;

    private Set<Integer> supportedForms;

    @NotNull
    @NotEmpty
    @Valid
    private List<ScrapeStep> steps;

    public String getScrapeType() {
        return scrapeType;
    }

    public Set<Integer> getSupportedForms() {
        return supportedForms;
    }

    public List<ScrapeStep> getSteps() {
        return steps;
    }

    @ValidationMethod(message = "scrape task steps are not sequential")
    public boolean isStepsSequential() {
        List<Integer> stepSequenceList = getStepSequenceList();

        return steps == null
                || steps.isEmpty()
                || CollectionsUtil.containsDuplicates(stepSequenceList)
                || CollectionsUtil.containsNulls(stepSequenceList)
                || CollectionsUtil.isSequential(stepSequenceList);
    }

    @ValidationMethod(message = "a scrape task steps have duplicate sequence numbers")
    public boolean isStepsSequenceUnique() {
        List<Integer> stepsSequenceList = getStepSequenceList();

        return steps == null
                || steps.isEmpty()
                || CollectionsUtil.containsNulls(stepsSequenceList)
                || !CollectionsUtil.containsDuplicates(stepsSequenceList);
    }

    private List<Integer> getStepSequenceList() {
        return steps == null
                ? null
                : steps.stream()
                    .map(ScrapeStep::getSequenceNumber)
                    .collect(Collectors.toList());
    }

}
