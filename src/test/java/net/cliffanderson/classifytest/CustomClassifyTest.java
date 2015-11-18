package net.cliffanderson.classifytest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andersonc12 on 11/6/2015.
 */
public class CustomClassifyTest
{
    static void neuralNetwork(File dataFile)
    {
        Map<String, int[]> pixels = loadData(dataFile, 49);

        double[] hiddenNodes = new double[49];
        double[] resultNodes = new double[10];

        //populate the hidden nodes with weights
        populateWeights(hiddenNodes);

        //for every input vector of pixels values, train the hidden layer
        for(int[] inputVector : pixels.values())
        {
            for(int i = 0; i < inputVector.length || i < hiddenNodes.length; i++)
            {
                hiddenNodes[i] = sigmoid();
            }
        }


    }

    static double sigmoid(double v)
    {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    static void populateWeights(double[] hiddenNodes)
    {
        for(int i = 0; i < hiddenNodes.length; i++)
        {
            hiddenNodes[i] = (Math.random() * 0.005);
        }
    }

    /*
        Arguments: Data file, pixels per image
     */
    static Map<String, int[]> loadData(File dataFile, int pixelCount)
    {
        Map<String, int[]> result = new HashMap<String, int[]>();

        try
        {
            BufferedReader file = new BufferedReader(new FileReader(dataFile));
            while(file.ready())
            {
                String input = file.readLine();
                String[] values = input.split(",");

                int[] pixels = new int[pixelCount];

                for(int i = 0; i < pixelCount; i++)
                {
                    pixels[i] = Integer.parseInt(values[i]);
                }

                result.put(values[values.length - 1], pixels);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
