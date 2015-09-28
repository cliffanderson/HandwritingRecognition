package net.cliffanderson.handwriting;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * Created by andersonc12 on 9/26/2015.
 */
public class HandwritingRecognition
{
    public static void main(String[] args)
    {
        HandwritingRecognition hr = new HandwritingRecognition();

        hr.decodeImages(new File("C:\\Users\\andersonc12\\Desktop\\HandwritingRecognition" +
                "\\train-images.idx3-ubyte"),
                new File("C:\\Users\\andersonc12\\HandwritingImages\\"));
    }

    public void decodeImages(File encodedFile, File outputDir)
    {
        //quick check
        if(!outputDir.exists())
        {
            outputDir.mkdirs();
        }

        //setup input stream
        InputStream fileIn;
        DataInputStream in;

        try
        {
            fileIn = new FileInputStream(encodedFile);
            in = new DataInputStream(fileIn);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("The encoded image file does not exist!");
            e.printStackTrace();
            return;
        }



        //read in all the bytes
        try
        {
            //read in magic number
            int magic = in.readInt();
            System.out.println("Magic number for encoded image file: " + magic);

            //read dataset info
            int numberOfImages = in.readInt();
            int imageWidth = in.readInt();
            int imageHeight = in.readInt();

            //create all the images
            for(int img = 0; img < numberOfImages; img++)
            {
                //create an image
                BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
                for(int y = 0; y < imageHeight; y++)
                {
                    for(int x = 0; x < imageWidth; x++)
                    {
                        byte color = in.readByte(); //color of this pixel

                        if(color < 0)
                        {
                            color += 128; //we want unsigned bytes
                        }

                        image.setRGB(x, y, new Color(color, color, color).getRGB()); //all rgb the same value for grayscale
                    }
                }

                ImageIO.write(image, "PNG", new File(outputDir, String.valueOf(System.currentTimeMillis()) + ".png"));

                if((img + 1) % 1000 == 0)
                {
                    System.out.println((img + 1) + " images complete");
                }
            }

        }
        catch (IOException e)
        {
            System.err.println("Error reading the encoded image file!");
            e.printStackTrace();
            return;
        }
    }
}
