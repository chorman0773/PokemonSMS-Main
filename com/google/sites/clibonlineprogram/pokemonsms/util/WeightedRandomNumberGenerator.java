package com.google.sites.clibonlineprogram.pokemonsms.util;

import java.util.Random;

public class WeightedRandomNumberGenerator {

	private Random base;
	public WeightedRandomNumberGenerator(Random base) {
		this.base = base;
	}
	public int nextWeighted(int[] weights) {
		int totalWeight = 0;
		int weight;
		for(int  i = 0;i<weights.length;i++)
			totalWeight += weights[i];
		weight = base.nextInt(totalWeight);
		for(int i = 0;i<weights.length;i++)
			if(weights[i]<weight)
				return i;
			else
				weight -= weights[i];
		return -1;
	}
	public int nextWeighted(double[] weights) {
		double totalWeight = 0;
		double weight;
		for(int  i = 0;i<weights.length;i++)
			totalWeight += weights[i];
		weight = base.nextDouble()*totalWeight;
		for(int i = 0;i<weights.length;i++)
			if(weights[i]<weight)
				return i;
			else
				weight -= weights[i];
		return -1;
	}

}
