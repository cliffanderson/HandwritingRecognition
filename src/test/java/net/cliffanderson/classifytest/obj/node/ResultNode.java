package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

/**
 * Created by Cliff on 11/30/2015.
 */
public class ResultNode extends Node
{
    public ResultNode(NeuralNetwork network, int inputs, int outputs)
    {
        super(network, inputs, outputs);
    }

    @Override
    public void calculateError(double[] weights)
    {
        if (errorCalculated) return;

        double actual;
        // if(this.network.trainingSet.getInputVector(this.network.trainingVectorCount).getTarget()
        // == this.network.resultNodes.indexOf(this))
        // {
        actual = 1;
        // }
        // else
        // {
        //   actual = 0;
        // }

        this.error = this.output * (1 - output) * (actual - this.output);
        this.errorCalculated = true;
        //have each input calculate the errors
        for (Node n : this.inputs)
        {
            n.calculateError(this.weights);
        }
    }
}
