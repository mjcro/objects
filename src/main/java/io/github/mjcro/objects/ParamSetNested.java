package io.github.mjcro.objects;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class ParamSetNested implements ParamSet {
    private final ParamSetNested parent;
    private final Param<?> param;

    ParamSetNested(ParamSetNested parent, Param<?> param) {
        this.parent = parent;
        this.param = param;
    }

    @Override
    public Optional<Param<?>> find(String name) {
        return name == null
                ? Optional.empty()
                : (name.equals(param.getName()) ? Optional.of(param) : (parent == null ? Optional.empty() : parent.find(name)));
    }

    public int depth() {
        return parent == null ? 1 : 1 + parent.depth();
    }

    @Override
    public int size() {
        return names().size();
    }

    @Override
    public Set<String> names() {
        LinkedHashSet<String> result = new LinkedHashSet<>();
        ParamSetNested current = this;
        do {
            result.add(current.param.getName());
            current = current.parent;
        } while (current != null);
        return result;
    }

    @Override
    public Iterator<Param<?>> iterator() {
        return new ParamSetIterator(this);
    }

    @Override
    public Stream<Param<?>> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }

    private static final class ParamSetIterator implements Iterator<Param<?>> {
        private final HashSet<String> visited = new HashSet<>();
        private ParamSetNested next;

        ParamSetIterator(ParamSetNested paramSet) {
            this.next = paramSet;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Param<?> next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            Param<?> result = next.param;
            visited.add(result.getName());
            do {
                next = next.parent;
            } while (next != null && visited.contains(next.param.getName()));

            return result;
        }
    }
}
