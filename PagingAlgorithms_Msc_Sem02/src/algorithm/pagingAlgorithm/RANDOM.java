package algorithm.pagingAlgorithm;

import java.util.ArrayList;

import algorithm.Algorithm;

/**
 * This class is an implementation of the RANDOM paging algorithm
 * 
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class RANDOM extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private int _pageRange;
	private String _name;
	
	private ArrayList<Integer> _cache;
	private boolean[] _addressSpace;
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 * @param pageRange
	 */
	public RANDOM(String name, int cacheSize, int pageRange)
	{
		this._pageFaults = 0;
		this._cacheSize = cacheSize;
		this._pageRange = pageRange;
		this._name = name;
		
		
		this._cache = new ArrayList<Integer>();
		
		this._addressSpace = new boolean[pageRange + 1];
		//inizial direct addressing with -1
		for(int i = 0; i < _pageRange + 1; i++)
		{
			this._addressSpace[i] = false;
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
		if(this._addressSpace[page] == true)
		{
			//is in cache - do nothing
			
		}
		else
		{				
			++_pageFaults;	
	
			// if cache is full choose random to be deleted
			if(_cache.size() >= _cacheSize)	{
				int rand = (int) (Math.random() * this._cacheSize);
				
				//remove first page & add new one in direct addressing
				this._addressSpace[this._cache.get(rand)] = false;
				this._addressSpace[page] = true;
				
				//add new page
				_cache.remove(rand); // remove the random page
				_cache.add(page);
								
			}
			else {
				_cache.add(page);
				this._addressSpace[page] = true;
				
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
		this._cache = new ArrayList<Integer>();
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new RANDOM(name, cacheSize, pageRange);
	}
}
