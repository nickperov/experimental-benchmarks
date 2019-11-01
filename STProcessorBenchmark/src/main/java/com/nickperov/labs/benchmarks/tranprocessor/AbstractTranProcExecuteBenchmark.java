package com.nickperov.labs.benchmarks.tranprocessor;

import com.nickperov.stud.tranprocessor.test.TestUtils;
import com.nickperov.stud.tranprocessor.test.TestUtils.TranGenStrategy;
import com.nickperov.stud.tranprocessor.tran.Transaction;
import java.util.List;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public abstract class AbstractTranProcExecuteBenchmark {

	int i = 0;
	List<Transaction> transactions;

	@Setup(Level.Trial)
	public void prepareData() {
		prepareTrialData();
	}

	protected abstract void prepareTrialData();

	protected void initDictionaries() {
		TestUtils.initDictionaries(1000, null);
	}

	protected List<Transaction> prepareTransactionList(final int number) {
		this.i = 0;
		return TestUtils.generateTransactions(TranGenStrategy.RND, number);
	}
}