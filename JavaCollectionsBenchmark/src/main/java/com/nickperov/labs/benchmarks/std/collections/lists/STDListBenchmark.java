package com.nickperov.labs.benchmarks.std.collections.lists;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;


@Fork(value = 1, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
public class STDListBenchmark {
	

	public enum ListType {
		ArrayList, LinkedList, Vector, CopyOnWriteArrayList
	}

	@Param({"0", "10000000", "20000000"})
	private int preAllocatedSize;
	
	@Param({"ArrayList", "LinkedList", "Vector", "CopyOnWriteArrayList"})
	private ListType listType;

	private final Random rnd = new Random();
	private List<Integer> list;

	@Setup(Level.Iteration)
	public void initializeList() {
		//SkipBenchmarksException 
		list = getListSupplier().apply(preAllocatedSize);
		/*list = Stream.generate(rnd::nextInt).limit(initialSize).collect(Collectors.toCollection(getListSupplier()));*/
	}

	private Function<Integer,List<Integer>> getListSupplier() {

		switch (listType) {
			case ArrayList:
				return ArrayList::new;
			case LinkedList:
				return (Integer size) -> new LinkedList();
			case Vector:
				return Vector::new;
			case CopyOnWriteArrayList:
				return  (Integer size) -> new CopyOnWriteArrayList();
		}

		throw new RuntimeException("List type not found: " + listType);
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void addElement() {
		list.add(1);
	}


}
