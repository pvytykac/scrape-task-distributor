package pvytykac.net.scrape.server.task.impl;

import static pvytykac.net.scrape.model.v1.enums.ExpectationType.BODY_CSS;
import static pvytykac.net.scrape.model.v1.enums.ExpectationType.HEADER;
import static pvytykac.net.scrape.model.v1.enums.ExpectationType.STATUS_CODE;
import static pvytykac.net.scrape.model.v1.enums.HttpMethod.GET;
import static pvytykac.net.scrape.model.v1.enums.HttpMethod.POST;
import static pvytykac.net.scrape.model.v1.enums.Operator.CONTAINS;
import static pvytykac.net.scrape.model.v1.enums.Operator.EQUALS;
import static pvytykac.net.scrape.model.v1.enums.Operator.NOT_BLANK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import pvytykac.net.scrape.model.v1.ClientException;
import pvytykac.net.scrape.model.v1.FailedExpectation;
import pvytykac.net.scrape.model.v1.Parameter;
import pvytykac.net.scrape.model.v1.ParameterType;
import pvytykac.net.scrape.model.v1.ScrapeDefinition;
import pvytykac.net.scrape.model.v1.ScrapeExpectation;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;
import pvytykac.net.scrape.model.v1.enums.Type;
import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.model.res.ResAttribute;
import pvytykac.net.scrape.server.db.model.res.ResAttributeValue;
import pvytykac.net.scrape.server.db.model.res.ResForm;
import pvytykac.net.scrape.server.db.model.res.ResInstitution;
import pvytykac.net.scrape.server.db.model.res.ResRegion;
import pvytykac.net.scrape.server.db.model.res.ResUnit;
import pvytykac.net.scrape.server.db.repository.impl.RepositoryFacade;
import pvytykac.net.scrape.server.util.HtmlDocument;
import pvytykac.net.scrape.server.util.HtmlDocument.HtmlTable;
import pvytykac.net.scrape.server.util.HtmlDocument.HtmlTableRow;

public class ResTaskType extends AbstractTaskType {

	private static final Logger LOG = LoggerFactory.getLogger(ResTaskType.class);

	public ResTaskType(String id, RepositoryFacade facade, String offsetIco) {
		super(id, facade, offsetIco);
	}

	@Override
	public Set<Integer> getApplicableFormIds() {
		return Collections.emptySet();
	}

	@Override
	protected boolean isKnownIco(Ico ico) {
		return ico.getResId() != null;
	}

	@Override
	protected boolean isResultResponseValid(ScrapeResult result) {
		return result.getStatusCode() == 200;
	}

	@Override
	public Status onClientError(ClientException error, ScrapeStep step) {
		return new Status(null, false);
	}

	@Override
	public Status onExpectationsFailed(List<FailedExpectation> errors, ScrapeStep step) {
		Long timeout = errors.stream()
				.mapToLong(expec -> getTimeout(expec.getExpectation().getId(), expec.getActual()))
				.filter(Objects::nonNull)
				.max()
				.orElse(0L);

		boolean retry = errors.stream()
				.anyMatch(expec -> isRetry(expec.getExpectation().getId(), expec.getActual()));

		return new Status(timeout > 0L ? timeout : null, retry);
	}

	@Override
	protected Status onInvalidResult(ScrapeResult result) {
		return new Status(0L, false);
	}

	@Override
	public Status onSuccess(ScrapeResult result) {
		HtmlDocument document = new HtmlDocument(result.getPayload());

		Integer id = document.getFormByName("form_detail")
				.getInputByName("vypis")
				.attrOptional("onclick")
				.map(ResTaskType::findId)
				.orElse(null);

		HtmlTable idTable = document.getTableBySummary("identifikace");
		String ico = idTable.selectRowAndColumn(1, 3).select("strong").text();
		String name = idTable.selectRowAndColumn(2, 3).select("strong").text();

		Optional<String> form = idTable.selectRowAndColumn(3, 3).textOptional();
		String formId = form.map(str -> str.substring(0, str.indexOf('-')).trim()).orElse(null);
		String formName = form.map(str -> str.substring(str.indexOf('-') + 1).trim()).orElse(null);

		HtmlTable dateTable = document.getTableBySummary("vznik a zanik");
		DateTime created = dateTable.selectRowAndColumn(1, 3).textAsDate("dd.MM.yyyy");
		DateTime ceased = dateTable.selectRowAndColumn(2, 3).textAsDate("dd.MM.yyyy");

		HtmlTable addressTable = document.getTableBySummary("adresa");
		String address = addressTable.selectRowAndColumn(1, 3).text();

		HtmlTable codeTable = document.getTableBySummary("adresa-kody");
		String regionCode = codeTable.selectRowAndColumn(1, 3).text();
		String region = codeTable.selectRowAndColumn(1, 5).text();
		String unitCode = codeTable.selectRowAndColumn(2, 3).text();
		String unit = codeTable.selectRowAndColumn(2, 5).text();

		Map<String, ResAttribute> attributes = new HashMap<>();
		List<ResAttributeValue> attributeValues = new ArrayList<>();
		String attrId = null;
		String attrName = null;
		List<HtmlTableRow> attributeRows = document.getTableBySummary("atributy").getRows(1);
		for (HtmlTableRow attributeRow: attributeRows) {
			if (attributeRow.selectColumn(1).select("a").hasText()) {
				attrName = attributeRow.selectColumn(1).text();
				attrId = attributeRow.selectColumn(1).select("a").attr("href");
				attrId = attrId.substring(attrId.lastIndexOf('=') + 1);
				attributes.putIfAbsent(attrId, new ResAttribute.Builder()
						.withId(attrId)
						.withText(attrName)
						.build());
			}

			String code = attributeRow.selectColumn(2).text();
			String text = attributeRow.selectColumn(3).text();

			attributeValues.add(new ResAttributeValue.Builder()
					.withCode(code)
					.withAttribute(attributes.get(attrId))
					.withText(text)
					.build());
		}

		ResInstitution institution = new ResInstitution.Builder()
				.withId(id)
				.withIco(ico)
				.withName(name)
				.withAddress(address)
				.withCreated(created)
				.withCeased(ceased)
				.withForm(new ResForm.Builder()
						.withId(formId)
						.withText(formName)
						.build())
				.withRegion(new ResRegion.Builder()
						.withId(regionCode)
						.withText(region)
						.build())
				.withUnit(new ResUnit.Builder()
						.withId(unitCode)
						.withText(unit)
						.build())
				.withAttributes(attributeValues)
				.build();

		facade.getIcoRepository().updateFromRes(ico, formId == null ? null : Integer.valueOf(formId), id);
		facade.getResRepository().save(institution);

		LOG.info("Successfully scraped RES institution '{}'", ico);
		return new Status(0L, false);
	}

	@Override
	protected ScrapeTask createTaskForNewIco(String ico) {
		Map<String, Parameter> parameters = ImmutableMap.of("ico", new Parameter.Builder()
				.withType(new ParameterType.Builder()
						.withRootType(Type.STRING)
						.build())
				.withValue(ico)
				.build());

		return new ScrapeTask.ScrapeTaskBuilder()
				.withTaskUuid(UUID.randomUUID().toString())
				.withTaskType(getId())
				.withParameters(parameters)
				.addStep(new ScrapeStep.ScrapeStepBuilder()
						.withSequenceNumber(1)
						.withMethod(POST)
						.withUri("http://apl.czso.cz/irsw/hledat.jsp")
						.withContentType("application/x-www-form-urlencoded")
						.withPayload("ico=${ico}&nazev=&forma=&okres=&texttype=0&zanik=0&run_rswquery=Hledej")
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(1)
								.withType(STATUS_CODE)
								.withExpectedValue("200")
								.withOperator(EQUALS)
								.withExpected(true)
								.build())
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(2)
								.withType(HEADER)
								.withTarget("Content-Type")
								.withExpectedValue("text/html")
								.withOperator(CONTAINS)
								.withExpected(true)
								.build())
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(3)
								.withType(BODY_CSS)
								.withTarget("a[href^='detail.jsp?prajed_id=']")
								.withOperator(NOT_BLANK)
								.withExpected(true)
								.build())
						.addScrape(new ScrapeDefinition.Builder()
								.withScrapeType(ScrapeType.ELEMENT)
								.withSelector("a[href^='detail.jsp?prajed_id=']")
								.withSubDefinitions(Collections.singletonList(new ScrapeDefinition.Builder()
										.withScrapeType(ScrapeType.ATTRIBUTE)
										.withSelector("href")
										.withStoreAs("detailHref")
										.build()))
								.build())
						.build())
				.addStep(new ScrapeStep.ScrapeStepBuilder()
						.withSequenceNumber(2)
						.withMethod(GET)
						.withUri("http://apl.czso.cz/irsw/${detailHref}")
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(4)
								.withType(STATUS_CODE)
								.withExpectedValue("200")
								.withOperator(EQUALS)
								.withExpected(true)
								.build())
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(5)
								.withType(HEADER)
								.withTarget("Content-Type")
								.withExpectedValue("text/html")
								.withOperator(CONTAINS)
								.withExpected(true)
								.build())
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(6)
								.withType(BODY_CSS)
								.withTarget("table[summary='identifikace'] tr:nth-child(1) td:nth-child(3)")
								.withOperator(NOT_BLANK)
								.withExpected(true)
								.build())
						.build())
				.build();
	}

	@Override
	protected ScrapeTask createTaskForKnownIco(Ico ico) {
		Map<String, Parameter> parameters = ImmutableMap.of("ico", new Parameter.Builder()
				.withType(new ParameterType.Builder()
						.withRootType(Type.STRING)
						.build())
				.withValue(ico.getId())
				.build());

		return new ScrapeTask.ScrapeTaskBuilder()
				.withTaskUuid(UUID.randomUUID().toString())
				.withTaskType(getId())
				.withParameters(parameters)
				.addStep(new ScrapeStep.ScrapeStepBuilder()
						.withSequenceNumber(1)
						.withMethod(GET)
						.withUri("http://apl.czso.cz/irsw/detail.jsp?prajed_id=${id}")
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(4)
								.withType(STATUS_CODE)
								.withExpectedValue("200")
								.withOperator(EQUALS)
								.withExpected(true)
								.build())
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(5)
								.withType(HEADER)
								.withTarget("Content-Type")
								.withExpectedValue("text/html")
								.withOperator(CONTAINS)
								.withExpected(true)
								.build())
						.addExpectation(new ScrapeExpectation.ScrapeExpectationBuilder()
								.withId(6)
								.withType(BODY_CSS)
								.withTarget("table[summary='identifikace'] tr:nth-child(1) td:nth-child(3)")
								.withOperator(NOT_BLANK)
								.withExpected(true)
								.build())
						.build())
				.build();
	}

	private static long getTimeout(Integer expectationId, String actual) {
		switch (expectationId) {
			case 1:
			case 4:
				return "404".equals(actual) ? 200L : 0L;
			default: return 0L;
		}
	}

	private static boolean isRetry(Integer expectationId, String actual) {
		switch (expectationId) {
			case 1:
			case 4:
				return "404".equals(actual);
			default: return false;
		}
	}

	private static Integer findId(String str) {
		String prefix = "prajed_id=";
		int start = str.indexOf(prefix) + prefix.length();
		int end = str.indexOf('&', start);

		return Integer.valueOf(str.substring(start, end));
	}
}
