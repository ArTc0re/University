package algorithm.pagingAlgorithm;

import java.util.HashMap;
import java.util.Map.Entry;

import algorithm.Algorithm;

/**
 * This class is an implementation of the Least Recently Used Algorithm
 * Cache simulated via Boolean[] Array und HashMap<Integer,Integer>
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class LRU3 extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private int _pageRange;
	private String _name;
	
	private Boolean[] _cache;
	private int _timer;
	private HashMap<Integer, Integer> _timeStamp;
	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 * @param pageRange
	 */
	public LRU3(String name, int cacheSize, int pageRange)
	{
		this._pageFaults = 0;
		this._pageRange = pageRange;
		this._cacheSize = cacheSize;
		this._name = name;
		this._cache = new Boolean[pageRange + 1];		
		for(int i = 0; i < pageRange + 1; i++)
		{
			_cache[i] = false;
		}		
		this._timer = 0;		
		this._timeStamp = new HashMap<Integer, Integer>();
	}
	
	/**
	 * Processing Next Page
	 * Check for Hit and Faults
	 * 
	 * @param page the page to process - please make sure that page is > 0 or you will get an IndexOutOfBounds Exception
	 */
	@Override
	public void processNextPage(int page) {		
		boolean hit = false;
		hit = _cache[page];
		_timer++;

		if(hit)
		{
			_timeStamp.put(page, _timer);
		}
		else
		{
			++_pageFaults;			
			if(_timeStamp.size() >= _cacheSize)
			{
				int minKey = Integer.MAX_VALUE;
				int minValue = Integer.MAX_VALUE;
				// find the oldest value
				for(Entry<Integer, Integer> entry : _timeStamp.entrySet())
				{
					if(entry.getValue() < minValue)
					{
						minValue = entry.getValue();
						minKey = entry.getKey();
					}
				}

				// remove least recently used key/value
				_timeStamp.remove(minKey, minValue); 
				_cache[minKey] = false; 
				
				// add newest key/value
				_timeStamp.put(page, _timer);
				_cache[page] = true;
			}
			else
			{
				// add newest key/value
				_cache[page] = true;
				_timeStamp.put(page, _timer); // key, value
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
		this._cache = new Boolean[this._pageRange - 1];	
		for(int i = 0; i < this._pageRange - 1; i++)
		{
			_cache[i] = false;
		}		
		this._timer = 0;		
		this._timeStamp = new HashMap<Integer, Integer>();		
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new LRU3(name, cacheSize, pageRange);
	}
}
