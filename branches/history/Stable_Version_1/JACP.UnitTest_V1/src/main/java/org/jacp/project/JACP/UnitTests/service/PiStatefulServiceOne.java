package org.jacp.project.JACP.UnitTests.service;

import java.awt.event.ActionEvent;

import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.component.AStateComponent;

public class PiStatefulServiceOne extends AStateComponent {
	private static Long num_rects = 1000000L;
	private double mid_1, heigt_1, widht_1, sum_1 = 0.0;
	public static int CPU_COUNT=4;
	@Override
	public Object handleAction(IAction<ActionEvent, Object> action) {
		if(action.getLastMessage().equals("startProcessCalculation")) {
			// start PI calculation; divide num_rects to equal amount
			//
		} else if(action.getLastMessage().equals("collectSubResults")){
			// collect all subresults an add 
			//for(double i:array2) {
			// sum+=i;
			//}
		}
		if(action.getLastMessage().equals("startSequentialProcessing")) {
			// start conventional pi computation
			final long startTime = System.currentTimeMillis();		
			computePIConventional();
			final long stopTime = System.currentTimeMillis();
			System.out.println("stop Time PI Conventional for num_rects: 100000000L: " + (stopTime - startTime));
		} else if(action.getLastMessage().equals("oneServiceButtonBottomOne")) {
			widht_1 = 1.0/(double) num_rects;
			
			
			
			for(int i = 5000; i < num_rects; i++) {
				mid_1 = (i + 0.5) * widht_1;
				heigt_1 = 4.0/(1.0 + mid_1 * mid_1);
				sum_1 += heigt_1;
			}
		}

		return null;
	}
	
	
	private void computePIConventional(){
		double mid, heigt, widht, sum= 0.0;
		double sum3= 0.0;
		double area;
		long startTime = System.currentTimeMillis();		
		widht = 1.0/(double) num_rects;
		double[] array1 =new double[100000];
		for(int i = 0; i < 100000; i++) {
			mid = (i + 0.5) * widht;
			heigt = 4.0/(1.0 + mid * mid);
			array1[i]=heigt;
	
		}


		double[] array2 =new double[1000000];
		for(int i = 100000; i < num_rects; i++) {
			mid = (i + 0.5) * widht;
			heigt = 4.0/(1.0 + mid * mid);
			array2[i]=heigt;
		}

		for(double i:array1) {
			sum+=i;
		}

		for(double i:array2) {
			sum+=i;
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("stop Time: " + (stopTime - startTime));
		
		startTime = System.currentTimeMillis();		
		for(int i = 0; i < num_rects; i++) {
			mid = (i + 0.5) * widht;
			heigt = 4.0/(1.0 + mid * mid);
			sum3 += heigt;
		}

		System.out.println("Ergebnis_01: "+ sum);
		System.out.println("Ergebnis_02: "+ sum3);		

		stopTime = System.currentTimeMillis();
		System.out.println("stop Time_2: " + (stopTime - startTime));
		/*
		sum2 += sum;
		
		
		area = widht * sum2;

		System.out.println("Ergebnis: "+ area);
		

		
		area = widht * sum3;

		System.out.println("Ergebnis::: "+ area);
		*/
	}

}
