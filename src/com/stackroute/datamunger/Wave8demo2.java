package com.stackroute.datamunger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Wave8demo2 {
	public static void main(String[] args) {
		List<String> cars = Arrays.asList("maruti zen", "toyoto", "maruti swift", "i10", "maruti alto");
		long marutimodels = cars.stream().filter(car -> car.startsWith("maruti")).count();
		System.out.println("No. of maruti models : " + marutimodels);

		List<String> uppercasecars = cars.stream().map(item -> item.toUpperCase()).collect(Collectors.toList());
		System.out.println(uppercasecars);

		List<String> sortedcars = cars.stream().sorted().collect(Collectors.toList());
		System.out.println("After sorting :" + sortedcars);

		String shortest = cars.stream().min(Comparator.comparing(car -> car.length())).get();
		System.out.println("Car model with shortest name : " + shortest);

		String max = cars.stream().max(Comparator.comparing(car -> car.length())).get();
		System.out.println("Car model with longest name   :" + max);

		List<Integer> num = Arrays.asList(2, 4, 6, 8, 10, 12);
		System.out.println(num.stream().reduce(10, (a, b) -> a + b));

		List<Integer> runs = Arrays.asList(25, 30, 0, 12, 99, 201, 12, 87, 98, 11, 10);
		int totalruns = runs.stream().reduce(0, (a, b) -> b+a);

		System.out.println(totalruns);
	}
}
