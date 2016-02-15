import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import controller.Helper;
import algorithm.pagingAlgorithm.PagingAlgorithmResult;
import simulator.InputReader;
import simulator.PagingSimulator;
import algorithm.Algorithm;
import algorithm.pagingAlgorithm.FIFO;
import algorithm.pagingAlgorithm.LRU0;
import algorithm.pagingAlgorithm.LRU1;
import algorithm.pagingAlgorithm.LRU2;
import algorithm.pagingAlgorithm.LRU3;
import algorithm.pagingAlgorithm.LRU4a;
import algorithm.pagingAlgorithm.LRU4b;
import algorithm.pagingAlgorithm.OnMin;
import algorithm.pagingAlgorithm.FIFO;
import algorithm.pagingAlgorithm.FIFO2;
import algorithm.pagingAlgorithm.FIFO3;
import algorithm.pagingAlgorithm.RANDOM;
import algorithm.pagingAlgorithm.RANDOM2;
import algorithm.pagingAlgorithm.RandomLRU;
import algorithm.pagingAlgorithm.RandomLRU2;

/**
 * Simulator for Paging Algorithm
 * @author Markus, Justine
 *
 */
public class Main {

	public static void main(String[] args) 
	{
		try 
		{						
//			Controller control = new Controller();
//			SWTDisplay display = new SWTDisplay();
//			InputReader reader = new InputReader("data\\compressxx", 8000000);
			
			Helper helper = new Helper();
//			int cacheSize = 50;
			double[] distribution1 = { 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024 };
			double[] distribution2 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 55 };
			double[] distribution3 = { 0, 0, 0, 0, 0, 0, 0, 1, 9, 90 };
			double[] normalRandomDistribution = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
			double[] distribution40 = { 2, 2, 2, 2, 4, 4, 4, 4, 8, 8, 8, 8, 16, 16, 16, 16, 32, 32, 32, 32, 64, 64, 64, 64, 128, 128, 128, 128, 256, 256, 256, 256, 512, 512, 512, 512, 1024, 1024, 1024, 1024 };
			
			// range von compressxx = 920
					
			String data = "gnuplotxx";
//			gnuplotxx -> 7719 versch. pages
			
			helper.logPageFaultsSeq(data,  100000000, new OnMin("ONMIN_" + data, 0, 0, 25), 25, 1500, 10, "ONMIN_" + data + ".csv", 10);	
			helper.logPageFaultsSeq(data,  100000000, new LRU4a("LRU4a_" + data, 0, 0), 25, 1500, 25, "LRU4a_" + data + ".csv", 10);	
			helper.logPageFaultsSeq(data,  100000000, new FIFO3("FIFO3_" + data, 0, 0), 25, 1500, 25, "FIFO3_" + data + ".csv", 10);	
			helper.logPageFaultsSeq(data,  100000000, new RANDOM2("RANDOM2_" + data, 0, 0), 25, 1500, 25, "RANDOM_" + data + ".csv", 10);
			
			helper.logPageFaultsSeq(data,  100000000, new RandomLRU2("RandomLRU2_" + data, 0, 0, 0), 25, 1500, 25, "RandomLRU2_" + data + "_distribution1_range_10.csv", 10, distribution1);
			helper.logPageFaultsSeq(data,  100000000, new RandomLRU2("RandomLRU2_" + data, 0, 0, 0), 25, 1500, 25, "RandomLRU2_" + data + "_distribution1_range_40.csv", 10, distribution40);
			
			
			
			
//			helper.logPageFaultsSeq("compressxx",  100000000, new RandomLRU("RandomLRU_compressxx", 0, 0, 10), 10, 920, 10, "RandomLRU_compressxx.csv", 10);
			
//			helper.logPageFaultsSeq("compressxx",  100000000, new RandomLRU2("RandomLRU2_compressxx", 0, 0, 0), 10, 500, 10, "RandomLRU2_compressxx_distribution1_range_10.csv", 10, distribution1);
//			helper.logPageFaultsSeq("compressxx",  100000000, new RandomLRU2("RandomLRU2_compressxx", 0, 0, 0), 10, 500, 10, "RandomLRU2_compressxx_distribution2_range_10.csv", 10, distribution2);
//			helper.logPageFaultsSeq("compressxx",  100000000, new RandomLRU2("RandomLRU2_compressxx", 0, 0, 0), 10, 500, 10, "RandomLRU2_compressxx_distribution3_range_10.csv", 10, distribution3);
			
			
			
			
//			helper.logPageFaultsSeq("compressxx",  100000000, new RANDOM2("RANDOM2_compressxx", 0, 0), 10, 920, 10, "RANDOM2_compressxx.csv", 10);
			
			

			
//
//			
//			RandomLRU2 rndLRU = new RandomLRU2("RandomLRU2", cacheSize,reader.getRange(), 10);				
//
// 			rndLRU.setProbabilityDistribution(distribution);
//			speedTestRandomLRU(data, rndLRU, 1, distribution);
//			
//			helper.SpeedTest("grobnerxx", 10000000, new RandomLRU("RandomLRU", cacheSize, reader.getRange(), 10));
//			helper.SpeedTest("grobnerxx", 10000000, new LRU4a("LRU4a", cacheSize, reader.getRange()));
//			
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new LRU0("LRU0", 50), 5, 70, 1, 67, "LRU0.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new LRU1("LRU1", 50), 5, 70, 1, 67, "LRU1.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new LRU2("LRU2", 50), 5, 70, 1, 67, "LRU2.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new LRU3("LRU3", 50, 67), 5, 70, 1, 67, "LRU3.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new LRU4a("LRU4a", 50, 67), 5, 70, 1, 67, "LRU4a.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new LRU4b("LRU4b", 50, 67), 5, 70, 1, 67, "LRU4b.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new FIFO("FIFO", 50), 5, 70, 1, 67, "FIFO.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new FIFO2("FIFO2", 50, 67), 5, 70, 1, 67, "FIFO2.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new FIFO_M("FIFO_M", 50, 67), 5, 70, 1, 67, "FIFO_M.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new RANDOM("RANDOM", 50, 67), 5, 70, 1, 67, "RANDOM.csv");
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new RANDOM_M("RANDOM_M", 50, 67), 5, 70, 1, 67, "RANDOM_M.csv");			
//			helper.logPageFaultsSeq("grobnerxx", 10000000, new OnMin("ONMIN", 50, 67, 0), 5, 70, 1, 67, "ONMIN.csv");
			
		} 
		catch (Exception e) {

			e.printStackTrace();
		}
	}

	private static void speedTestRandomLRU(int data[], RandomLRU2 algo, int forCount, double[] distribution) throws Exception
	{
		long time_a = 0;
		PagingAlgorithmResult result;
		
		if(distribution != null)
			algo.setProbabilityDistribution(distribution);
		
			time_a = System.currentTimeMillis();

			for(int i = 0; i < forCount; i++)
			{
				algo.reset();
//				int counter = 0;
				for(int page : data)
				{
					if(page <= 0)
						break;
					algo.processNextPage(page);
				}
			}

			time_a = System.currentTimeMillis() - time_a;
			
			result = algo.getResults();
			System.out.println(result.getName() 
					+ " CacheSize: " + result.getCacheSize()
					+ " PageFaults: " + result.getPageFaults() 
					+ " Time: " + time_a);	
			
			Thread.sleep(200);
		}
}
	

