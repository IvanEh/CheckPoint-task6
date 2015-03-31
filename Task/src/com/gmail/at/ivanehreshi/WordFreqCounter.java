package com.gmail.at.ivanehreshi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WordFreqCounter {
	
	private int nThreads;
	private final String text;
	private Set<String> dictionary = new HashSet<>();
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
		words = text.split(" ;\\?\n\\.\\!");
	}
	
	private void addWordsToDict(){
		for(int i = 0; i < words.length; i++)
			dictionary.add(words[i]);
	}
	
	public void compute(){
		if(computed)
			return;
		
		splitIntoWords();
		addWordsToDict();
		
		pool = Executors.newFixedThreadPool(nThreads);
		for(String key: dictionary){
			results.add(
					pool.submit(new SingleWordCounter(key)));
		}
		
		computed = true;
	}
	
	public int getFrequency(int i){
		try {
			return results.get(i).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String[] getWords() {
		return words;
	}

	
	private class SingleWordCounter implements Callable<Integer>{

		private String word;

		public SingleWordCounter(String word) {
			this.word = word;
		}

		@Override
		public Integer call() throws Exception {
			int count = 0;
			
			for(String w: words){
				if(w.equalsIgnoreCase(word))
					count++;
			}
			
			return count;
		}
	
	}
}
