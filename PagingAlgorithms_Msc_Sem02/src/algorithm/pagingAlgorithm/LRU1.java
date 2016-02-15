package algorithm.pagingAlgorithm;

import java.util.ArrayList;
import java.util.HashSet;

import algorithm.Algorithm;

/**
 * This class is an implementation of the Least Recently Used Algorithm
 * Cache simulated via HashSet & ArrayList
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class LRU1 extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private String _name;
	
	private ArrayList<Integer> _cacheOrder;
	private HashSet<Integer> _cache;
	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 */
	public LRU1(String name, int cacheSize)
	{
		this._pageFaults = 0;
		this._cacheSize = cacheSize;
		this._name = name;
		
		this._cache = new HashSet<Integer>();
		this._cacheOrder = new ArrayList<Integer>();
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
			int index = _cacheOrder.indexOf(page);
			_cacheOrder.remove(index);
			_cacheOrder.add(page);			
		}
		else
		{				
			++_pageFaults;			
	
			//if cache is full, remove an item and add the requested page
			if(_cache.size() >= _cacheSize)	{
				_cache.remove(_cacheOrder.get(0));
				_cacheOrder.remove(0);
				
				_cache.add(page);
				_cacheOrder.add(page);
				
			}
			else {
				_cache.add(page);
				_cacheOrder.add(page);
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
		this._cache = new HashSet<Integer>();
		this._cacheOrder = new ArrayList<Integer>();		
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new LRU1(name, cacheSize);
	}
}
