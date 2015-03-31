package com.gmail.at.ivanehreshi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WordFreqCounter {
	
	private int nThreads;
	private final String text;
	private Set<String> dictionary = new HashSet<>();
	private ArrayList<String> queuedDictionary = new ArrayList<>();
	private ArrayList<Integer> firstOccurence = new ArrayList<>();
	


	private String[] words;

	private ExecutorService pool;
	private final ArrayList<Future<Integer>> results;
	private boolean computed = false;
	
	public WordFreqCounter(String text, int nThreads){
		this.text = text;
		this.nThreads = nThreads;
		results = new ArrayList<>();
	}
	
	private void splitIntoWords(){
		words = text.split("[ ,:;\\?\n\\.\\!\r]");
	}
	
	private void addWordsToDict(){
		for(int i = 0; i < words.length; i++){
			String word = words[i].toLowerCase();
			if(!dictionary.contains(word) && !word.equals("")){
				dictionary.add(word); 
				queuedDictionary.add(word);
				firstOccurence.add(i);
			}
		}

	}
	
	public void compute(){
		if(computed)
			return;
		
		splitIntoWords();
		addWordsToDict();
		
		pool = Executors.newFixedThreadPool(nThreads);
		for(int i = 0; i < queuedDictionary.size(); i++){
			results.add(
					pool.submit(new SingleWordCounter(i)));
		}
		
		
		
		computed = true;
		pool.shutdown();
	}
	
	public int getFrequency(int i){
		try {
			return results.get(i).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public ArrayList<String> getQueuedDictionary() {
		return queuedDictionary;
	}
	
	public String getDictionaryWord(int i){
		return queuedDictionary.get(i);
	}
	
	
	private class SingleWordCounter implements Callable<Integer>{

		private int ind;

		public SingleWordCounter(int ind) {
			this.ind = ind;
		}

		@Override
		public Integer call() throws Exception {
			int count = 0;
			String word = queuedDictionary.get(ind);
			int start = firstOccurence.get(ind);
			for(int i = start; i < words.length; i++){
				if(words[i].equalsIgnoreCase(word))
					count++;
			}
			
			return count;
		}
	
	}
}
