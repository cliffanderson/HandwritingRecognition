package net.cliffanderson.classifytest.obj.node;

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

    //input nodes do not have weights
    @Override
    public void updateWeights()
    {
        return;
    }

    //do not have errors
    @Override
    public void calculateError(double[] weights)
    {
        return;
    }
}
