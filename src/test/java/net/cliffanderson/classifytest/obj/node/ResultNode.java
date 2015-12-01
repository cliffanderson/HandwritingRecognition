package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

import java.util.List;

/**
 * Created by Cliff on 11/30/2015.
 */
public class ResultNode extends Node
{
    public ResultNode(NeuralNetwork network)
    {
        super(network);
    }

    @Override
    public void calculateError(List<Double> weights)
    {
        double actual;
        if(this.network.trainingSet.getInputVector(this.network.trainingVectorCount).getTarget()
                == this.network.resultNodes.indexOf(this))
        {
            actual = 1;
        }
        else
        {
            actual = 0;
        }

        this.error = this.output * (1 - output) * (actual - this.output);

        //have each input calculate the errors
        for(Node n : this.inputs)
        {
            n.calculateError(this.weights);
        }
    }
}
