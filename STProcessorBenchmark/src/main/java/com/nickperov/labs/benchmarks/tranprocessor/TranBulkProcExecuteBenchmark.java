package com.nickperov.labs.benchmarks.tranprocessor;

import com.nickperov.stud.tranprocessor.exec.MultiThreadTranExecutor;
import com.nickperov.stud.tranprocessor.exec.SingleThreadTranExecutor;
import com.nickperov.stud.tranprocessor.exec.TransactionExecutorService;
import com.nickperov.stud.tranprocessor.test.TestUtils;
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
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 5, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
public class TranBulkProcExecuteBenchmark extends AbstractTranProcExecuteBenchmark {

	private TransactionExecutorService singleThreadExecutor, multiThreadExecutor;

	@Setup(Level.Trial)
	public void prepareTransactions() {
		this.transactions = prepareTransactionList(1_000_000);
		this.multiThreadExecutor.setTransDocumentList(TestUtils.createTranDocsMT(this.transactions));
		this.singleThreadExecutor.setTransDocumentList(TestUtils.createTranDocsST(this.transactions));
		this.multiThreadExecutor.reInit();
	}

	@Override
	protected void prepareTrialData() {
		initDictionaries();
		this.singleThreadExecutor = SingleThreadTranExecutor.getExecutorService();
		this.multiThreadExecutor = MultiThreadTranExecutor.getExecutorService();
	}

	@Benchmark
	@Threads(8)
	public void executeMT() {
		this.multiThreadExecutor.startTranService();
	}

	/*@Benchmark*/
	public void executeST() {
		this.singleThreadExecutor.startTranService();
	}
}