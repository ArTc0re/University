package algorithm.pagingAlgorithm;

import java.util.ArrayList;

import algorithm.Algorithm;

/**
 * This class is an implementation of the First In First Out Algorithm
 * with ArrayList
 * 
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class FIFO extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private String _name;
	
	private ArrayList<Integer> _cache;

	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 */
	public FIFO(String name, int cacheSize) // pageRange not needed
	{
		this._pageFaults = 0;
		this._cacheSize = cacheSize;
		this._name = name;
		
		this._cache = new ArrayList<Integer>();
		
	}
	
	/**
	 * Processing Next Page
	 * Check for Hit and Faults
	 * 
	 * @param page the page to process - please make sure that page is > 0 or you will get an IndexOutOfBounds Exception
	 */
	@Override
	public void processNextPage(int page) {	
		boolean hit = _cache.contains(page);
		
		if(hit)
		{
			//do nothing or count hits
			
		}
		else
		{				
			++_pageFaults;			
	
			// if cache is full remove first item and add the requested page
			if(_cache.size() >= _cacheSize)	{
				_cache.remove(0); // remove last in ArrayList = first in
				_cache.add(page);
								
			}
			else {
				_cache.add(page);
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
	public Algorithm create(String Name, int cacheSize, int pageRange, int randomRange) {
		return new FIFO(Name, cacheSize);
	}

}
