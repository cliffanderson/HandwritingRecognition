package net.cliffanderson.handwriting;

import org.apache.commons.io.IOUtils;

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
                "\\train-images.idx3-ubyte"), null);
    }

    public void decodeImages(File encodedFile, File outputDir)
    {
        //setup input stream
        InputStream fileIn;

        try
        {
            fileIn = new FileInputStream(encodedFile);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("The encoded image file does not exist!");
            e.printStackTrace();
            return;
        }



        //read in all the bytes
        byte[] data;
        try
        {
            data = IOUtils.toByteArray(fileIn);
        }
        catch (IOException e)
        {
            System.err.println("Error reading the encoded image file!");
            e.printStackTrace();
            return;
        }

        //we have the data, build images
        System.out.println(Arrays.toString(Arrays.copyOf(data, 10)));
    }
}
