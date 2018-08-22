package pvytykac.net.scrape.server.task.impl;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pvytykac.net.scrape.model.v1.ClientException;
import pvytykac.net.scrape.model.v1.FailedExpectation;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.model.res.ResAttributeValue;
import pvytykac.net.scrape.server.db.model.res.ResForm;
import pvytykac.net.scrape.server.db.model.res.ResInstitution;
import pvytykac.net.scrape.server.db.model.res.ResRegion;
import pvytykac.net.scrape.server.db.model.res.ResUnit;
import pvytykac.net.scrape.server.db.repository.impl.RepositoryFacade;
import pvytykac.net.scrape.server.db.repository.ResRepository;
import pvytykac.net.scrape.server.task.TaskType;
import pvytykac.net.scrape.server.util.HtmlDocument;
import pvytykac.net.scrape.server.util.HtmlDocument.HtmlTable;
import pvytykac.net.scrape.server.util.HtmlDocument.HtmlTableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResTaskType implements TaskType {

	private static final Logger LOG = LoggerFactory.getLogger(ResTaskType.class);

	private final ResRepository repository;

	public ResTaskType(RepositoryFacade repositoryFacade) {
		this.repository = repositoryFacade.getResRepository();
	}

	@Override
	public ScrapeTask createScrapeTask(Ico ico) {
		return ico.getResId() == null
			? createTaskForUnknownId(ico.getId())
			: createTaskForKnownId(ico.getResId());
	}

	private ScrapeTask createTaskForUnknownId(String ico) {
// todo: build task from below steps
//		stepDefinitions:
//		- sequenceNumber: 1
//		method: "POST"
//		uri: "http://apl.czso.cz/irsw/hledat.jsp"
//		contentType: "application/x-www-form-urlencoded"
//		payload: "ico=${ico}&nazev=&forma=&okres=&texttype=0&zanik=0&run_rswquery=Hledej"
//		expectations:
//		- id: 1
//		type: STATUS_CODE
//		expectedValue: "200"
//		operator: EQUALS
//		expected: true
//				- id: 2
//		type: HEADER
//		target: "Content-Type"
//		expectedValue: "text/html"
//		operator: CONTAINS
//		expected: true
//				- id: 3
//		type: BODY_CSS
//		target: "a[href^='detail.jsp?prajed_id=']"
//		operator: NOT_BLANK
//		expected: true
//		scrape:
//		- type: HREF
//		target: "a[href^='detail.jsp?prajed_id=']"
//		storeAs: "detailHref"
//				- sequenceNumber: 2
//		method: "GET"
//		uri: "http://apl.czso.cz/irsw/${detailHref}"
//		expectations:
//		- id: 4
//		type: STATUS_CODE
//		expectedValue: 200
//		operator: EQUALS
//		expected: true
//				- id: 5
//		type: HEADER
//		target: "Content-Type"
//		expectedValue: "text/html"
//		operator: CONTAINS
//		expected: true
//				- id: 6
//		type: BODY_CSS
//		target: "table[summary='identifikace'] tr:nth-child(1) td:nth-child(3)"
//		operator: NOT_BLANK
//		expected: true
		return null;
	}

	private ScrapeTask createTaskForKnownId(Integer id) {
		return null;
	}

	@Override
	public Status processClientError(ClientException error, ScrapeStep step) {
		LOG.error("scrape task failed because of client error during step '{}': {}\n{}", step.getSequenceNumber(),
				error.getMessage(), error.getStackTrace());

		return new Status(null, false);
	}

	@Override
	public Status processFailedExpectations(List<FailedExpectation> errors, ScrapeStep step) {
		LOG.error("scrape task failed because of '{}' unmet expectations during step '{}' ", errors.size(),
				step.getSequenceNumber());

		Long timeout = errors.stream()
				.mapToLong(expec -> getTimeout(expec.getExpectation().getId(), expec.getActual()))
				.filter(Objects::nonNull)
				.max()
				.orElse(0L);

		return new Status(timeout > 0L ? timeout : null, false);
	}

	@Override
	public Status processSuccess(ScrapeResult result) {
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

		Map<String, List<String>> attributes = new HashMap<>();
		String attrName = null;
		List<HtmlTableRow> attributeRows = document.getTableBySummary("atributy").getRows(1);
		for (HtmlTableRow attributeRow: attributeRows) {
			attrName = attributeRow.selectColumn(1).select("a").hasText()
					? attributeRow.selectColumn(1).text()
					: attrName;

			String code = attributeRow.selectColumn(2).text();
			String text = attributeRow.selectColumn(3).text();

			attributes.putIfAbsent(attrName, new ArrayList<>());
			attributes.get(attrName).add(code + " - " + text);
		}

		LOG.info("parsed out res institution: '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}'",
				ico, name, formId, formName, created, ceased, address, regionCode, region, unitCode, unit, attributes);

		List<ResAttributeValue> resAttributes = attributes.entrySet().stream()
				.flatMap(attr -> attr.getValue().stream()
						.map(attrValue -> new ResAttributeValue.Builder()
						.withId(attr.getKey())
						.withText(attrValue)
						.build()))
				.collect(Collectors.toList());

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
				.withAttributes(resAttributes)
				.build();

		repository.save(institution);

		return new Status(0L, false);
	}

	private static long getTimeout(Integer expectationId, String actual) {
		switch (expectationId) {
			case 1:
			case 4:
				return "404".equals(actual) ? 250L : 0L;
			default: return 0L;
		}
	}

	private static Integer findId(String str) {
		String prefix = "prajed_id=";
		int start = str.indexOf(prefix) + prefix.length();
		int end = str.indexOf('&', start);

		return Integer.valueOf(str.substring(start, end));
	}
}
