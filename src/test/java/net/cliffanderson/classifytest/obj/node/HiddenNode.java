package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

/**
 * Created by Cliff on 11/30/2015.
 */
public class HiddenNode extends Node
{
    public HiddenNode(NeuralNetwork network, int inputs, int outputs)
    {
        super(network, inputs, outputs);
    }

    @Override
    public void calculateError(double[] weights)
    {
        if (this.errorCalculated) return;

        double sum = 0.0;
        for (int i = 0; i < weights.length; i++)
        {
            sum += this.outputs[i].getError() * weights[i];
        }

        this.error = this.output * (1 - this.output) * sum;
        this.errorCalculated = true;
        //have inputs calculate errors
        for (Node n : this.inputs)
        {
            n.calculateError(this.weights);
        }
    }
}
