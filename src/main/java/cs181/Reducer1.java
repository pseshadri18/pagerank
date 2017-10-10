package cs181;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
 
public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	/* TODO - Implement the reduce function. 
	 * 
	 * 
	 * Input :    Adjacency Matrix Format       ->	( j   ,   M  \t  i	\t value 
	 * 			  Vector Format					->	( j   ,   V  \t   value )
	 * 
	 * Output :   Key-Value Pairs               
	 * 			  Key ->   	i
	 * 			  Value -> 	M_ij * V_j  
	 * 						
	 */

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double v_j = 0;
		ArrayList<String> mList = new ArrayList<String> ();

		// Loop through values, to add m_ij term to mList and save v_j to variable v_j
		for (Text vals : values) {
			String inputLine = vals.toString();
			String[] inputList = inputLine.split("\t");
		    if (inputList[0].equals("M")) {
		    	String m_ij = inputList[1] + "\t" + inputList[2]; // i <tab> value
		    	// String m_ij = indicesAndValue[1] + "\t" + indicesAndValue[2]; 
		    	mList.add(m_ij);
		    }
		    else {
		    	v_j = Double.parseDouble(inputList[1]);
		    }
		}
		// Then iterate through the terms in mList, to multiply each term by variable v_j.
		for (String m_ijString : mList) {
			String[] m_ijList = m_ijString.split("\t");
			String m_i = m_ijList[0];
			double m_ij = Double.parseDouble(m_ijList[1]);
			Text outputKey = new Text();
			Text outputValue = new Text();
			
			// Each output is a key-value pair  ( i  ,   m_ij * v_j)
			outputKey.set(m_i);
			outputValue.set(Double.toString(m_ij*v_j));
			context.write(outputKey, outputValue);
		}
		
	}

}
