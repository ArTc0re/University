package algorithm.pagingAlgorithm;

/**
 * This class contains the results of an algorithm
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class PagingAlgorithmResult{

	private String _name;
	private int _pageFaults;
	private int _cacheSize;
	
	/**
	 * The constructor
	 * @param name the name/identifier of the algorithm
	 * @param pageFaults the amount of page faults
	 */
	public PagingAlgorithmResult(String name, int pageFaults, int cacheSize)
	{
		this._name = name;
		this._pageFaults = pageFaults;
		this._cacheSize = cacheSize;
	}
	
	/**
	 * This method returns the name/identifier of the algorithm
	 * @return the name/identifier of the algorithm
	 */
	public String getName()	
	{
		return this._name;
	}
	
	/**
	 * This method returns the page faults
	 * @return the page faults
	 */
	public int getPageFaults()	
	{
		return this._pageFaults;
	}
	
	public int getCacheSize()
	{
		return this._cacheSize;
	}
}
