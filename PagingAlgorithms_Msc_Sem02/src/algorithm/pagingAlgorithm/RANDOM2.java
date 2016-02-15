package algorithm.pagingAlgorithm;

import java.util.Random;

import algorithm.Algorithm;

/**
 * This class is an implementation of the RANDOM paging algorithm
 * 
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class RANDOM2 extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private int _pageRange;
	private String _name;
	private Random random;
	private int _randomIndex;
	
	
	private int _cacheIndex; 	// to make sure, that the cache is full
	private Boolean _cacheFull;	// to make sure, that the cache is full
	
	private int[] _cache;
	private Boolean[] _addressSpace;
	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 * @param pageRange
	 */
	public RANDOM2(String name, int cacheSize, int pageRange)
	{
		this._name = name;
		this._cacheSize = cacheSize;
		this._pageRange = pageRange;
		this.random = new Random();
				
		this._pageFaults = 0;
		this._randomIndex = -1;
		
		this._cacheIndex = 0;
		this._cacheFull = false;
		
		this._cache = new int[cacheSize];		
		for(int i = 0; i < cacheSize; i++)
			_cache[i] = -1;
		
		this._addressSpace = new Boolean[pageRange + 1];
		for(int i = 0; i < pageRange + 1; i++)
			_addressSpace[i] = false;
	}
	
	/**
	 * Processing Next Page
	 * Check for Hit and Faults
	 * 
	 * @param page the page to process - please make sure that page is > 0 or you will get an IndexOutOfBounds Exception
	 */
	@Override
	public void processNextPage(int page) {		
		// page fault
		if(!_addressSpace[page])
		{
			_pageFaults++;						
			if(_cacheFull)
			{
				_randomIndex = random.nextInt(_cacheSize);
				
				// set removed page to false
				_addressSpace[_cache[_randomIndex]] = false;
				// set new page on the position of the removed page
				_cache[_randomIndex] = page;				
				// set new page to true
				_addressSpace[page] = true;
			}
			else // to fill the cache. before random pages will be removed
			{
				_addressSpace[page] = true;
				_cache[_cacheIndex] = page;
				_cacheIndex++;
				
				if(_cacheIndex >= _cacheSize)
					_cacheFull = true;
			}
		}
	}

	/**
	 * Return results (Name, Faults, CachSize)
	 */
	@Override
	public PagingAlgorithmResult getResults() {
		return new PagingAlgorithmResult(this._name, this._pageFaults, this._cacheSize);
	}
	
	@Override
	public void reset() {
		this._pageFaults = 0;
		this._randomIndex = -1;
		
		this._cacheIndex = 0;
		this._cacheFull = false;
		
		this._cache = new int[_cacheSize];		
		for(int i = 0; i < _cacheSize; i++)
			_cache[i] = -1;
		
		this._addressSpace = new Boolean[_pageRange + 1];
		for(int i = 0; i < _pageRange + 1; i++)
			_addressSpace[i] = false;	
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new RANDOM2(name, cacheSize, pageRange);
	}
}
