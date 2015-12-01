package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

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
        return result;
    }
}
