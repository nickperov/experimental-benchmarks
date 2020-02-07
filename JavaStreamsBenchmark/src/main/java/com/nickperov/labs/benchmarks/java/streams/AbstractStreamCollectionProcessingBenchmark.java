package com.nickperov.labs.benchmarks.java.streams;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public abstract class AbstractStreamCollectionProcessingBenchmark<T extends Collection<Integer>> extends AbstractStreamCollectionBenchmark<T> {

	public enum Operation {IDENTITY, SQRT, BLACK_HOLE_10}

	private Function<Integer, Double> mappingFunction;
	private Consumer<Double> outputConsumer;

	@Param({"IDENTITY", "SQRT", "BLACK_HOLE_10"})
	private Operation operation;

	@Override
	public void init(Blackhole blackhole) {
		super.init(blackhole);
		mappingFunction = getMappingFunction();
		outputConsumer = blackhole::consume;
		collection = initCollection();
	}

	Function<Integer, Double> getMappingFunction() {
		switch (operation) {
			case SQRT:
				return Math::sqrt;
			case IDENTITY:
				return Integer::doubleValue;
			case BLACK_HOLE_10:
				return (Integer x) -> {
					Blackhole.consumeCPU(10L);
					return x.doubleValue();
				};
			default:
				throw new RuntimeException("Operation not found: " + operation);
		}
	}
	

	@Override
	public void executeLoop() {
		for (Integer element : collection) {
			outputConsumer.accept(mappingFunction.apply(element));
		}
	}

	@Override
	public void executeForEach() {
		collection.forEach(element -> outputConsumer.accept(mappingFunction.apply(element)));
	}

	@Override
	public void executeStream() {
		collection.stream().map(mappingFunction).forEach(outputConsumer);
	}
}