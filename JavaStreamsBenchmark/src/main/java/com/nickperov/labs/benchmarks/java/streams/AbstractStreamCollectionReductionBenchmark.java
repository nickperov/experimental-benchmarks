package com.nickperov.labs.benchmarks.java.streams;

import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public abstract class AbstractStreamCollectionReductionBenchmark<T extends Collection<Integer>> extends
		AbstractStreamCollectionBenchmark<T> {

	public enum Operation {COUNT, SUM, MAX}

	@Param({"COUNT", "SUM", "MAX"})
	private Operation operation;

	private Function<Stream<Integer>, Integer> streamProcessor;
	private BinaryOperator<Integer> operator;
	private Blackhole blackhole;

	private static class IntContainer {

		private IntContainer(int value) {
			this.value = value;
		}

		private void add(int x) {
			value = value + x;
		}

		private int get() {
			return value;
		}

		private int value;
	}

	@Override
	public void init(Blackhole blackhole) {
		super.init(blackhole);
		this.blackhole = blackhole; 
		streamProcessor = getStreamProcessor();
		operator = getOperator();
		collection = initCollection();
	}

	private BinaryOperator<Integer> getOperator() {
		switch (operation) {
			case COUNT:
				return (element, count) -> count++;
			case SUM:
				return Integer::sum;
			case MAX:
				return Math::max;
			default:
				throw new RuntimeException("Operation not found: " + operation);
		}
	}

	private Function<Stream<Integer>, Integer> getStreamProcessor() {
		switch (operation) {
			case COUNT:
				return s -> Math.toIntExact(s.count());
			case SUM:
				return s -> s.mapToInt(integer -> integer).sum();
			case MAX:
				return s -> s.mapToInt(integer -> integer).max().orElse(0);
			default:
				throw new RuntimeException("Operation not found: " + operation);
		}
	}

	@Override
	public void executeLoop() {
		Integer result = 0;
		for (Integer element : collection) {
			result = operator.apply(element, result);
		}
		blackhole.consume(result);
	}
	
	@Override
	public void executeForEach() {
		IntContainer result = new IntContainer(0);

		collection.forEach(element -> result.add(operator.apply(element, result.get())));

		blackhole.consume(result.get());
	}

	@Override
	public void executeStream() {
		blackhole.consume( streamProcessor.apply(collection.stream()));
	}
}
