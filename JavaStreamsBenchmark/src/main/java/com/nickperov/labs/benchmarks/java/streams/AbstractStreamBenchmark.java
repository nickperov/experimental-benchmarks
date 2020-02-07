package com.nickperov.labs.benchmarks.java.streams;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public abstract class AbstractStreamBenchmark {

	public enum Execution {STREAM, LOOP, FOR_EACH}

	private Runnable runnable;

	@Param({"STREAM", "LOOP", "FOR_EACH"})
	private Execution executionType;

	@Setup(Level.Trial)
	public void init(Blackhole blackhole) {
		runnable = getRunnable();
	}

	private Runnable getRunnable() {
		switch (executionType) {
			case STREAM:
				return this::executeStream;
			case LOOP:
				return this::executeLoop;
			case FOR_EACH:
				return this::executeForEach;
		}
		return runnable;
	}

	@Benchmark
	public void execute() {
		runnable.run();
	}

	public abstract void executeLoop();

	public abstract void executeForEach();

	public abstract void executeStream();
}