package simulator;

import algorithm.Algorithm;
import algorithm.pagingAlgorithm.PagingAlgorithmResult;

/**
 * This class represents the AlgorithmThread
 * All algorithms will be handled by an AlgorithmThread
 * 
 * @author Markus
 *
 */
public class AlgorithmThread implements Runnable {

	private boolean _hasData;
	private boolean _isRunning;
	private boolean _isFinished;
	private int[] _data;
	private Thread _worker;	
	private Algorithm _algorithm;
	private long _processTime;
	
	/**
	 * This is the AlgorithmThread constructor
	 * @param algorithm the algorithm, which the thread will perform
	 */
	public AlgorithmThread(Algorithm algorithm)
	{
		this._hasData = false;
		this._isFinished = false;
		this._algorithm = algorithm;
		this._processTime = 0;
	}	

	/**
	 * This method returns true, if the Thread is waiting for data and returns false otherwise
	 */
	public boolean isWaiting() {		
		return !_hasData;
	}
	
	/**
	 * This method sets the paging data sequence for the algorithm
	 */
	public void setData(int[] data) {
		this._data = data;		
		this._hasData = true;
	}

	/**
	 * This method starts the thread, which runs this algorithm
	 */
	public void start()
	{
		_isRunning = true;
		_worker = new Thread(this);
		_worker.start();
	}
	
	/**
	 * This method stops the thread, which runs this algorithm
	 */
	public void stop()
	{
		_isRunning = false;
	}
	
	public boolean isFinished()
	{
		return _isFinished;
	}
	
	public boolean isRunning()
	{
		return _isRunning;
	}
	
	/**
	 * This method will be called by the _worker thread
	 * the algorithms will be performed here
	 */
	public void run()
	{		
		while(_isRunning)
		{
			if(_hasData)
			{
				long startTime = System.currentTimeMillis();				
				for (int i = 0; i < _data.length; i++)					
				{
					if(_data[i] != 0) // 0 is not a valid page id and means the input file has ended
					{
					// process next page
					_algorithm.processNextPage(_data[i]);	
					}
					else
					{
						// if the page is 0, then the input file has reached end of file
						_isFinished = true;
						stop();
						break;
					}
				}	
				_hasData = false;
				
				long endTime = System.currentTimeMillis();	
				// adding processTime of this turn/data set
				_processTime += (endTime - startTime);
			}
		}		
		System.out.println("Algorithm: " + _algorithm.getResults().getName() + " stopped, time: " + _processTime);
	}
	
	/**
	 * This method returns the total results of the algorithm
	 * @return the algorithm results
	 */
	public PagingAlgorithmResult getResults()
	{
		return _algorithm.getResults();
	}	

}
