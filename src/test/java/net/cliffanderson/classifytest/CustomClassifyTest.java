package net.cliffanderson.classifytest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andersonc12 on 11/6/2015.
 */
public class CustomClassifyTest
{
    public static void neuralNetwork(File dataFile, int epochs)
    {
        Map<String, int[]> pixels = loadData(dataFile, 49);

        double[] hiddenNodes = new double[9];
        double[] resultNodes = new double[10];

        //populate the hidden nodes with weights
        populateWeights(hiddenNodes);
        System.out.println("Starting: " + Arrays.toString(hiddenNodes));

        //times to train it
        for(int training = 0; training < epochs; training++) {
            //for every input vector of pixels values, train the hidden layer
            for (int hidden = 0; hidden < hiddenNodes.length; hidden++) {
                double sum = 0.0;

                for (int[] input : pixels.values()) {
                    for (int pixel = 0; pixel < input.length; pixel++) {
                        sum += pixel * hiddenNodes[hidden];
                    }


                }
                System.out.println("Hidden " + hidden + "   sum " + sum);
                hiddenNodes[hidden] = sigmoid(sum /*sum of every value of input * hiddenNodes[hidden]*/);

            }
            System.out.println("Ending: " + Arrays.toString(hiddenNodes));
        }
        System.out.println("Final ending: " + Arrays.toString(hiddenNodes));
    }

    static double sigmoid(double v)
    {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    static void populateWeights(double[] hiddenNodes)
    {
        for(int i = 0; i < hiddenNodes.length; i++)
        {
            hiddenNodes[i] = (Math.random() * 0.000005);
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
