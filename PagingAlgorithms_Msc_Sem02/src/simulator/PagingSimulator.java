package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algorithm.Algorithm;
import algorithm.pagingAlgorithm.PagingAlgorithmResult;

/**
 * This class is the Simulator for the Paging Algorithms
 * @author Markus Strobel
 * @email mksstrobel@googlemail.com
 */
public class PagingSimulator implements Runnable{
	
	private boolean _isRunning;
	
	private Thread _worker;	
	private List<AlgorithmThread> _tasks;
	
	private InputReader inputReader;
	private int[] pagingData;
	private int pageRange;

	
	/**
	 * Standalone constructor
	 */
	public PagingSimulator(String fileName, int bufferSize) throws Exception
	{
		_isRunning = false;
		_tasks = new ArrayList<AlgorithmThread>();

		initInputReader(fileName, bufferSize);
	}
		
	/**
	 * This constuctor requires an lazy execution of initInputReader, otherwise there is no input defined
	 * i.e. setting up the input file configuration in the gui or otherwise
	 */
	public PagingSimulator() throws Exception
	{
		_isRunning = false;
		_tasks = new ArrayList<AlgorithmThread>();
	}	
	
	/**
	 * This method initializes the InputReader and its source for the input file.
	 * 
	 * @param fileName the file path
	 * @param bufferSize the buffer size of the buffered reader, each time it reads from the hard disc, it will read this amount of chars
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void initInputReader(String fileName, int bufferSize) throws NumberFormatException, IOException
	{
		inputReader = new InputReader(fileName, bufferSize, 16);
		
		pageRange = inputReader.getRange();
		
		// initial array load
		pagingData = inputReader.getNext();
	}
	
	public int getPageRange()
	{
		return this.pageRange;
	}

	
	/**
	 * This method starts the PagingSimulator
	 * @throws IOException
	 */
	public void start() throws IOException
	{				
		_isRunning = true;
		_worker = new Thread(this);
		_worker.start();
	}
	
	/**
	 * This method stops all AlgorithmThreads and the PagingSimulator
	 * @throws InterruptedException 
	 * 
	 */
	public void stop() throws InterruptedException
	{	
		_isRunning = false;	
		int statusCount = 0;
		
		boolean threadsRunning = false;
		while(!threadsRunning)
		{
			threadsRunning = true;

			int count = 0;
			
			for(AlgorithmThread thread : _tasks)
			{
				boolean status = thread.isRunning();
				
				if(!status)
					count++;				
				threadsRunning &= !status; // if all threads are NOT running then this will be true, and then we will leave the while loop
			}
			
			if(count > statusCount)
			{
				statusCount = count;
				double percent = count / _tasks.size() * 100d;
				System.out.print("Progress... " + percent + " %");
			}
			
			Thread.sleep(100);
		}	
		
		
	}

	public void run()
	{				
		// starting all AlgorithmThreads
		for(AlgorithmThread thread : _tasks)
		{
			thread.setData(pagingData);
			thread.start();
		}		
		
		boolean waiting = false;
		while(_isRunning)
		{			
			if(waiting)
			{
				try {
					// checks for end of file
					if(!inputReader.isEndOfFile())
					{ 
						pagingData = inputReader.getNext(); // get next int[] of data						
						for(AlgorithmThread thread : _tasks)
						{
							thread.setData(pagingData); // sets int[] data and _hasData = true
						}
					}
					else
					{
						System.out.println("System stopped");
						stop();
					}
				} 
				catch (IOException e) {	e.printStackTrace(); } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{			
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				waiting = true;
				for (AlgorithmThread thread : _tasks)		
				{
					waiting &= thread.isWaiting();
				}
			}			
		}
	}	
	
 	public void addSimulation(Algorithm algorithm)
	{
 		AlgorithmThread thread = new AlgorithmThread(algorithm); 		
 		
		synchronized (_tasks) 
		{
			_tasks.add(thread);
		}
	}
	
	public void addSimulation(List<Algorithm> algorithmsList)
	{
		for(Algorithm algorithm : algorithmsList)
		{
			AlgorithmThread thread = new AlgorithmThread(algorithm);
		
			synchronized (_tasks) 
			{
				_tasks.add(thread);
			}
		}
	}

	public void Clear()
	{
		_tasks.clear();
		_isRunning = false;
		_worker = null;
	}		
	
	public boolean isRunning()
	{
		return this._isRunning;
	}

	public List<PagingAlgorithmResult> getResults()
	{
		List<PagingAlgorithmResult> results = new ArrayList<PagingAlgorithmResult>();
		for(AlgorithmThread thread : _tasks)
		{
			results.add(thread.getResults());
		}		
		return results;
	}	
}
