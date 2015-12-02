package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.InputVector;
import net.cliffanderson.classifytest.obj.NeuralNetwork;

/**
 * Created by Cliff on 11/30/2015.
 */
public class InputNode extends Node
{
    public InputNode(NeuralNetwork network, int inputs, int outputs)
    {
        super(network, inputs, outputs);
    }

    @Override
    public double getOutput()
    {
        double result = this.network.getInputValue(this);
        this.output = result;
        return this.output;
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
