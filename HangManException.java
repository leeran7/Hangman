package edu.cuny.csi.csc330.extra;

public class HangManException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HangManException(HangMan item){
			System.err.println(item.toString());
	}
}
