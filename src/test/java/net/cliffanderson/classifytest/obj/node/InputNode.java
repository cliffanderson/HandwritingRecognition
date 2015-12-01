package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

import java.util.List;

/**
 * Created by Cliff on 11/30/2015.
 */
public class InputNode extends Node
{
    public InputNode(NeuralNetwork network)
    {
        super(network);
    }

    @Override
    public double calculateOutput() {
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
    public void calculateError(List<Double> weights)
    {
        return;
    }
}
