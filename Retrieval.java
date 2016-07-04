import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;



class Retrieval{
	
	public static double[][] allFeatures=new double[1000][66];
	public static double[][] distances = new double[1000][2];
	
	public static void retrieve(String fileName){
		
		double[] featureVector=FeatureExtraction.extractSingleImage(fileName);
		
		double dis;
		
		for (int i=0;i<1000;i++){
			distances[i][0]=-1;
			distances[i][1]=100;
		}
		
		int k;
		double D;
		
		for (int i=0;i<1000;i++){
			k=0;
			D=euclideanDistance(featureVector, allFeatures[i]);
			
		    while (distances[k][1]<D){
		        k=k+1;        
		    }
		    for (int n=999;n>=k+1;n--){
		        distances[n][0]=distances[n-1][0];
		        distances[n][1]=distances[n-1][1];
		    }
		    distances[k][0]=i;
		    distances[k][1]=D;
		}
		
	}
	
	public static double euclideanDistance(double[] a, double[] b){
		double S=0;
		for (int i=0;i<18;i++){
			S+= Math.pow(a[i]-b[i],2);
		}
		for (int i=18;i<66;i++){
			S+= 10*Math.pow(a[i]-b[i],2);
		}
		return S;
		
	}
	
	public static void main(String args[])
	{
	      try{
	    	  FileInputStream fstream = new FileInputStream("Features.txt");
	    	  DataInputStream in = new DataInputStream(fstream);
	    	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	  String strLine;
	    	  Scanner scan;
	    	  int imgInd=0;
	    	  while ((strLine = br.readLine()) != null){
	    		  //System.out.println(imgInd);
	    		  scan=new Scanner(strLine);
	    		  int ftrInd=0;
	    		  while(scan.hasNext()){
	    			  allFeatures[imgInd][ftrInd]=scan.nextDouble();
	    			  ftrInd++;
	    		  }
	    		  imgInd++;	    		  	
	    	  }
	    	  in.close();
	      }catch (Exception e){
	    	  System.err.println("Error: " + e.getMessage());
	      }
	      
	      String inputNumber = JOptionPane.showInputDialog("Enter the input number (0 - 999) : ");
	      
	      retrieve ("/image.orig/"+inputNumber+".jpg");
	      
//	      for (int i=0;i<10;i++){
//	    	  System.out.println(distances[i][0]+"\t"+distances[i][1]);
//	      }
	      String path;
	      Retrieval test;
	      

	      for (int i=0;i<10;i++){
	    	  System.out.println(distances[i][0]);		      
	              	  
	      }
	      
	}
	
	
	
	
	BufferedImage image;
    Dimension size = new Dimension();

    public Retrieval(BufferedImage image) {
        this.image = image;
        size.setSize(image.getWidth(), image.getHeight());
    }
}
