package algorithm.pagingAlgorithm;

import java.util.ArrayList;
import java.util.Random;

import algorithm.Algorithm;

/**
 * This class is an implementation of the Least Recently Used Algorithm with Randomization
 * Cache simulated via Int[] für direkte Addressierung und ArrayList<Integer> für cache
 * 
 * 
 * @author Markus Strobel, Justine Smyzek
 */
public class RandomLRU2 extends Algorithm {

	private int _pageFaults;
	private int _cacheSize;
	private int _pageRange;
	private String _name;
	
	private boolean[] _addressSpace;
	private ArrayList<Integer> _cache;
	private int _timer;
	
	// this is the amount of pages which are in range of a random remove. if randomRange = 5, then the last 5 pages in the cache could be removed.
	private int _randomRange;	
	
	private boolean distributionUsed = false;
	// this array has the size of _randomRange and represents the probability of each page within the random range of being kicked out of the cache.
	private int[] _probabilityDistribution; 
	private int _probabilitySum;
	private Random random;
	
	/**
	 * 
	 * @param name
	 * @param cacheSize
	 * @param pageRange
	 */
	public RandomLRU2(String name, int cacheSize, int pageRange, int randomRange)
	{
		this._pageFaults = 0;
		this._pageRange = pageRange;
		this._cacheSize = cacheSize;
		this._name = name;
		this._addressSpace = new boolean[pageRange + 1];	
		for(int i = 0; i < _pageRange + 1; i++)
		{
			this._addressSpace[i] = false;
		}
		this._timer = 0;	
		this._cache = new ArrayList<Integer>();
		this._randomRange = randomRange;
		this._probabilityDistribution = new int[_randomRange];
		this._probabilitySum = 0;		
		this.random = new Random();
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
		
		if(_addressSpace[page])
		{
			int curIndex = _cache.indexOf(page);
			_cache.remove(curIndex);
			_cache.add(page);			
		}
		else
		{
			++_pageFaults;		
			
			// update the time stamp of the current page
			_addressSpace[page] = true;
			
			// remove old page if the cache is not full
			if(_cache.size() == _cacheSize)
			{
				int oldPageIndex;
				
				if(distributionUsed)
				{
					// get the index of the page to remove from the cache
					oldPageIndex = getPageIndexToRemove();
				}
				else
				{
					oldPageIndex = random.nextInt(_randomRange);
				}
				
				// set old page timer to -1
				_addressSpace[_cache.get(oldPageIndex)] = false;
				
				// remove the old page from cache
				_cache.remove(oldPageIndex);				
			}
			
			// add the new page to cache
			_cache.add(page);			
		}
	}

	public void setProbabilityDistribution(double[] distribution) throws Exception
	{
		if(distribution.length != _randomRange)
			throw new Exception("the distribution length has the be the same as the random range value");
		
		distributionUsed = true;
		int total = 0;
		int temp = 0;
		int[] prob = new int[_randomRange];
		for(int i = 0; i < _randomRange; i++)
		{
			temp = (int) (distribution[i] * 1000); // 0,001 -> wird zu 1, in summe würde die 100 (%) zu 100.000 werden, (der einfach halber)
			total += temp;
			
			// neuen wert abspeichern. Beispiel: prob[0] = 10, prob[1] = 140, prob[2] = 332 -> wenn random kleiner als 10, 
			// dann index = 0, wenn rnd kleiner als 140, größer als 10, dann index = 1
			prob[i] = total; 
		}
		
		// damit arbeiten wir am ende
		_probabilityDistribution = prob;
		_probabilitySum = total;
	}
		
	private int getPageIndexToRemove()
	{
		int rnd = random.nextInt(_probabilitySum);
		
		int i = 0;
		while(true)
		{
			if(rnd > _probabilityDistribution[i])
			{
				i++;
			}
			else
			{
				return (_probabilityDistribution.length - 1 - i);
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
		
		this._addressSpace = new boolean[this._pageRange + 1];	
		for(int i = 0; i < _pageRange + 1; i++)
		{
			this._addressSpace[i] = false;
		}
		
		this._cache = new ArrayList<Integer>();
		this.random = new Random();
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new LRU4b(name, cacheSize, pageRange);
	}
}
