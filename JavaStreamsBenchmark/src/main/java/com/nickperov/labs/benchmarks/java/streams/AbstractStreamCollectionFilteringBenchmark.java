package com.nickperov.labs.benchmarks.java.streams;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public abstract class AbstractStreamCollectionFilteringBenchmark<T extends Collection<Integer>> extends
		AbstractStreamCollectionBenchmark<T> {

	public enum Operation {ODD}

	private Predicate<Integer> filterPredicate;
	private Consumer<Integer> outputConsumer;

	@Param({"ODD"})
	private Operation operation;

	@Override
	public void init(Blackhole blackhole) {
		super.init(blackhole);
		filterPredicate = getFilterPredicate();
		outputConsumer = blackhole::consume;
		collection = initCollection();
	}

	Predicate<Integer> getFilterPredicate() {
		switch (operation) {
			case ODD:
				return number -> number % 2 != 0;
			default:
				throw new RuntimeException("Operation not found: " + operation);
		}
	}


	@Override
	public void executeLoop() {
		for (Integer element : collection) {
			if (filterPredicate.test(element)) {
				outputConsumer.accept(element);
			}
		}
	}

	@Override
	public void executeForEach() {
		collection.forEach(element -> {
			if (filterPredicate.test(element)) {
				outputConsumer.accept(element);
			}
		});
	}

	@Override
	public void executeStream() {
		collection.stream().filter(filterPredicate).forEach(outputConsumer);
	}
}
