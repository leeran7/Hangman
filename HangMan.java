package edu.cuny.csi.csc330.extra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.cuny.csi.csc330.util.Randomizer;


public class HangMan {
	private String[] word;
	private List<String> lettersGuessed;
	private List<String> incorrectGuesses;
	private boolean gameInProgress;
	private int guessesRemaining = 6;
	private final char LEFT_LIMB = '/';
	private final char RIGHT_LIMB = '\\';
	private final char HEAD = 'O';
	private final  char BODY = '|';
	private String[] board;
	public static Scanner scanner = new Scanner(System.in);
	private List<String> wordChoices;
	private int guesses = 1;
	private String gameStatus;
	
	
	HangMan() throws IOException, HangManException{
		wordChoices = new ArrayList<String>();
		FileReader file = new FileReader("/Users/leeranfarin/eclipse-workspace/330project/src/edu/cuny/csi/csc330/extra/Words");
		BufferedReader buff = new BufferedReader(file);
		while (buff.readLine() != null) {
			wordChoices.add(buff.readLine());
		};
		buff.close();
		int wordIdx = Randomizer.generateInt(1, wordChoices.size() - 1);
		word = wordChoices.get(wordIdx).split("");
		lettersGuessed = new ArrayList<String>(word.length);
		incorrectGuesses = new ArrayList<String>(word.length);
		gameInProgress = true;
		generateBoard();
		
	}
	
	public void toggleGame() {
		gameInProgress = !gameInProgress;
		System.out.println(gameStatus);
	}
	
	public void startGame() throws HangManException {
		
		while(this.gameInProgress) {
			if(getGuessedLetters().replaceAll("[\\s|\\u00A0]+", "").equals(String.join("", word))) {
				System.out.println("\n\nCongrats! You won");
				System.out.println(this);
				gameInProgress = false;
				
			} else if(guessesRemaining == 0) {
				throw new HangManException(this);
			} else {
//				System.out.println("\n" + Arrays.asList(word).toString() + "\n\n");
				printBoard();
				System.out.println(getGuessedLetters());
				guessLetter();
			}
			
			
			
		}
		
		
	}
	
	private void guessLetter() {
		System.out.printf("\nYou have %d guesses left:  ", guessesRemaining);
		System.out.printf("\nGuess a Letter! Guess #%d:  ", guesses);
		String letter = null;
		while(letter == null || letter.length() > 1) {
			letter = scanner.next().toUpperCase();
		}
		if(lettersGuessed.contains(letter)) {
			System.err.print("You already Guessed this letter.. try a different one\n");
		} else {
			lettersGuessed.add(letter);
			if(!Arrays.asList(word).contains(letter)) {
				incorrectGuess();
				addLimb();
				incorrectGuesses.add(letter);
			}
		}
		guesses++;
	}
	
	private void addLimb() {
		switch(guessesRemaining) {
			case 5: addHead();
			break;
			case 4: addLeftLimb(0);
			break;
			case 3: addBody();
			break;
			case 2: addRightLimb(0);
			break;
			case 1: addLeftLimb(1);
			break;
			case 0: addRightLimb(1);
			break;
		}
	}
	
	private String getGuessedLetters() {
		StringBuilder r = new StringBuilder();
		for(String letter: word) {
			if(!lettersGuessed.contains(letter)) {
				r.append(" _ ");
			} else {
				r.append(" " + letter + " ");
			}
		}
		return r.toString();
		
	}
	
	private void generateBoard() {
		this.board = new String[12];
		this.board[0] =  ("_____________________");
		this.board[1] =  ("|                   |");
		this.board[2] =  ("|                   |");
		this.board[3] =  ("|                   |");
		this.board[4] =  ("|                   |");
		this.board[5] =  ("|                   |");
		this.board[6] =  ("|                   ");
		this.board[7] =  ("|                  ");
		this.board[8] =  ("|                  ");
		this.board[9] =  ("|                   ");
		this.board[10] = ("|                   ");
		this.board[11] = ("|____________      ");
	}
	
	private void addHead() {
		this.board[6] = this.board[6] + HEAD;
	}
	
	private void addLeftLimb(int leg) {
		this.board[7 + leg] = this.board[7 + leg] + LEFT_LIMB;
	}
	
	private void addRightLimb(int leg) {
		String space = "";
		if(leg == 1) {
			space = " ";
		}
		this.board[7 + leg] = this.board[7 + leg] + space + RIGHT_LIMB;
	}
	
	private void addBody() {
		this.board[7] = this.board[7] + BODY;
	}
	
	private String boardToString() {
		StringBuilder b = new StringBuilder();
		for(String line: board) {
			b.append(line + "\n");
		}
		return b.toString();
	}
	
	private void printBoard(){
			System.out.println(boardToString());
		
	}
	
	private void incorrectGuess() {
		this.guessesRemaining--;
	}
	
	@Override
	public String toString() {
		return "Hang Man Game: \n" + boardToString() + 
				"\n\nThe Word: " + String.join("", word) + "\n" + 
				getGuessedLetters() + 
				"\n\nIncorrect Guesses: " + String.join("", incorrectGuesses) + 
				"\nGuesses: " + guesses + "\n";
	}

	public static void main(String[] args) throws IOException, HangManException {
		boolean stillPlaying = true;
		do {
			try {
				HangMan game = new HangMan();
				game.startGame();
			} catch (Exception e) {
				e.getMessage();
			}
			System.out.println("Want to play another game? [Y]es or [N]o?");
			
			String reply = scanner.next().toUpperCase();
			while(reply.toCharArray()[0] != 'N' && reply.toCharArray()[0] != 'Y') {
				reply = scanner.next().toUpperCase();
			}
			if(reply.toCharArray()[0] == 'N') {
				stillPlaying = false;
			}
		} while(stillPlaying);
		
		System.out.println("\nThank you for playing! :)");
	}
}
