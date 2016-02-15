package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import simulator.AlgorithmThread;
import simulator.InputReader;
import algorithm.Algorithm;
import algorithm.pagingAlgorithm.LRU4a;
import algorithm.pagingAlgorithm.PagingAlgorithmResult;
import algorithm.pagingAlgorithm.RandomLRU2;

public class Helper {
		
	public void SpeedTest(String dataFileName, int bufferSize, Algorithm algo) throws IOException, InterruptedException
	{
		InputReader reader = new InputReader("data\\" + dataFileName, bufferSize);
		int[] data = reader.getNext();		
		
		long time_a = 0;
		PagingAlgorithmResult result;
				
			time_a = System.currentTimeMillis();
			
			while(data != null)
			{
				for(int i = 0; i < 100; i++)
				{
					algo.reset();
					int counter = 0;
					for(int page : data)
					{
						if(page <= 0)
							break;
						
						if(counter % 100000 == 0)
							System.out.println("counter: " + counter + "      page faults: " + algo.getResults().getPageFaults());
						algo.processNextPage(page);
						counter++;
					}	
				}
				data = reader.getNext();
			}

			time_a = System.currentTimeMillis() - time_a;
			
			result = algo.getResults();
			System.out.println(result.getName() 
					+ " CacheSize: " + result.getCacheSize()
					+ " PageFaults: " + result.getPageFaults() 
					+ " Time: " + time_a);	
			
			Thread.sleep(200);
	}
	
	
	/**
	 * This method logs the average speed of an algorithm for a specific bufferSize
	 * (this will only load one times the buffer with bufferSize elements)
	 * 
	 * Be careful with using this method, in dependency on traceData and iterations this method could take much time!
	 * 
	 * @param traceDataFileName the trace data file name in the data folder
	 * @param algo the specific algorithm to test
	 * @param bufferSize the amount of pages loaded from the trace (recommended ~ 10+ million depends on memory)
	 */
	public void LogSpeed(Algorithm algorithm, String traceDataFileName, int bufferSize, int cacheMin, int cacheMax, int stepSize, int iterationCount, String logFileName) throws NumberFormatException, IOException, InterruptedException
	{
		InputReader reader = new InputReader("data\\" + traceDataFileName, bufferSize);
		int[] data = reader.getNext();
			
		long time_a = 0;
		long avgTime = 0;
		PagingAlgorithmResult result;	

		FileWriter writer = new FileWriter("data\\" + logFileName);
		writer.write("CacheSize; PageFaults; AverageRunTime" + System.lineSeparator());
		System.out.println("logging started");

		Algorithm algo;
		
		for(int cacheSize = 10; cacheSize < cacheMax; cacheSize += stepSize)
		{
			algo = algorithm.create("", cacheSize, reader.getRange(), 0);
			
			time_a = System.currentTimeMillis();			
			for(int i = 0; i < iterationCount; i++)
			{
				algo.reset();
				for(int page : data)
				{
					if(page <= 0)
					{
						break;
					}	
					algo.processNextPage(page);
				}	
			}	
			time_a = System.currentTimeMillis() - time_a; // the time for this turn			
			avgTime = (time_a / iterationCount);
			
			result = algo.getResults();
			writer.write(cacheSize + "; " + result.getPageFaults() + "; " + avgTime + System.lineSeparator());
		}
		writer.flush();
		writer.close();
		Thread.sleep(200);
		System.out.println("logging finished");
	}
	
	// Slow
	public void logPageFaultsSeq(String dataFileName, int bufferSize, Algorithm algo, int minCache, int maxCache, int stepSize, String logFileName, int randomRange) throws IOException, InterruptedException
	{
		Algorithm algorithm;
		FileWriter writer = new FileWriter("data\\logs\\" + logFileName);
		writer.write("CacheSize; PageFaults" + System.lineSeparator());
		
		double total = (maxCache - minCache) / stepSize;
		double current = 0;
		
		for(int cacheSize = minCache; cacheSize < maxCache; cacheSize += stepSize)
		{
			current++;
			InputReader reader = new InputReader("data\\" + dataFileName, bufferSize);
			algorithm = algo.create(algo.getResults().getName(), cacheSize, reader.getRange(), randomRange);	

			int[] data = reader.getNext();			
			boolean endOfFile = false;			
			while(!endOfFile)
			{							
				for(Integer page : data)
				{
					if(page > 0)
					{
						algorithm.processNextPage(page);
					}
					else
					{
//						System.out.println("EndOfFile_cacheSize: " + cacheSize);
						endOfFile = true;
						break;
					}						
				}				
				// get next Part of Data
				if(!endOfFile)
				{
					data = reader.getNext();
				}
			}						
			// Write algorithm result with current cacheSize
			writer.write(cacheSize + ";" + algorithm.getResults().getPageFaults() + System.lineSeparator());
			
			double status = (double) ((int) ((current / total * 100d) * 100)) / 100d;
			System.out.println(algo.getResults().getName() + ": " + status + " %");
		}
		writer.close();
	}	
	
	// Slow
	public void logPageFaultsSeq(String dataFileName, int bufferSize, RandomLRU2 algo, int minCache, int maxCache, int stepSize, String logFileName, int randomRange, double[] randomDistribution) throws Exception
	{
		FileWriter writer = new FileWriter("data\\logs\\" + logFileName);
		writer.write("CacheSize; PageFaults" + System.lineSeparator());
		
		double total = (maxCache - minCache) / stepSize;
		double current = 0;
		
		for(int cacheSize = minCache; cacheSize < maxCache; cacheSize += stepSize)
		{
			InputReader reader = new InputReader("data\\" + dataFileName, bufferSize);
			current++;
			RandomLRU2 algo2 = new RandomLRU2(algo.getResults().getName(), cacheSize, reader.getRange(), randomRange);
			algo2.setProbabilityDistribution(randomDistribution);
			int[] data = reader.getNext();			
			boolean endOfFile = false;			
			while(!endOfFile)
			{							
				for(Integer page : data)
				{
					if(page > 0)
					{
						algo2.processNextPage(page);
					}
					else
					{
//						System.out.println("EndOfFile_cacheSize: " + cacheSize);
						endOfFile = true;
						break;
					}						
				}				
				// get next Part of Data
				if(!endOfFile)
				{
					data = reader.getNext();
				}
			}						
			// Write algorithm result with current cacheSize
			writer.write(cacheSize + ";" + algo2.getResults().getPageFaults() + System.lineSeparator());
			
			double status = (double) ((int) ((current / total * 100d) * 100)) / 100d;
			System.out.println(algo.getResults().getName() + ": " + status + " %");
		}
		writer.close();
	}	
	
	// TODO
	public void logPageFaultsThreaded(String dataFileName, int bufferSize, Algorithm algo, int minCache, int maxCache, int stepSize, int pageRange, String logFileName) throws IOException, InterruptedException
	{
		InputReader reader = new InputReader("data\\" + dataFileName, bufferSize);
		int[] data = reader.getNext();
		
		ArrayList<AlgorithmThread> threads = new ArrayList<AlgorithmThread>();
		
		for(int cacheSize = minCache; cacheSize < maxCache; cacheSize = cacheSize + stepSize)
		{
			
		}
	}
	
	
	
	
}
