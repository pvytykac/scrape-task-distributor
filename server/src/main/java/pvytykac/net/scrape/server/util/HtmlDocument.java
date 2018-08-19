package pvytykac.net.scrape.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlDocument {

	private Document document;

	public HtmlDocument(String html) {
		this.document = Jsoup.parse(html);
	}

	public HtmlTable getTableBySummary(String summary) {
		return new HtmlTable(getElementByAttribute("table", "summary", summary));
	}

	public HtmlForm getFormByName(String name) {
		return new HtmlForm(getElementByAttribute("form", "name", name));
	}

	private Element getElementByAttribute(String tag, String attribute, String value) {
		return document.selectFirst(tag + "[" + attribute + "='" + value + "']");
	}

	public static class HtmlForm extends HtmlElement {

		public HtmlForm(Element element) {
			super(element);
		}

		public HtmlElement getInputByName(String name) {
			return new HtmlElement(super.selectFirstRaw("input[name='" + name  + "']"));
		}

	}

	public static class HtmlTable extends HtmlElement {

		public HtmlTable(Element element) {
			super(element);
		}

		public List<HtmlTableRow> getRows() {
			return getRows(0);
		}

		public List<HtmlTableRow> getRows(int offset) {
			return selectRaw("tr:nth-child(n+" + (offset + 1) + ")")
				.stream()
				.map(HtmlTableRow::new)
				.collect(Collectors.toList());
		}

		public HtmlTableRow selectRow(int row) {
			return new HtmlTableRow(selectFirstRaw("tr:nth-child(" + row + ")"));
		}

		public HtmlElement selectRowAndColumn(int row, int col) {
			return selectRow(row).selectColumn(col);
		}

	}

	public static class HtmlTableRow extends HtmlElement {

		public HtmlTableRow(Element element) {
			super(element);
		}

		public HtmlElement selectColumn(int col) {
			return select("td:nth-child(" + col + ")");
		}
	}

	public static class HtmlElement {

		private final Optional<Element> element;

		public HtmlElement(Element element) {
			this.element = Optional.ofNullable(element);
		}

		protected Element selectFirstRaw(String selector) {
			return element.map(el -> el.selectFirst(selector)).orElse(null);
		}

		protected Elements selectRaw(String selector) {
			return element.map(el -> el.select(selector)).orElse(null);
		}

		public HtmlElement select(String selector) {
			return new HtmlElement(selectFirstRaw(selector));
		}

		public Optional<String> textOptional() {
			return element.map(Element::text);
		}

		public String text() {
			return textOptional().orElse(null);
		}

		public Optional<String> attrOptional(String attribute) {
			return element.map(el -> el.attr(attribute));
		}

		public String attr(String attribute) {
			return attrOptional(attribute).orElse(null);
		}

		public boolean hasText() {
			return textOptional()
					.map(StringUtils::isNotBlank)
					.orElse(false);
		}

		public DateTime textAsDate(String format) {
			return textOptional().map(str -> {
				try {
					return StringUtils.isBlank(str) ? null : new SimpleDateFormat(format).parse(str);
				} catch (ParseException ex) {
					throw new RuntimeException(ex);
				}
			})
			.map(date -> new DateTime(date.getTime(), DateTimeZone.UTC))
			.orElse(null);
		}
	}
}
