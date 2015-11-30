package net.cliffanderson.classifytest.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andersonc12 on 11/27/2015.
 */
public class DataSet
{
    private List<InputVector> vectors;
    private int parameters;

    /*
       parameters: The number of inputs
     */
    public DataSet(File dataFile, int parameters) {
        vectors = new ArrayList<InputVector>();
        this.parameters = parameters;

        try {
            BufferedReader file = new BufferedReader(new FileReader(dataFile));
            while (file.ready()) {
                String input = file.readLine();
                String[] values = input.split(",");

                int[] pixels = new int[parameters];

                for (int i = 0; i < parameters; i++) {
                    pixels[i] = Integer.parseInt(values[i]);
                }

                vectors.add(new InputVector(Integer.parseInt(values[values.length - 1]), pixels));
            }

            file.close();
        }
        catch (IOException e)
        {
            System.err.println("There was an error reading in the data file: " + dataFile.getAbsolutePath());
            e.printStackTrace();
            return;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.err.println(parameters + " parameters were specified but not found for data file: " + dataFile.getAbsolutePath());
            e.printStackTrace();
            return;
        }

        //verify the dataset is valid
        if(vectors.size() == 0)
        {
            System.err.println("0 input vectors were found in the data set");
            return;
        }

        for(int i = 0; i < vectors.size(); i++)
        {
            if(vectors.get(i).getData().length == 0)
            {
                System.err.println("Line " + i + " in data file " + dataFile.getAbsolutePath() + " had no parameters!");
                return;
            }
        }

    }

    /*
        The number of input vectors
     */
    public int getSize()
    {
        return this.vectors.size();
    }

    /*
        The number of inputs in each vector
     */
    public int getParameters()
    {
        return this.parameters;
    }

    /*
        A set of inputs
     */
    public InputVector getInputVector(int i)
    {
        return this.vectors.get(i);
    }
}