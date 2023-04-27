package com.nickperov.labs.benchmarks.std.collections.lists;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
public class SDTListIterateBenchmark extends STDListBenchmark {

    @Param({"ArrayList", "LinkedList"})
    private ListType listType;

    @Param({"1000"})
    private int listSize;


    @Setup(Level.Iteration)
    public void initializeList() {
        final var rnd = new Random();
        super.initializeList(listType, listSize);
        // Fill Elements
        for (int i = 0; i < listSize; i++) {
            list.add(rnd.nextInt());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @SuppressWarnings("All")
    public int iterateForLoop(final Blackhole blackhole) {
        for (int i = 0; i < list.size(); i++) {
            blackhole.consume(list.get(i));
        }
        return 1;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void iterateEnhancedForLoop(final Blackhole blackhole) {
        for (int el : list) {
            blackhole.consume(el);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void iterateForEach(final Blackhole blackhole) {
        list.forEach(blackhole::consume);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @SuppressWarnings("All")
    public void whileIterator(final Blackhole blackhole) {
        final var iterator = list.listIterator();
        while (iterator.hasNext()) {
            blackhole.consume(iterator.next());
        }
    }
}
