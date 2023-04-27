package com.nickperov.labs.benchmarks.std.collections.lists;

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


/**
 *
 */

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
public class STDListAddBenchmark extends STDListBenchmark {

    @Param({"0", "10000000"})
    private int preAllocatedSize;

    @Param({"ArrayList", "LinkedList", "Vector", "CopyOnWriteArrayList"})
    private ListType listType;

    @Setup(Level.Iteration)
    public void initializeList() {
        if (preAllocatedSize > 0 && listType != ListType.Vector && listType != ListType.ArrayList) {
            // Skip execution
            // Non-zero pre allocation size make sense only for array backed lists  
            System.exit(0);
        }
        super.initializeList(listType, preAllocatedSize);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void addElement() {
        list.add(1);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void add10MilElements() {
        for (int i = 0; i < 10_000_000; i++) {
            list.add(1);
        }
    }
}