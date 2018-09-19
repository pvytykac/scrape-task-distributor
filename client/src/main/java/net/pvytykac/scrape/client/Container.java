package net.pvytykac.scrape.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Container {

	void put(String storeAs, Object value);
	Object get(String key);

	static Container listContainer() {
		return new Container() {

			private final List<Object> list = new ArrayList<>();

			@Override
			public void put(String storeAs, Object value) {
				list.add(value);
			}

			@Override
			public Object get(String key) {
				return list;
			}

			@Override
			public String toString() {
				return list.isEmpty()
					? ""
					: "[" + list.stream()
						.reduce("", (acc, cur) -> acc + ", " + cur.toString())
						.toString()
						.substring(2) + "]";
			}
		};
	}

	static Container objectContainer() {
		return objectContainer(new HashMap<>());
	}

	static Container objectContainer(Map<String, Object> container) {
		return new Container() {

			private final Map<String, Object> map = container;

			@Override
			public void put(String storeAs, Object value) {
				map.putIfAbsent(storeAs, value);
			}

			@Override
			public Object get(String key) {
				return map.get(key);
			}

			@Override
			public String toString() {
				return map.isEmpty()
						? "{}"
						: "{" + map.entrySet().stream()
							.reduce("", (acc, cur) -> acc + ", \"" + cur.getKey() + "\": \"" + cur.getValue().toString() + "\"", (old, cur) -> cur)
							.substring(2) + "}";
			}
		};
	}

}
