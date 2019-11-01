package com.nickperov.labs.benchmarks.tranprocessor;

import com.nickperov.stud.tranprocessor.tran.Transaction;
import com.nickperov.stud.tranprocessor.tran.TransactionDocument;
import com.nickperov.stud.tranprocessor.tran.TransactionDocumentSortSync;
import com.nickperov.stud.tranprocessor.tran.TransactionDocumentSync;
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

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 3, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@Threads(value = 1)
@State(Scope.Thread)
public class TranProcExecuteBenchmark extends AbstractTranProcExecuteBenchmark {

	private TransactionDocument transDoc;
	private TransactionDocumentSync transDocSync;
	private TransactionDocumentSortSync transDocSortSync;

	private Transaction tran;

	@Setup(Level.Iteration)
	public void prepareTransactions() {
		this.tran = this.transactions.get(this.i++ % this.transactions.size());
		this.transDoc = new TransactionDocument(this.tran);
		this.transDocSync = new TransactionDocumentSync(this.tran);
		this.transDocSortSync = new TransactionDocumentSortSync(this.tran);
	}

	@Override
	protected void prepareTrialData() {
		initDictionaries();
		this.transactions = prepareTransactionList(100);
	}

	@Benchmark
	public int sync() {
		synchronized (this.tran.getDebitAcc()) {
			synchronized (this.tran.getCreditAcc()) {
				return 1;
			}
		}
	}

	@Benchmark
	public void execute() {
		this.transDoc.execute();
	}

	@Benchmark
	public void executeSync() {
		this.transDocSync.execute();
	}

	@Benchmark
	public void executeSortSync() {
		this.transDocSortSync.execute();
	}
}