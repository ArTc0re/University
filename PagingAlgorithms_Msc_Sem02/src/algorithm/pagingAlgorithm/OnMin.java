package algorithm.pagingAlgorithm;

import java.util.Random;

import algorithm.Algorithm;

/***
 * 
 * @author Dr. Andrei Negoescu , modified by Markus Strobel
 *
 */
public class OnMin extends Algorithm {

	private String name;
	private int absCounter; // ??
	private int requestCounter; // the current request count
	private double parameter; // ???
	private int pageRange; // the max range of pages
	private int cacheSize; // memory size
	
	private int delsum=0; // ???
	private int s=0; // ???
	
	double alpha;   // ??
	double beta; // ??
	
	private int[] cache; // memory	
	private int[] set_del; // important for partition
	private int[] priorityArray; // priority
	private int[] last_s0;   // timestamp of last request from S0
	private int[] lastRequest; // The last requests, needed to find Li
	private int[] freq; // ???
	
	private int froms0; // ??
	private int fromsi; // ??
	private int fromsk; // ??
	
	private int occ=0; // ??
	private int opt_pf=0; // ??
	private int optpf=0; // ??	
	private double pf; // pageFaults
	
	private Random rng; // random number generator
	
	private int lif=0; // ??	
	private int i1pf, i2pf; // ??
	
	public OnMin(String name, int cacheSize, int pageRange, double parameter)
	{
		this.name = name + "_parameter " + parameter + "_";
		this.absCounter = 0;
		this.requestCounter = 0;
		this.alpha = parameter;
		
		this.parameter = parameter;	
		this.pageRange = pageRange;
		this.cacheSize = cacheSize;
		
		this.cache = new int[cacheSize];
		this.set_del = new int[cacheSize + 1];
		this.set_del[cacheSize] = Integer.MAX_VALUE;	
		
		this.last_s0 = new int[pageRange + 1];
				
		this.priorityArray = new int[pageRange + 1];
		this.lastRequest = new int[pageRange + 1];
		for (int i=0; i<pageRange + 1; i++)
		{
			priorityArray[i] = -1;
			lastRequest[i] = -1;
		}		
		
	}
	
	@Override
	public void processNextPage(int page) {
		requestCounter++;
		
		priorityArray[page] = (int) (alpha * (-last_s0[page]) + (1 - alpha) + requestCounter);
		
		if(lastRequest[page] >= set_del[cacheSize - 1])
		{
			fromsk++;
			return;
		}		
		
		int fromSet = findSet(page);
		
//		if(fromSet == 0)
//			requestCounter++;	
		int p=page;

		if (fromSet!=0)
			this.fromsi++; 
		else 
			this.froms0++;
			      
		int pos= inMem(page);
		lastRequest[p] = requestCounter;
		
		if ((occ < cacheSize) && (pos > -1)) 
		{
			return;
		}
		
		if (occ < cacheSize)
		{ 
			cache[occ] = p; 
			occ++; 
			this.pf++;  
			this.opt_pf++;	 
			return; 
		} 
		
		if (pos > -1)
		{               
			// found page, update 
			for (int i = pos; i < occ-1; i++) 
				cache[i] = cache[i+1]; 
			cache[occ-1] = p; // move p to the right
			if (fromSet != cacheSize && fromSet != 0)
			{ 
				updateSeti(fromSet); 
			}
			if (fromSet == 0) 
				this.updateSet0();
			return;
		}
		
		if (fromSet==0)
		{   
			// from S0 and not in mem, replacement to DO			
			opt_pf++;
			pf++;		
			int kick = this.getMin(cacheSize);
						
			for (int i = kick; i < cacheSize - 1; i++) 
				cache[i] = cache[i+1];
			cache[cacheSize - 1] = p;
			updateSet0();
			last_s0[page] = this.requestCounter;			
			return;
		}
		
		//		int j = findDiffuseJ(fromSet);
		int j = findj(fromSet);
		
		int kick;
		kick = this.getMin(j+1);
		//  System.out.println("OnMIn from Si: " + memory[kick] + " by " + p);	
		
		for (int i = kick; i < cacheSize - 1; i++) 
			cache[i] = cache[i+1];
		cache[cacheSize - 1] = p;
		updateSeti(fromSet);
		this.i2pf++;
		pf++;
		lif++;		
	}
	
	private int findSet(int p){
		int lr=lastRequest[p];
		int actSet=0;		
		
		for (int i=0; i < cacheSize; i++)
		{
			actSet=i;
			if (lr < set_del[i]) break;			
		}		
		
		if (lr < set_del[cacheSize - 1]) 
			return actSet; 
		else return cacheSize;		
	}
	
	private int inMem(int p){
		for (int i=0; i < occ; i++)
		{
			if (cache[i] == p) 
				return i;			
		}
		return -1;
	}
	
	private void updateSeti(int j)
	{   
		// page requeseted from Si
		for (int i = j - 1; i < cacheSize - 1; i++)
			set_del[i] = set_del[i+1];
	}
	
	private void updateSet0()
	{
		// page requested from S0
		set_del[cacheSize - 1] = requestCounter;		
	}
	
	private int getMin(int j)
	{
		int minpos=0; 
		long minvalue = priorityArray[cache[0]];
		//this.dynPrio(memory[0]);//
		int aux;		
	
		for (int i=0; i<j; i++)
		{
			aux = priorityArray[cache[i]]; 
			//this.dynPrio(memory[i]);//
			if (aux < minvalue) 
			{
				minpos=i; 
				minvalue=aux;
			}
		}
		return minpos;
   }
	
	private int findj(int si){

		int pos=0;
		for (int i=si-1; i < cacheSize ;i++)   // traverse mem
		{
			pos=i;
			if (inSetI(cache[i],i+1) && lastRequest[cache[i]]>= set_del[si-1]) break;							
		}	
		return pos;
	}
	
	private boolean inSetI(int page, int setno)
	{
		if(setno==0)
		{ 
			if (lastRequest[page] < set_del[setno]) 
				return true; 
			else 
				return false;
		}
		
		if (this.set_del[setno-1] <= lastRequest[page] && lastRequest[page] < set_del[setno]) 
			return true;
		return false;
	}

	@Override
	public PagingAlgorithmResult getResults() {
		return new PagingAlgorithmResult(name, opt_pf, cacheSize);
	}
	
	@Override
	public void reset() {
		this.absCounter = 0;
		this.requestCounter = 0;
				
		this.cache = new int[cacheSize];
		this.set_del = new int[cacheSize + 1];
		this.set_del[cacheSize] = Integer.MAX_VALUE;	
		
		this.last_s0 = new int[pageRange];
				
		this.priorityArray = new int[pageRange];
		this.lastRequest = new int[pageRange];
		for (int i=0; i<pageRange; i++)
		{
			priorityArray[i] = -1;
			lastRequest[i] = -1;
		}		
	}

	@Override
	public Algorithm create(String name, int cacheSize, int pageRange, int randomRange) {
		return new OnMin(name, cacheSize, pageRange, 0);
	}
}
