package pvytykac.net.scrape.server.service.impl;

import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.repository.IcoRepository;
import pvytykac.net.scrape.server.service.IcoService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-22
 */
public class IcoServiceImpl implements IcoService {

    private final IcoRepository repository;

    public IcoServiceImpl(IcoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Ico> getIco(String offsetIco, Set<Integer> formIds) {
        return (formIds == null || formIds.isEmpty())
                ? generateAndFetchNextIco(offsetIco)
                : repository.findNext(offsetIco, formIds);
    }

    private Optional<Ico> generateAndFetchNextIco(String offsetIco) {
        String nextIco = offsetIco == null
                ? new GeneratedIco().increment().toString()
                : new GeneratedIco(offsetIco).increment().toString();

        if (nextIco == null) {
            return Optional.empty();
        } else {
            Ico ico = new Ico.Builder()
                    .withId(nextIco)
                    .build();

            if (!repository.findOptional(nextIco).isPresent()) {
                repository.save(ico);
            }

            return Optional.of(ico);
        }
    }

    private static class GeneratedIco {

        private final int a;
        private final int b;
        private final int c;
        private final int d;
        private final int e;
        private final int f;
        private final int g;

        private GeneratedIco() {
            this(0, 0, 0, 0, 0, 0, 1);
        }

        private GeneratedIco(String ico) {
            this.a = Integer.parseInt(ico.substring(0, 1));
            this.b = Integer.parseInt(ico.substring(1, 2));
            this.c = Integer.parseInt(ico.substring(2, 3));
            this.d = Integer.parseInt(ico.substring(3, 4));
            this.e = Integer.parseInt(ico.substring(4, 5));
            this.f = Integer.parseInt(ico.substring(5, 6));
            this.g = Integer.parseInt(ico.substring(6, 7));
        }

        private GeneratedIco(int a, int b, int c, int d, int e, int f, int g) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
        }

        private int product() {
            return a * 8 + b * 7 + c * 6 + d * 5 + e * 4 + f * 3 + g * 2;
        }

        private int check() {
            return (11 - (product() % 11)) % 10;
        }

        private boolean hasNext() {
            return a < 8;
        }

        private GeneratedIco increment() {
            return doIncrement(new ArrayList<>(Arrays.asList(g, f, e, d, c, b, a)), 0,1);
        }

        private GeneratedIco doIncrement(List<Integer> list, int ix, int carry) {
            if (carry == 0 || ix >= list.size()) {
                return new GeneratedIco(list.get(6), list.get(5), list.get(4), list.get(3), list.get(2), list.get(1),
                        list.get(0));
            } else if (list.get(ix) == 9) {
                list.remove(ix);
                list.add(ix, 0);
                return doIncrement(list, ++ix, 1);
            } else {
                list.add(ix, list.remove(ix) + 1);
                return doIncrement(list, ++ix, 0);
            }
        }

        @Override
        public String toString() {
            return String.format("%d%d%d%d%d%d%d%d", a, b, c, d, e, f, g, check());
        }
    }
}
