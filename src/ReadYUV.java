/*
 * ReadYUV.java
 * By Abdul Arfan
 * 20101204005
 * can read in color
 */

import java.io.*;
import javax.swing.*;
import java.awt.image.*;

import javax.imageio.*;
import java.util.*;

public class ReadYUV
{
	DataInputStream dis;
	byte[] oneFrame;
	int width;
	int height;
	String type= "4:2:0";
	int oneFrameLength;
	float[][] matriceLuminance ;
	
	public ReadYUV(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.type = "4:2:0"; //this version only support the 4:2:0 yuv
	}
	
	public void startReading(String filename)
	{
		try
		{
			dis = new DataInputStream(new BufferedInputStream(
					new FileInputStream(filename)));
            
			double pengali = 1.5;

			if (type.equals("4:2:0"))
			{
				pengali = 1.5;
			} 

			oneFrameLength = (int) (width * height * (pengali));

			oneFrame = new byte[oneFrameLength];
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
		
	public int getRGBFromStream(int x, int y)
	{
		int arraySize = height * width;
		int Y = unsignedByteToInt(oneFrame[y * width + x]);
		int U = unsignedByteToInt(oneFrame[(y/2) * (width/2) + x/2 + arraySize]);
		int V = unsignedByteToInt(oneFrame[(y/2) * (width/2) + x/2 + arraySize + arraySize/4]);

		//~ int R = (int)(Y + 1.370705 * (V-128));
		//~ int G = (int)(Y - 0.698001 * (V-128) - 0.337633 * (U-128));
		//~ int B = (int)(Y + 1.732446 * (U-128)); 
		
		int R = (int)(Y + 1.4075 * (V - 128));
		int G = (int)(Y - 0.3455 * (U - 128) - (0.7169 * (V - 128)));
		int B =(int) (Y + 1.7790 * (U - 128));


		if(R>255) R = 255;
		if(G>255) G = 255;
		if(B>255) B = 255;
		
		if(R<0) R = 0;
		if(G<0) G = 0;
		if(B<0) B = 0;
		
		int rColor = (0xff << 24) | (R << 16) | (G << 8) | B;

		return rColor;
	}
	
	public float getLuminanceFromStream(int x, int y)
	{
		int arraySize = height * width;
		int Y = unsignedByteToInt(oneFrame[y * width + x]);
		int U = unsignedByteToInt(oneFrame[(y/2) * (width/2) + x/2 + arraySize]);
		int V = unsignedByteToInt(oneFrame[(y/2) * (width/2) + x/2 + arraySize + arraySize/4]);

		//~ int R = (int)(Y + 1.370705 * (V-128));
		//~ int G = (int)(Y - 0.698001 * (V-128) - 0.337633 * (U-128));
		//~ int B = (int)(Y + 1.732446 * (U-128)); 
		
		int R = (int)(Y + 1.4075 * (V - 128));
		int G = (int)(Y - 0.3455 * (U - 128) - (0.7169 * (V - 128)));
		int B =(int) (Y + 1.7790 * (U - 128));


		if(R>255) R = 255;
		if(G>255) G = 255;
		if(B>255) B = 255;
		
		if(R<0) R = 0;
		if(G<0) G = 0;
		if(B<0) B = 0;
		
		float rColor = (float) (0.299 *R + 0.587 *G + 0.114 *B) ;

		return rColor;
	}
	
	public void setLuminance(){
		System.out.println(width + " " + height);
		for(int i = 10; i<width ; i++){
			for(int j = 10 ; j<height ; j ++){
				this.matriceLuminance[i][j] = getLuminanceFromStream(i, j);
			}
		}
	}
	
	public int getGrayScaleFromY(int x, int y)
	{
		int arraySize = height * width;
		int Y = unsignedByteToInt(oneFrame[y * width + x]);
		
		int R = Y;
		
		if(R>255) R = 255;
		
		int G = R;
		int B = R;
		
		int rColor = (0xff << 24) | (R << 16) | (G << 8) | B;

		return rColor;
	}

	public BufferedImage nextImage()
	{
		try
		{
			int n = dis.read(oneFrame);
			
			if (n != oneFrameLength)
			{
				return null;
			}

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for (int j = 0; j < height; j++)
			{
				for (int i = 0; i < width; i++)
				{
					int rColor = getRGBFromStream(i, j);
					image.setRGB(i, j, rColor);
				}
			}

			//ImageIO.write(image, "png", new File("result.png"));
			return image;
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	public BufferedImage nextImageYOnly()
	{
		try
		{
			int n = dis.read(oneFrame);

			if (n != oneFrameLength)
			{
				return null;
			}

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for (int j = 0; j < height; j++)
			{
				for (int i = 0; i < width; i++)
				{
					int rColor = getGrayScaleFromY(i, j);
					image.setRGB(i, j, rColor);
				}
			}

			return image;
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public void endReading()
	{
		try {
			dis.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static int unsignedByteToInt(byte b)
	{
		return (int) b & 0xFF;
	}
    
	public void setMatricePixel(){
		for (int i = 0; i< width ; i++){
			for(int j = 0; j<height ; j++){
				
			}
		}
	}
    
	public static void main(String args[]) throws Exception
	{
		BufferedImage currentImage = new BufferedImage(400, 400, BufferedImage.TYPE_BYTE_GRAY);

		ReadYUV ryuv = new ReadYUV(176, 144); //read the qcif yuv
		System.out.println("yo");
		ryuv.startReading("akiyo_qcif.yuv");
		
		
		WriteYUV wyuv = new WriteYUV(176, 144);
		wyuv.startWriting("test_qcif.yuv");
		
		while((currentImage=ryuv.nextImageYOnly())!=null)
		{
			wyuv.writeImageYOnly(currentImage);
		}
		
		wyuv.endWriting();
		ryuv.endReading();
	}
}
