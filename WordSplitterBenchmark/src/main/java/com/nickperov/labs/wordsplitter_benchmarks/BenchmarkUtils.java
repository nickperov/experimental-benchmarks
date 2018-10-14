package com.nickperov.labs.wordsplitter_benchmarks;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BenchmarkUtils {
	
	private static final BenchmarkUtils instance = new BenchmarkUtils();
	
	public static BenchmarkUtils getUtils() {
		return instance;
	}
	
	private BenchmarkUtils() {}

	public String readText() {
		final byte[] buf = new byte[256];
		final StringBuilder sb = new StringBuilder();
		try (final InputStream in = getClass().getResourceAsStream("/Text.txt");
				final BufferedInputStream bi = new BufferedInputStream(in)) {
			int index;
			while ((index = bi.read(buf)) > 0)
				sb.append(new String(buf, 0, index));
			
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
