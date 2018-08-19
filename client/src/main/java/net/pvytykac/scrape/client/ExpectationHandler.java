package net.pvytykac.scrape.client;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pvytykac.net.scrape.model.v1.FailedExpectation;
import pvytykac.net.scrape.model.v1.ScrapeExpectation;
import pvytykac.net.scrape.model.v1.enums.ExpectationType;
import pvytykac.net.scrape.model.v1.enums.Operator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExpectationHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExpectationHandler.class);

	private final Map<Operator, OperatorHandler> operatorHandlers;
	private final Map<ExpectationType, ExpectationTypeHandler> expectationTypeHandlers;

	public ExpectationHandler() {
		operatorHandlers = ImmutableMap.<Operator, OperatorHandler>builder()
				.put(Operator.CONTAINS, (val, target) -> val.map(v -> v.contains(target)).orElse(false))
				.put(Operator.STARTS_WITH, (val, target) -> val.map(v -> v.startsWith(target)).orElse(false))
				.put(Operator.ENDS_WITH, (val, target) -> val.map(v -> v.endsWith(target)).orElse(false))
				.put(Operator.EQUALS, (val, target) -> val.map(v -> v.equals(target)).orElse(false))
				.put(Operator.MATCHES_REGEX, (val, target) -> val.map(v -> Pattern.compile(target).matcher(v).find()).orElse(false))
				.put(Operator.EXISTS, (val, target) -> val.isPresent())
				.put(Operator.NOT_BLANK, (val, target) -> val.map(StringUtils::isNotBlank).orElse(false))
				.build();

		expectationTypeHandlers = ImmutableMap.<ExpectationType, ExpectationTypeHandler>builder()
				.put(ExpectationType.HEADER, (response, target) -> Optional.ofNullable(response.header(target)))
				.put(ExpectationType.STATUS_CODE, (response, target) -> Optional.of(Integer.toString(response.code())))
				.put(ExpectationType.BODY_CSS, (response, target) -> Optional.ofNullable(response.html().selectFirst(target))
						.map(Element::text))
				.build();
	}

	public List<FailedExpectation> processExpectations(ResponseWrapper response, List<ScrapeExpectation> expectations) {
		LOG.info("processing '{}' expectations", expectations == null ? 0 : expectations.size());
		return Optional.ofNullable(expectations).orElse(Collections.emptyList())
				.stream()
				.map(expectation -> processExpectation(response, expectation))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}

	private Optional<FailedExpectation> processExpectation(ResponseWrapper response, ScrapeExpectation expectation) {
		OperatorHandler opHandler = operatorHandlers.get(expectation.getOperator());
		ExpectationTypeHandler expTypeHandler = expectationTypeHandlers.get(expectation.getType());

		Optional<String> value = expTypeHandler.retrieveValue(response, expectation.getTarget());
		boolean expectationMet = opHandler.matches(value, expectation.getExpectedValue());

		LOG.info("processing expectation of type '{}' with target '{}', operator '{}', expected value: '{}' and " +
						"expected result '{}'. Actual value '{}' and match was evaluated as '{}'", expectation.getType(),
				expectation.getTarget(), expectation.getOperator(), expectation.getExpectedValue(),
				expectation.getExpected(), value.orElse(null), expectationMet);

		return expectationMet == expectation.getExpected()
				? Optional.empty()
				: new FailedExpectation.FailedExpectationBuilder()
						.withActual(value.orElse(null))
						.withExpectation(expectation)
						.buildAsOptional();
	}

	interface ExpectationTypeHandler {
		Optional<String> retrieveValue(ResponseWrapper response, String target);
	}

	interface OperatorHandler {
		boolean matches(Optional<String> value, String target);
	}
}