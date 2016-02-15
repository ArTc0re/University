package algorithm.pagingAlgorithm;

import java.util.ArrayList;

import algorithm.Algorithm;

/**
 * This class is an implementation of the Least Recently Used Algorithm
 * Cache simulated via ArrayList
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class LRU0 extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private String _name;
	
	private ArrayList<Integer> _cache;
	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 */
	public LRU0(String name, int cacheSize)
	{
		this._pageFaults = 0;
		this._cacheSize = cacheSize;
		this._name = name;
		
		this._cache = new ArrayList<Integer>();		
	}
	
	/**
	 * Processing Next Page
	 * Check for Hit and Faults
	 */
	@Override
	public void processNextPage(int page) {		
		boolean hit = _cache.contains(page);
		
		if(hit)	{
			// remove from current position to first position
			int index = _cache.indexOf(page);
			_cache.remove(index);
			_cache.add(page);			
		}
		else
		{	
			++_pageFaults;
			// if cache is full, remove an item and add the requested page
			if(_cache.size() >= _cacheSize)	{
				_cache.remove(0); // remove least recently used item
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
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new LRU0(name, cacheSize);
	}
}
