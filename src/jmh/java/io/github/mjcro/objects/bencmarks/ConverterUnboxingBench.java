package io.github.mjcro.objects.bencmarks;

import io.github.mjcro.objects.Converter;
import io.github.mjcro.objects.converters.ConverterUnboxing;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class ConverterUnboxingBench {
    private final Converter converter = new ConverterUnboxing();

    @Benchmark
    public boolean booleans() {
        return converter.convert(Boolean.TRUE, boolean.class);
    }

    @Benchmark
    public byte bytes() {
        return converter.convert((byte) 33, byte.class);
    }

    @Benchmark
    public short shorts() {
        return converter.convert((short) -8, short.class);
    }

    @Benchmark
    public int intShorts() {
        return converter.convert((short) 22, int.class);
    }

    @Benchmark
    public int ints() {
        return converter.convert(1024, int.class);
    }

    @Benchmark
    public long longShorts() {
        return converter.convert((short) 22, long.class);
    }

    @Benchmark
    public long longInts() {
        return converter.convert(-4, long.class);
    }

    @Benchmark
    public long longs() {
        return converter.convert(8363355L, long.class);
    }

    @Benchmark
    public float floats() {
        return converter.convert(1.23f, float.class);
    }

    @Benchmark
    public double floatDoubles() {
        return converter.convert(-332.1f, double.class);
    }

    @Benchmark
    public double doubles() {
        return converter.convert(12312.11d, double.class);
    }
}
