package net.cliffanderson.classifytest.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andersonc12 on 11/27/2015.
 */
public class NeuralNetwork
{
    private List<Node> resultNodes;
    private List<List<Node>> hiddenLayerList;
    private List<Node> inputNodes;

    //number of inputs in the training set
    private int trainingParameters;

    //the current input vector
    private int vectorCount;

    //training set
    private DataSet trainingSet;

    /*
    trainingSet: set for training the network
    testingSet: set for testing the data
    learningRate: the learning rate for the network, between 0 and 1
    hiddenlayers: an array representing the hidden layers, where each value is the number of nodes in that layer
    classes: the number of classification classes
     */
    public NeuralNetwork(DataSet trainingSet, DataSet testingSet, double learningRate, int[] hiddenlayers, int classes) {
        //check parameters
        if(learningRate <= 0.0 || learningRate >= 1.0)
        {
            System.err.println("Learning rate must be between 0 and 1");
            return;
        }

        //check hidden layers
        for(int i : hiddenlayers)
        {
            if(i <= 0)
            {
                System.err.println("Invalid number of nodes in hidden layer: " + i);
                return;
            }
        }

        //check classes
        if(classes <= 0)
        {
            System.err.println("Invalid number of classes: " + classes);
            return;
        }

        this.trainingSet = trainingSet;
        this.trainingParameters = this.trainingSet.getParameters();


        createNetwork(trainingSet.getParameters(), hiddenlayers, classes);
    }

    private void createNetwork(int parameters, int[] hiddenlayers, int classes)
    {
        resultNodes = new ArrayList<Node>();
        hiddenLayerList = new ArrayList<List<Node>>();
        inputNodes = new ArrayList<Node>();

        //make result nodes
        for(int i = 0; i < classes; i++)
        {
            resultNodes.add(new Node(this));
        }

        //make input nodes
        for(int i = 0; i < parameters; i++)
        {
            inputNodes.add(new Node(this));
        }


        //if there are no hidden nodes, connect result to input and be done
        if(hiddenlayers.length == 0)
        {
            for(int result = 0; result < classes; result++)
            {
                for(int input = 0; input < parameters; input++)
                {
                    resultNodes.get(result).addInput(inputNodes.get(input));
                }
            }

            return;
        }



        //generate hidden layers
        for(int i = 0; i < hiddenlayers.length; i++)
        {
            for(int nodes = 0; nodes < hiddenlayers[i]; nodes++)
            {
                if(hiddenLayerList.get(i) == null)
                {
                    hiddenLayerList.add(new ArrayList<Node>());
                }

                hiddenLayerList.get(i).add(new Node(this));
            }
        }



        //connect result nodes to first layer of hidden nodes
        for(int result = 0; result < resultNodes.size(); result++)
        {
            for(int hidden = 0; hidden < hiddenlayers[0]; hidden++)
            {
                resultNodes.get(result).addInput(hiddenLayerList.get(0).get(hidden));
            }
        }

        //connect the hidden layers together
        for(int layer = 0; layer < hiddenlayers.length - 1; layer++)
        {
            //connect each layer to the next one
            for(int hiddenParent = 0; hiddenParent < hiddenlayers[layer]; hiddenParent++)
            {
                for(int hiddenChild = 0; hiddenChild < hiddenlayers[layer + 1]; hiddenChild++)
                {
                    hiddenLayerList.get(layer).get(hiddenParent).addInput(hiddenLayerList.get(layer + 1).get(hiddenChild));
                }
            }
        }

        //connect last hidden layer to input nodes
        for(int hidden = 0; hidden < hiddenlayers[hiddenlayers.length - 1]; hidden++)
        {
            for(int input = 0; input < parameters; input++)
            {
                hiddenLayerList.get(hiddenLayerList.size() - 1).get(hidden).addInput(inputNodes.get(input));
            }
        }
    }

    //run the entire training set through the network, using back propagation after every input vector
    private void epoch()
    {
       for(int input = 0; input < this.trainingSet.getSize(); input++)
       {

       }
    }

    //n - the node calling this function
    public double getInputValue(Node n)
    {
        int index = this.inputNodes.indexOf(n);

        if(index == -1)
        {
            System.err.println("Only input nodes can call getInputValue()");
            return 0;
        }

        if(index == trainingParameters - 1)
        {
            double result = this.trainingSet.getInputVector(this.vectorCount).getData()[index];

            //roll over to next vector
            this.vectorCount++;

            if(this.vectorCount > this.trainingSet.getSize())
            {
                this.vectorCount = 0;
            }

            return result;
        }

        return this.trainingSet.getInputVector(this.vectorCount).getData()[index];
    }


}
