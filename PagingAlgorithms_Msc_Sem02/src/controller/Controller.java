package controller;

import java.io.IOException;
import java.util.ArrayList;

import algorithm.Algorithm;
import simulator.PagingSimulator;

/**
 * Controller Class
 * Coordination between GUI and Simulator
 * @author Markus, Justine
 *
 */
public class Controller {
	
	private PagingSimulator pagingSimulator;
	private ArrayList<Algorithm> algorithms;
	
	public Controller()
	{
		// wir könn bei setup den PagingSimulator inizialiesieren 
		//1. GUI alle algos laden 
		//2. File und buffersize wählen --> GUI 'Start' iniszialiesiert pagingSumulator und übergibt alla algos
		//oder wir kapseln es ab
	}
	
	/**
	 *  Sets the data for the PagingSimulator
	 * @param fileName sets the input file name for the inputReader
	 * @param bufferSize sets the buffer size of the inputReader
	 * @throws Exception 
	 */
	public void setupInputData(String fileName, int bufferSize) throws Exception
	{
		this.pagingSimulator = new PagingSimulator(fileName, bufferSize);
		this.pagingSimulator.addSimulation(algorithms);
	}
	
	/**
	 * Starting Simulator
	 * @throws IOException 
	 */
	public void startSimulation() throws IOException  
	{
		this.pagingSimulator.start();
	}
	
	/**
	 * Stopping Simulator
	 * @throws InterruptedException 
	 */
	public void stopSimulation() throws InterruptedException
	{
		this.pagingSimulator.stop();
	}
	
	/**
	 * Adding a new Algorithm
	 * @param algorithm
	 */
	public void addAlgorithm(Algorithm algorithm)
	{
		this.algorithms.add(algorithm);
	}	
	
	/**
	 * Removing an Algorithm
	 * @param algorithm
	 */
	public void removeAlgorithm(Algorithm algorithm) 
	{	
		this.algorithms.remove(algorithm);
	}
	
	/**
	 * Removing an Algorithm by index
	 * @param index
	 */
	public void removeAlgorithm(int index){
		this.algorithms.remove(index);
	}
		
	/**
	 * Reset Simulator
	 */
	public void clearSimulator()
	{
		this.pagingSimulator.Clear();
	}
	
	/**
	 * TODO:
	 * Result
	 * 
	 **/
	public void getResultsFromSimulator()
	{
		
	}
}
