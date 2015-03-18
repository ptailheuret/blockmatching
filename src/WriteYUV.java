import java.io.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.*;

public class WriteYUV
{
	DataOutputStream dos;
	
	int width;
	int height;
	String type= "4:2:0";
	int oneFrameLength;
	byte oneFrame[];
	
	public WriteYUV(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.type = "4:2:0"; //this version only support the 4:2:0 yuv
	}
	
	public void startWriting(String filename)
	{
		try
		{
			dos = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(filename)));
			
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
	public void writeImage(BufferedImage bi)
	{
		int w = bi.getWidth();
		int h = bi.getHeight();

		LinkedList<Byte> uBuffer = new LinkedList<Byte>();
		LinkedList<Byte> vBuffer = new LinkedList<Byte>();
		try
		{
			boolean s = false;

			for (int j = 0; j < h; j++)
			{
				for (int i = 0; i < w; i++)
				{
					int color = bi.getRGB(i, j);

					int alpha = color >> 24 & 0xff;
					int R = color >> 16 & 0xff;
					int G = color >> 8 & 0xff;
					int B = color & 0xff;

					//~ int y = (int) ((0.257 * red) + (0.504 * green) + (0.098 * blue) + 16);
					//~ int v = (int) ((0.439 * red) - (0.368 * green) - (0.071 * blue) + 128);
					//~ int u = (int) (-(0.148 * red) - (0.291 * green) + (0.439 * blue) + 128);
					
					int Y = (int)(R *  .299000 + G *  .587000 + B *  0.114000);
					int U = (int)(R * -.168736 + G * -.331264 + B *  0.500000 + 128);
					int V = (int)(R *  .500000 + G * -.418688 + B * -0.081312 + 128);

					
					int arraySize = height * width;
					int yLoc = j * width + i;
					int uLoc = (j/2) * (width/2) + i/2 + arraySize;
					int vLoc = (j/2) * (width/2) + i/2 + arraySize + arraySize/4;
					
					oneFrame[yLoc] = (byte)Y;
					oneFrame[uLoc] = (byte)U;
					oneFrame[vLoc] = (byte)V;
					
					s = !s;
				}
			}
			
			for(int i=0;i<oneFrameLength;i++)
			{
				dos.write(oneFrame[i]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void writeImageYOnly(BufferedImage bi)
	{
		int w = bi.getWidth();
		int h = bi.getHeight();

		LinkedList<Byte> uBuffer = new LinkedList<Byte>();
		LinkedList<Byte> vBuffer = new LinkedList<Byte>();
		try
		{
			boolean s = false;

			for (int j = 0; j < h; j++)
			{
				for (int i = 0; i < w; i++)
				{
					int color = bi.getRGB(i, j);

					int alpha = color >> 24 & 0xff;
					int R = color >> 16 & 0xff;
					int G = color >> 8 & 0xff;
					int B = color & 0xff;

					//~ int y = (int) ((0.257 * red) + (0.504 * green) + (0.098 * blue) + 16);
					//~ int v = (int) ((0.439 * red) - (0.368 * green) - (0.071 * blue) + 128);
					//~ int u = (int) (-(0.148 * red) - (0.291 * green) + (0.439 * blue) + 128);
					
					int Y = (int)(R *  .299000 + G *  .587000 + B *  0.114000);
					int U = (int)(R * -.168736 + G * -.331264 + B *  0.500000 + 128);
					int V = (int)(R *  .500000 + G * -.418688 + B * -0.081312 + 128);

					
					int arraySize = height * width;
					int yLoc = j * width + i;
					int uLoc = (j/2) * (width/2) + i/2 + arraySize;
					int vLoc = (j/2) * (width/2) + i/2 + arraySize + arraySize/4;
					
					oneFrame[yLoc] = (byte)Y;
					oneFrame[uLoc] = (byte)128;
					oneFrame[vLoc] = (byte)128;
					
					s = !s;
				}
			}
			
			for(int i=0;i<oneFrameLength;i++)
			{
				dos.write(oneFrame[i]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void endWriting()
	{
		try
		{
			dos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}