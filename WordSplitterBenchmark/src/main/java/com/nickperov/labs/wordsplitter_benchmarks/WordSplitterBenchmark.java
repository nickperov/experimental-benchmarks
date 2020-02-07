package com.nickperov.labs.wordsplitter_benchmarks;

import com.nickperov.labs.wordsplitter.WordSplitter;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value=2, warmups=1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
@Threads(value = 1)
public class WordSplitterBenchmark {


	private static String text = BenchmarkUtils.getUtils().readText();
	
	/*@Param({"2", "4", "6", "8", "10", "12", "16"})
	int cores;*/
	
	@Benchmark
	public void runSplitWordsRegExpCycle() {
		WordSplitter.splitWordsRegExpCycle(text);
	}
	
	@Benchmark
	public void runSplitWordsCharArrayCycle() {
		WordSplitter.splitWordsCharArrayCycle(text);
	}
	
	@Benchmark
	public void runSplitWordsForkJoinPool() {
		WordSplitter.splitWordsForkJoin(text);
	}
	
	
	/*@Benchmark
	public void runSplitWordsCharArrayRecursion() {
		WordSplitter.splitWordsCharArrayRecursion(text);
	}*/
}