package io.github.mjcro.objects.bencmarks;

import io.github.mjcro.objects.Converter;
import io.github.mjcro.objects.converters.ConverterStringNumbers;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class ConvertersStringNumbersBench {
    private final Converter converter = new ConverterStringNumbers();

    @Benchmark
    public int ints() {
        return converter.convert("123456789", int.class);
    }

    @Benchmark
    public int integers() {
        return converter.convert("123456789", Integer.class);
    }

    @Benchmark
    public double doubles() {
        return converter.convert("-3122.333", double.class);
    }
}
