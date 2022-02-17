package io.github.mjcro.objects;

import io.github.mjcro.objects.bencmarks.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class Benchmarks {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ConverterSameBench.class.getSimpleName())
                .include(ConverterStringBooleansBench.class.getSimpleName())
                .include(ConverterUnboxingBench.class.getSimpleName())
                .include(ConverterStringEnumsBench.class.getSimpleName())
                .include(ConverterToStringBench.class.getSimpleName())
                .include(ConvertersStringNumbersBench.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .warmupTime(TimeValue.seconds(1))
                .resultFormat(ResultFormatType.TEXT)
                .result("jmh.latest.txt")
                .build();
        new Runner(opt).run();
    }
}
