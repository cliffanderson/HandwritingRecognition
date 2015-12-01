package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

import java.util.List;

/**
 * Created by Cliff on 11/30/2015.
 */
public class HiddenNode extends Node
{
    public HiddenNode(NeuralNetwork network)
    {
        super(network);
    }

    @Override
    public void calculateError(List<Double> weights)
    {
        double sum = 0.0;
        for(int i = 0; i < weights.size(); i++)
        {
            sum += this.outputs.get(i).getError() * weights.get(i);
        }

        this.error = this.output * (1 - this.output) * sum;

        //have inputs calculate errors
        for(Node n : this.inputs)
        {
            n.calculateError(this.weights);
        }
    }



}
