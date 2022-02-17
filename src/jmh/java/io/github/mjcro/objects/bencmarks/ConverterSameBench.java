package io.github.mjcro.objects.bencmarks;

import io.github.mjcro.objects.Converter;
import io.github.mjcro.objects.converters.ConverterSame;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class ConverterSameBench {
    private final Converter converter = new ConverterSame();

    @Benchmark
    public Integer simple() {
        return converter.convert(12345, Integer.class);
    }

    @Benchmark
    public Number inheritance() {
        return converter.convert(12345, Number.class);
    }
}
