package algorithm.pagingAlgorithm;

import java.util.LinkedHashSet;

import algorithm.Algorithm;

/**
 * This class is an implementation of the Least Recently Used Algorithm
 * Cache simulated via LinkedHashSet
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class LRU2 extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private String _name;
	
	private LinkedHashSet<Integer> _cache;
	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 */
	public LRU2(String name, int cacheSize)
	{
		this._pageFaults = 0;
		this._cacheSize = cacheSize;
		this._name = name;
		
		this._cache = new LinkedHashSet<Integer>();
	}
	
	/**
	 * Processing Next Page
	 * Check for Hit and Faults
	 */
	@Override
	public void processNextPage(int page) {
		
		boolean hit = _cache.contains(page);

		if(hit)
		{
			_cache.remove(page);
			_cache.add(page);		
		}
		else
		{				
			++_pageFaults;			
	
			// if cache is full, remove an item and add the requested page
			if(_cache.size() >= _cacheSize)	{
				_cache.remove(_cache.toArray()[0]); //maybe there is a better solution, workonprogress TODO
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
		this._cache = new LinkedHashSet<Integer>();		
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new LRU2(name, cacheSize);
	}
}
