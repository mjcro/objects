package io.github.mjcro.objects.bencmarks;

import io.github.mjcro.objects.Converter;
import io.github.mjcro.objects.converters.ConverterToString;
import org.openjdk.jmh.annotations.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class ConverterToStringBench {
    private static final byte[] foo = "Foo".getBytes(StandardCharsets.UTF_8);
    private final Converter converter = new ConverterToString();

    @Benchmark
    public String longs() {
        return converter.convert(5L, String.class);
    }

    @Benchmark
    public String strings() {
        return converter.convert("Foo", String.class);
    }

    @Benchmark
    public String byteArrays() {
        return converter.convert(foo, String.class);
    }
}
