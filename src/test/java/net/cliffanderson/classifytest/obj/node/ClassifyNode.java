package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

/**
 * Created by Cliff on 12/1/2015.
 */
public class ClassifyNode extends Node
{
    public ClassifyNode(NeuralNetwork network, int inputs, int outputs)
    {
        super(network, inputs, outputs);
    }

    @Override
    public void calculateError(double[] weights)
    {

    }
}
