package algorithm.pagingAlgorithm;

import java.util.ArrayList;

import algorithm.Algorithm;

/**
 * This class is an implementation of the Least Recently Used Algorithm
 * Cache simulated via Int[] für direkte Addressierung und ArrayList<Integer> für cache
 * 
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class LRU4b extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private int _pageRange;
	private String _name;
	
	private int[] _addressSpace;
	private ArrayList<Integer> _cache;
	private int _timer;
	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 * @param pageRange
	 */
	public LRU4b(String name, int cacheSize, int pageRange)
	{
		this._pageFaults = 0;
		this._pageRange = pageRange;
		this._cacheSize = cacheSize;
		this._name = name;
		this._addressSpace = new int[pageRange + 1];	
		for(int i = 0; i < _pageRange + 1; i++)
		{
			this._addressSpace[i] = -1;
		}
		this._timer = 0;	
		this._cache = new ArrayList<Integer>();
		for(int i = 0; i < _cacheSize; i++)
		{
			this._cache.add(-1);
		}
		
	}
	
	/**
	 * Processing Next Page
	 * Check for Hit and Faults
	 * 
	 * @param page the page to process - please make sure that page is > 0 or you will get an IndexOutOfBounds Exception
	 */
	@Override
	public void processNextPage(int page) {		
		_timer++;
		
		if(_addressSpace[page] > 0)
		{
			_addressSpace[page] = _timer;
		}
		else
		{
			++_pageFaults;		
			
			// update the time stamp of the current page
			_addressSpace[page] = _timer;
			
			int min = Integer.MAX_VALUE;
			int oldPageAddressSpacePos = -1;
			int cachePos = -1;
			
			// find the smallest time stamp in _cache
			for(int _cachePosition = 0; _cachePosition < _cache.size(); _cachePosition++)
			{
				// check if the _cache position is empty, if it is not then we can look for a time stamp for this cache position in the address space
				if(_cache.get(_cachePosition) > -1)
				{
					// get the direct address of the _caches page in the address space from the _cache[_cachePosition]
					if(_addressSpace[_cache.get(_cachePosition)] < min)
					{
						if(_cache.get(_cachePosition) > -1)
							min = _addressSpace[_cache.get(_cachePosition)];
						oldPageAddressSpacePos = _cache.get(_cachePosition);
						cachePos = _cachePosition;
					}
				}
				else
				{
					cachePos = _cachePosition;
					min = -1;
					break;
				}
			}
			
			if(min > -1)
			{
				// the new page takes place at the old pages place in the cache			
				_cache.set(cachePos, page);					
				// reset the old page time stamp from the direct address room
				_addressSpace[oldPageAddressSpacePos] = -1;
			}
			else
			{
				// add the page		
				_cache.set(cachePos, page);				
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
		this._timer = 0;	
		
		this._addressSpace = new int[this._pageRange + 1];	
		for(int i = 0; i < _pageRange + 1; i++)
		{
			this._addressSpace[i] = -1;
		}
		
		this._cache = new ArrayList<Integer>();
		for(int i = 0; i < _cacheSize; i++)
		{
			this._cache.add(-1);
		}
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new LRU4b(name, cacheSize, pageRange);
	}
}
