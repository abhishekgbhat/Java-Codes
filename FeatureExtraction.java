import ij.process.ColorProcessor;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.imageio.ImageIO;

public class FeatureExtraction {
	
	public static float[][] R;
	public static float[][] G;
	public static float[][] B;
	public static float[][] H;
	public static float[][] S;
	public static float[][] V;
	
	public static int[][] bin;
	static int height;
	static int width;
	public static double[][] allFeatures=new double[1000][66];
	public static double[] features=new double[66];
	public static Calendar cal=Calendar.getInstance();
	public static PrintStream outFile;
	
	
	public static double[] extractSingleImage(String name){
		double[] out=new double[66];
		URL url = FeatureExtraction.class.getResource(name);
		System.out.println("url = " + url);
		BufferedImage image = null;
		try {
			image = ImageIO.read(url);
		} catch(IOException e) {
			System.out.println("read error: " + e.getMessage());
		}
		
		ColorProcessor cp=new ColorProcessor(image);
		
		/////////////// Color Extraction /////////////////
		
		ColorExtraction ce=new ColorExtraction(cp);
		ce.extract();
		double[] colors=ce.getColorFeature();


		////////////// Edge Detection ///////////////        

		CannyEdgeDetector ced=new CannyEdgeDetector();
		ced.setSourceImage(image);
		ced.process();
		BufferedImage edge=ced.getEdgesImage();
		rgb2bin(edge);
		

		////////////// Texture Extraction ///////////////        

		TextureExtraction te=new TextureExtraction(bin, height, width);
		te.extract();
		double[] textures=te.getCoocFeatures();
		

		//		////////// Join //////////////
		for (int i=0;i<18;i++){
			out[i]=colors[i];
		}

		for (int i=0;i<48;i++){
        	out[i+18]=textures[i];        	
        }
		
		return out;
	}
	
	
    public static void extractAll() {
    	try{
    		outFile=new PrintStream("Features.txt");
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	long start=cal.getTimeInMillis();
	    for (int ind=0;ind<1000;ind++){
	    
	    	System.out.println(ind);
	    	URL url = FeatureExtraction.class.getResource(//"Icons/icon.png");
	                                         "/image.orig/"+ ind +".jpg");
	        // where are we looking for this image
	        BufferedImage image = null;
	        try {
	            image = ImageIO.read(url);
	        } catch(IOException e) {
	            System.out.println("read error: " + e.getMessage());
	        }
	
	        ColorProcessor cp=new ColorProcessor(image);
	        
	         /////////////// Color Extraction /////////////////
	        
	        ColorExtraction ce=new ColorExtraction(cp);
	        ce.extract();
	        double[] colors=ce.getColorFeature();
	        
	        
	        ////////////// Edge Detection ///////////////        
	        
	        CannyEdgeDetector ced=new CannyEdgeDetector();
	        ced.setSourceImage(image);
	        ced.process();
	        BufferedImage edge=ced.getEdgesImage();
	        rgb2bin(edge);
	        
	        
	        ////////////// Texture Extraction ///////////////        
	        
	        TextureExtraction te=new TextureExtraction(bin, height, width);
	        te.extract();
	        double[] textures=te.getCoocFeatures();
	        
	        
	        //////////// Join //////////////
	        
	        
	        
	        //System.out.println("time: "+(finish-start));
	        //System.out.println("*************");
	        
	        for (int i=0;i<18;i++){
	        	features[i]=colors[i];
	        	outFile.print(colors[i]+ "\t");
	        }
	        
	        //System.out.println("*************");
	        
	        for (int i=0;i<48;i++){
	        	features[i+18]=textures[i];
	        	outFile.print(textures[i]+ "\t");
	        	//System.out.println(textures[i]);
	        }
	        
	        outFile.println();
	        allFeatures[ind]=features;
	        
	        
	    }
	    
	    long finish=cal.getTimeInMillis();
	    System.out.println("time: "+(finish-start));
//        
//        
//        for (int i=0;i<cp.getHeight();i++){
//        	for (int j=0;j<cp.getWidth();j++){
//        		System.out.print(bin[i][j]);
//        	}
//        	System.out.println();
//        }
//        
                
        
        
        
//        ColorProcessor cp=new ColorProcessor(image);
//        System.out.println(cp.getHeight());
//        System.out.println(cp.getWidth());
//        System.out.println(cp.getPixelCount());
//        System.out.println(cp.getPixel(100, 200));
//        int[] H=cp.getHistogram();
//        
//        cp.findEdges();
//        
//        byte[] R=new byte[165300];
//        byte[] G=new byte[165300];;
//        byte[] B=new byte[165300];;
//        
//        cp.getRGB(R, G, B);
//        
//        System.out.println(H.length);
//        
//        for (int i=0;i<H.length;i++)
//        	System.out.println(H[i]);
       

        
//        
//        
//        
//        Raster r=image.getData();
//        int[] temp=new int[7500];
//        int[] d=r.getPixels(0, 0, 50,50, temp);
//        for (int i=0;i<d.length;i++)
//        	System.out.println(d[i]);
//        System.out.println(d.length);
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setIconImage(image);
//        f.setVisible(true);
    }
    
    public static void RGB_HSV_Decompose(ColorProcessor cp){
    	
    	R=new float[cp.getHeight()][cp.getWidth()];
    	G=new float[cp.getHeight()][cp.getWidth()];
    	B=new float[cp.getHeight()][cp.getWidth()];
    	H=new float[cp.getHeight()][cp.getWidth()];
    	S=new float[cp.getHeight()][cp.getWidth()];
    	V=new float[cp.getHeight()][cp.getWidth()];
    	int rgb[] =new int[3];
    	float hsv[]=new float[3];
    	for (int i=0;i<cp.getHeight();i++){
    		for (int j=0;j<cp.getWidth();j++){
    			rgb=cp.getPixel(i, j, rgb);
    			R[i][j]=rgb[0];
    			G[i][j]=rgb[1];
    			B[i][j]=rgb[2];
    			hsv=java.awt.Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv);
    			H[i][j]=hsv[0];
    			S[i][j]=hsv[1];
    			V[i][j]=hsv[2];
    		}
    	}
    }
    
    public static double[] meanStdSkew ( float[][] data, int h, int w )
    {
	    double mean = 0;
	    double[] out=new double[3];
	    
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		mean += data[i][j];
        	}
	    }
	    mean /= (h*w);
	    out[0]=mean;
	    double sum = 0;
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		final double v = data[i][j] - mean;
        		sum += v * v;
        	}
	    }
	    out[1]=Math.sqrt( sum / ( h*w - 1 ) );
	    
	    sum = 0;
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		final double v = (data[i][j] - mean)/out[1];
        		sum += v * v * v;        		
        	}
	    }
	    
	    out[2]=Math.pow(sum/(h*w-1),1./3);
	    return out;
    }


    
    

    
    public static void rgb2bin(BufferedImage in){
    	int h=in.getHeight();
    	int w=in.getWidth();
    	height=h;
    	width=w;
    	int[][] out=new int[h][w];
    	long rgb;
    	int r,g,b;
    	ColorProcessor cp=new ColorProcessor(in);
    	for (int i=0;i<h;i++){
    		for (int j=0;j<w;j++){
    			rgb=cp.getPixel(i, j);
    	        r   = (int)(rgb % 0x1000000 / 0x10000);  
    	        g = (int)(rgb % 0x10000 / 0x100);  
    	        b = (int)(rgb % 0x100);  
    	        if (r==0 && g==0 && b==0){
    	        	out[i][j]=0;
    	        }
    	        else{
    	        	out[i][j]=1;
    	        }
    		}
    	}
    	
    	bin= out;
    	
    }
}