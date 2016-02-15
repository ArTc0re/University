package algorithm;

import algorithm.pagingAlgorithm.PagingAlgorithmResult;

/**
 * Abstract class for Algorithm
 * @author Markus, Justine
 *
 */
public abstract class Algorithm {
			
	/**
	 * This method processes the next page request and counts the page faults
	 * @param page the requested page
	 */
	public abstract void processNextPage(int page);
	
	/**
	 * This method returns the results of the algorithm in a PagingAlgorithmResults Container class
	 * @return the PagingAlgorithmResults
	 */
	public abstract PagingAlgorithmResult getResults();	
	
	/**
	 * This method resets the algorithm
	 */
	public abstract void reset();
	
	/**
	 * This method returns an instance of its own
	 * @param name the identifier/name of the instance
	 * @param cacheSize the cache size of the algorithm
	 * @param pageRange the maximum number of pages
	 * @param randomRange the range of pages which can be unload from cache
	 * @return returns the instance of the algorithm
	 */
	public abstract Algorithm create(String name, int cacheSize, int pageRange, int randomRange);
	
	
}

