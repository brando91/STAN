package it.disco.unimib.stan.experiments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class ReservoirSampling<T> {

	public HashMap<Integer, ArrayList<T>> samples(double percentage, ArrayList<T> items) {
		HashMap<Integer, ArrayList<T>> samples = new HashMap<Integer, ArrayList<T>>();
		int k = (int) getKDimension(percentage, items);
		int id = 1;
		while(items.size() >= k && k > 0){
			samples.put(id, sample(k, items));
			id++;
		}
		if(items.size() > 0){
			samples.put(id, items);
		}
		return samples;
	}
	
	public ArrayList<T> getAtMostSamples(int limit, ArrayList<T> items) {
		return (items.size() <= limit) ? items : sample(limit, items);
	}
	
	public ArrayList<T> getAtMostDistinctSamples(int limit, ArrayList<T> items) {
		return getAtMostSamples(limit, distinct(items));
	}

	private ArrayList<T> distinct(ArrayList<T> samples) {
		HashSet<T> unique = new HashSet<T>();
		for(T sample : samples){
			unique.add(sample);
		}
		return new ArrayList<T>(unique);
	}

	private ArrayList<T> sample(int k, ArrayList<T> items) {
		ArrayList<T> R = new ArrayList<T>();
		ArrayList<Integer> alreadyAdded = new ArrayList<Integer>();
		
		for(int i = 1; i <= k; i++){
			R.add(items.get(i-1));
			alreadyAdded.add(i-1);
		}
		for(int i = k+1; i < items.size(); i++){
			int j = randInt(1, i);
			if(j <= k){
				R.set(j-1, items.get(i-1));
				alreadyAdded.set(j-1, i-1);
			}
		}
		Collections.sort(alreadyAdded);
		for(int i = alreadyAdded.size()-1; i >= 0; i--){
			int integer = alreadyAdded.get(i);
			items.remove(integer);
		}
		return R;
	}

	private long getKDimension(double percentage, ArrayList<T> items) {
		return Math.round(items.size()*percentage);
	}

	private int randInt(int min, int max) {
	    return new Random().nextInt((max - min) + 1) + min;
	}
}
