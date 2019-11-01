package com.nickperov.labs.benchmarks.tranprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import com.nickperov.stud.tranprocessor.data.Index;
import com.nickperov.stud.tranprocessor.data.Instrument;
import com.nickperov.stud.tranprocessor.test.TestUtils;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value=5, warmups=1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
public class TranProcInstrLockBenchmark {
	
	private final Index index = Index.getIndex();
	
	private final List<Instrument> instruments = new ArrayList<>();
	
	private Instrument instrument;
	
	@Setup(Level.Trial)
	public void prepareDictionaries() {
		TestUtils.initDictionaries(100, null);
		instruments.addAll(index.getAllInstruments());
	}
	
	@Setup(Level.Iteration)
	public void getInstrument() {
		// Get random instrument
		final Random rnd = new Random();
		instrument = instruments.get(rnd.nextInt(instruments.size() - 1));
	}
	
	@TearDown(Level.Invocation)
	public void resetInstrLock() {
		try { instrument.unLock(); } catch (IllegalMonitorStateException e) {}
	}
	
	// Try execute transaction
	@Benchmark
	public int getInstrumentLock() {
		instrument.lock();
		return 1;
	}
	
	@Benchmark
	public int syncInstrument() {
		synchronized (instrument) {
			return 1;
		}
	}
	 
	@Benchmark
	public int getInstrumentLockUnLock() {
		try {
			instrument.lock();
			return 1;
		} finally {
			instrument.unLock();
		}
	}

	@Benchmark
	public int baseLine() {
		return 1;
	}
}