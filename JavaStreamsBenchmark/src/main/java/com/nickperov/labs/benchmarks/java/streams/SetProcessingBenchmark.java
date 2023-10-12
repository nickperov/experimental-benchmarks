package com.nickperov.labs.benchmarks.java.streams;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmark to compare performance of processing the set of integers by stream and by loop
 */

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SetProcessingBenchmark extends AbstractStreamCollectionProcessingBenchmark<Set<Integer>> {

  @Override
  Supplier<Set<Integer>> getCollectionSupplier() {
    return HashSet::new;
  }
}