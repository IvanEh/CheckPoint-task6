package com.gmail.at.ivanehreshi.utils;

public class MutableInteger {
	public int value;
	
	public MutableInteger(int value){
		this.value = value;
	}
	
	public synchronized void inc(){
		value++;
	}
}
