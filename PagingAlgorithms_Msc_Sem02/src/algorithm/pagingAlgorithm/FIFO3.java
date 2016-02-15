package algorithm.pagingAlgorithm;

import algorithm.Algorithm;

/**
 * This class is an implementation of the First In First Out Algorithm
 * 
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class FIFO3 extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private int _pageRange;
	private String _name;	
	private int[] _cache;
	private int _fifoIndex;
	private Boolean[] _addressSpace;
	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 * @param pageRange
	 */
	public FIFO3(String name, int cacheSize, int pageRange)
	{
		this._cacheSize = cacheSize;
		this._pageRange = pageRange;
		this._name = name;
		
		this._pageFaults = 0;
		this._fifoIndex = 0;
		
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
			
			// remove page "first out" from position _fifiIndex, if it is not empty
			if(_cache[_fifoIndex] > -1)
			{
				// mark the direct address as false
				_addressSpace[_cache[_fifoIndex]] = false;
			} 
			
			// add the page "first in" at position _fifoIndex
			_cache[_fifoIndex] = page;			
			// mark the direct address as true
			_addressSpace[page] = true;
			
			// manage the _fifoIndex position to simulate queue behavior
			if(_fifoIndex < _cacheSize - 1)
				_fifoIndex++;
			else
				_fifoIndex = 0;
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
		this._fifoIndex = 0;
		
		this._cache = new int[_cacheSize];		
		for(int i = 0; i < _cacheSize; i++)
			_cache[i] = -1;
		
		this._addressSpace = new Boolean[_pageRange + 1];
		for(int i = 0; i < _pageRange + 1; i++)
			_addressSpace[i] = false;		
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new FIFO3(name, cacheSize, pageRange);
	}

}
