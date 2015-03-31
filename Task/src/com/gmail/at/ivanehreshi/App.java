package com.gmail.at.ivanehreshi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class App implements Runnable {
	
	public static final App instance = new App();
	
	private String inputFilePath;
	private String outputFilePath;
	private Scanner scanner = new Scanner(System.in);

	private int nThreads;
	
	private App(){
		
	}
	
	private void menu(){
		System.out.print("Input file: ");
		inputFilePath = scanner.next();
		System.out.println("Output file: ");
		outputFilePath = scanner.next();
		System.out.println("Threads count: ");
		nThreads = scanner.nextInt();
	}
	

	@Override
	public void run() {
		
		menu();
		
		String text = new String();
		
		try {
			Scanner fileScanner = new Scanner(new File(inputFilePath));
			text = fileScanner.useDelimiter("\\A").next();
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		WordFreqCounter counter = new WordFreqCounter(text, nThreads);
		PrintWriter writer = null;
		try {
			 writer = new PrintWriter(outputFilePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		counter.compute();
		int c = 0;
		for(String word: counter.getQueuedDictionary()){
			writer.println(word + "\t" + counter.getFrequency(c));
			c++;
		}
		writer.close();
	}
	 
	

	public static void main(String[] args) {
		App.instance.run();
	}



	
}
