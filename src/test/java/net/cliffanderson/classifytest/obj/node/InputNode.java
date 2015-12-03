package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.InputVector;
import net.cliffanderson.classifytest.obj.NeuralNetwork;

/**
 * Created by Cliff on 11/30/2015.
 */
public class InputNode extends Node
{
    private int id;
    public InputNode(NeuralNetwork network, int inputs, int outputs, int id)
    {
        super(network, inputs, outputs);
        this.id = id;
    }


    @Override
    public double getOutput()
    {
        return this.network.getInputVector().getData()[this.id];
    }

    @Override
    public void addToErrorSum(double errorPortion)
    {
        //do nothing for input nodes
    }

    @Override
    public void computeError()
    {
        //no error for input nodes
    }

    @Override
    public void adjustWeights()
    {
        //no weights to adjust for input nodes
    }




}
