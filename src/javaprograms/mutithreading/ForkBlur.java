package javaprograms.mutithreading;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.imageio.ImageIO;

/**
 * In this class I am studying the FORK/JOIN Framework introduced in Java SE 8.
 * Fork/Join framework is distinct from Executor Service because it follows a work steal algorithm.
 * 
 *  if my work is small 
 *  	do directly;
 * 	else
 * 		Split my work into two pieces. 
 * 		Invoke the two pieces and 	wait for results. 
 * 
 * @author neenadamodaran
 *
 *	What we are trying to do in this class is to blurr the clarity of an image. 
 *	Original source image is represented by array of integers where each integer represent the color value of  a single pixel
 *  
 */
public class ForkBlur extends RecursiveAction{
	
	private int []origImagePixelColorValues;
	private int []blurredImagePixelColorValues;
	
	//starting index of the original image pixel array that would be processed by this thread
	private int threadStartIndex;
	
	// the number of Element from the starting index that will be processed by this thread.
	private int threadWorkLength; 
	
	//processing window size should be odd
	// this window is used to calculate the average and this average 
	//is set as the current pixel blurred color value in the blurred IMage array. 
	private int mBlurWidth = 15;
	
	

	public ForkBlur(int[] origImagePixelColorValues, int threadStartIndex, int threadWorkLength, int[] blurredImagePixelColorValues) {
		super();
		this.origImagePixelColorValues = origImagePixelColorValues;
		this.blurredImagePixelColorValues = blurredImagePixelColorValues;
		this.threadStartIndex = threadStartIndex;
		this.threadWorkLength = threadWorkLength;
		
	} 
	
	protected void computeDirectly(){
		//calculate the left most and right most pixel of the pixel at the center of the window.
		int sidePixels = (mBlurWidth - 1)/ 2; 
		
		//the start index and end index that would be processed by the current thread
		for(int index = threadStartIndex; index < threadStartIndex + threadWorkLength ; index ++){
			
			float redTone = 0, greenTone = 0, blueTone = 0; 
			
			
			// to calculate the average using color value of pixels on both sides of the given pixel
			for(int mi = -sidePixels ; mi <= sidePixels; mi++){
				//Calculate the valid index
				int minIndex = Math.min(Math.max(mi + index,0 ), origImagePixelColorValues.length - 1); 
				
				int pixel = origImagePixelColorValues[minIndex]; 
				
				//getting the average values of RedTone for all the pixel in the window
				redTone += (float)((pixel & 0x00ff0000) >> 16)
	                      / mBlurWidth;
				//getting the average values of greenTone for all the pixel in the window
				greenTone += (float)((pixel & 0x0000ff00) >>  8)
	                      / mBlurWidth;
				//getting the average values of blueTone for all the pixel in the window
				blueTone += (float)((pixel & 0x000000ff) >>  0)
	                      / mBlurWidth;
				
			}
			
			 // Reassemble destination pixel.
            int dpixel = (0xff000000     ) |
                   (((int)redTone) << 16) |
                   (((int)greenTone) <<  8) |
                   (((int)blueTone) <<  0);
            blurredImagePixelColorValues[index] = dpixel;
			
		}
		
	}
	
	protected static int threadWorkThreshold = 10000;
	 
    @Override
    protected void compute() {
        if (threadWorkLength < threadWorkThreshold) {
            computeDirectly();
            return;
        }
 
        int split = threadWorkThreshold / 2;
 
        invokeAll(new ForkBlur(origImagePixelColorValues, threadStartIndex, split, blurredImagePixelColorValues),
                new ForkBlur(origImagePixelColorValues, threadStartIndex + split, threadWorkLength - split, 
                		blurredImagePixelColorValues));
    }
	
    // Plumbing follows.
    public static void main(String[] args) throws Exception {
        String srcName = "img/red-tulips.jpg";
       
        File srcFile = new File(srcName);
        
        BufferedImage image = ImageIO.read(srcFile);
         
        System.out.println("Source image: " + srcName);
         
        BufferedImage blurredImage = blur(image);
         
        String dstName = "img/blurred-tulips.jpg";
        File dstFile = new File(dstName);
        ImageIO.write(blurredImage, "jpg", dstFile);
         
        System.out.println("Output image: " + dstName);
        
    }

	private static BufferedImage blur(BufferedImage origImage) {
		int width =  origImage.getWidth(); 
		int height = origImage.getHeight(); 
		int [] src = origImage.getRGB(0, 0, width, height, null, 0, width); 
		int [] dst = new int[src.length]; 
		
		System.out.println("Array size is " + src.length);
        System.out.println("Threshold is " + ForkBlur.threadWorkThreshold);
        
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(Integer.toString(processors) + " processor"
                + (processors != 1 ? "s are " : " is ")
                + "available");
        
        ForkBlur fb = new ForkBlur(src, 0, src.length, dst);
        
        ForkJoinPool pool = new ForkJoinPool();
        
        long startTime = System.currentTimeMillis();
        pool.invoke(fb);
        long endTime = System.currentTimeMillis();
        
        System.out.println("Image blur took " + (endTime - startTime) + 
                " milliseconds.");
 
        BufferedImage dstImage =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        dstImage.setRGB(0, 0, width, height, dst, 0, width);
 
        return dstImage;
	}
}
