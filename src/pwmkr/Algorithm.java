package pwmkr;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

// This class describes the algorithm used for all cael-hammer projects
// Slight changes might exist due to performance issues
public class Algorithm {

	private Integer Aprime, Bprime, Cprime, Dprime;
	private BigInteger bfi;
	
	private ArrayList<String> inRAMFile;
	
	// Unique constructor for this project to help with the overhead of needing to read a file on every call to getPW
	// Reads the file supplied into memory
	public Algorithm(String filename)
	{
		if(inRAMFile == null || inRAMFile.isEmpty())
		{
			File readFile = new File(filename + ".txt");
			Scanner scan;
			try
			{
				scan = new Scanner(readFile);
				inRAMFile = new ArrayList<String>();
				
				while (scan.hasNextLine())
				{
					inRAMFile.add(scan.nextLine());
				}
				scan.close();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	// Manages the inputs to the algorithm and generates the BigInteger that eventually determines the password
	private void setValues(String A, String B, Integer C, Integer D)
	{
		// Mask the vales of the input strings
		Aprime = Math.abs(A.hashCode());
		Bprime = Math.abs(B.hashCode());

		// Further mask the input values
		Cprime = Math.abs((B + C.toString()).hashCode());
		Dprime = Math.abs((A + D.toString()).hashCode());

		// Take the multiplication of Cprime and Dprime
		bfi = BigInteger.valueOf(Cprime);
		bfi = bfi.multiply(BigInteger.valueOf(Dprime));

		BigInteger temp = BigInteger.valueOf(Aprime).multiply(BigInteger.valueOf(Bprime));
		
		bfi = bfi.add(temp);
		
		String bfiString = bfi.toString();

		// Remove trailing 0s from the BigInteger
		while (bfiString.endsWith("0"))
		{
			bfiString = bfiString.substring(0, bfiString.length() - 1);
		}
		bfi = new BigInteger(bfiString);

		// Square the BigInteger until it has over 500 digits
		while (bfiString.length() < 500)
		{
			bfi = bfi.multiply(bfi);
			bfiString = bfi.toString();
		}
	}
	
	// Pulls the password from the file given the inputs specified
	public String getPW(String filename, String input1, String input2, int row, int col,
			int length) throws FileNotFoundException
	{
		setValues(input1,input2,row,col);
		
		// Get the rows/columns of the input file
		int rows = inRAMFile.size();
		int columns = inRAMFile.get(0).length();

		// Get the initial starting positions for reading the file
		int nrow = Math.abs((Aprime * row) % rows);
		int ncol = Math.abs((Bprime * col) % columns);
		int nLength = Math.abs((Cprime * length) % (rows * columns));
		
		// This is here in case the mod above returns low values which lead to high duplication
		if(nLength < length)
		{
			nLength = length * rows;
		}
		
		// Pull nLength characters in order from the file
		// Note that nLength might be larger than the total number of characters in the file
		// This section may need changing in the future
		char[] offset = new char[nLength];
		for (int i = 0; i < nLength; i++)
		{
			offset[i] = inRAMFile.get((nrow + (ncol + i) / columns) % rows)
					.charAt((ncol + i) % columns);
		}

		String bfiString = bfi.toString();

		ArrayList<String> subs = new ArrayList<String>();

		// Split the BigInteger's string representation into length number of parts
		for (int i = 0; i < length; i++)
		{
			subs.add(bfiString.substring(i * bfiString.length() / length, (i + 1)
					* bfiString.length() / length));
		}

		char[] pw = new char[length];

		// Construct the password string
		for (int i = 0; i < length; i++)
		{
			// Read the i'th substring of the BigInteger string
			BigInteger tempBig = new BigInteger(subs.get(i));
			// Mod the above value by nLength to get a position in offset
			tempBig = tempBig.mod(BigInteger.valueOf(nLength));
			Integer temp = new Integer(tempBig.toString());

			pw[i] = offset[temp];
		}
		// Return the SLAB!!!
		String theSlab = new String(pw);
		return theSlab;
	}
}