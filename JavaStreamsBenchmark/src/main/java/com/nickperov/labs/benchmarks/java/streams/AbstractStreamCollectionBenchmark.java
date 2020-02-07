package com.nickperov.labs.benchmarks.java.streams;

import java.util.Collection;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public abstract class AbstractStreamCollectionBenchmark<T extends Collection<Integer>> extends AbstractStreamBenchmark {

	final Random rnd = new Random();

	T collection;

	@Param({/*"10", "100",*/ "1000"/*, "10000"*/})
	private int collectionSize;

	T initCollection() {
		return Stream.generate(rnd::nextInt).limit(collectionSize).collect(Collectors.toCollection(getCollectionSupplier()));
	}

	abstract Supplier<T> getCollectionSupplier();
}