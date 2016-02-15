package simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/***
 * This class is the input reader for input data
 *  
 */
public class InputReader {

	private int _n; // the amount of input values in the whole file (first line in the file)
	private int _range; // the maximum possible value in the file (second line in the file)
	private int _base; // hexadecimal (16) default
	private int _bufferSize; // the amount of values from the input file
	
	private long _currentPosition; // the current line position in the file 
	
	private String _fileName; // the fileName of the input file
	private boolean _isEndOfFile;
	private BufferedReader reader;	
	
	public InputReader(String fileName, int bufferSize) throws NumberFormatException, IOException
	{
		this._base = 16;
		this._bufferSize = bufferSize;
		this._isEndOfFile = false;
		this._currentPosition = 0;
		// n		
		this.reader = new BufferedReader(new FileReader(fileName), bufferSize);
		this._n = Integer.parseInt(reader.readLine(), 10);	
		// range
		this._range = Integer.parseInt(reader.readLine(), _base) + 1;		
	}
	
	public InputReader(String fileName, int bufferSize, int base) throws NumberFormatException, IOException
	{
		this._base = base;
		this._bufferSize = bufferSize;
		this._isEndOfFile = false;
		this._currentPosition = 0;		
		// n
		this.reader = new BufferedReader(new FileReader(fileName), bufferSize);
		this._n = Integer.parseInt(reader.readLine(), 10);		
		// range
		this._range = Integer.parseInt(reader.readLine(), base) + 1;	
	}
	
	/**
	 * This method reads the specified amount of lines and returns the values as array
	 * @return returns an array with input values of size 'packageSize'
	 */
	public int[] getNext() throws IOException
	{	
		if(_isEndOfFile)
			return null;
		
		int[] data = new int[this._bufferSize];

		String value;
		
		for(int i = 0; i < this._bufferSize; i++)
		{ 
			value = reader.readLine();
			if(value != null)
			{
				data[i] = Integer.parseInt(value, _base);
				_currentPosition++;			
				
				if(_currentPosition >= _n)
				{
					_isEndOfFile = true;
//					System.out.println("EndOfFile: " + _currentPosition);
					this.reader.close();
					break;
				}
			}
			else
			{
				break;
			}
		}	
		
		return data;
	}
	
	/**
	 * This method resets the input file position
	 */
	public void reset() throws IOException
	{
		 this._isEndOfFile = false;
		 this._currentPosition = 0;
		 // n		 
		 this.reader = new BufferedReader(new FileReader(_fileName));
		 this._n = Integer.parseInt(reader.readLine(), _base);
		 // range
		 this._range = Integer.parseInt(reader.readLine(), _base) + 1;	
	}	

	public int getRange()
	{
		return this._range;
	}
	
	public boolean isEndOfFile()
	{
		return _isEndOfFile;
	}
}
