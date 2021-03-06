package net.cliffanderson.classifytest.obj;

import net.cliffanderson.classifytest.obj.node.*;

/**
 * Created by andersonc12 on 11/27/2015.
 */
public class NeuralNetwork
{
    //training set
    private DataSet trainingSet;
    private ResultNode[] resultNodes;
    private HiddenNode[][] hiddenNodeArray;
    private InputNode[] inputNodes;
    private ClassifyNode classifyNode;

    //number of inputs in the training set
    private int parameters;
    //testing set
    private DataSet testingSet;
    //learning rate
    private double learningRate;
    //current input vector we are working with
    private InputVector inputVector;
    //current target
    private int target;

    /*
        trainingSet: set for training the network
        testingSet: set for testing the data
        learningRate: the learning rate for the network, between 0 and 1
        hiddenlayers: an array representing the hidden layers, where each value is the number of nodes in that layer
        classes: the number of classification classes
     */
    public NeuralNetwork(DataSet trainingSet, DataSet testingSet, double learningRate, int[] hiddenlayers, int classes)
    {
        //check number of parameters
        if (trainingSet.getParameters() != testingSet.getParameters())
        {
            System.err.println("Number of training set parameters and testing set parameters must be the same!");
            return;
        }
        //check learning rate
        if (learningRate <= 0.0 || learningRate >= 1.0)
        {
            System.err.println("Learning rate must be between 0 and 1");
            return;
        }

        //check hidden layers
        if (hiddenlayers.length == 0)
        {
            System.err.println("Must have at least one hidden layer");
            return;
        }

        for (int i : hiddenlayers)
        {
            if (i <= 0)
            {
                System.err.println("Invalid number of nodes in hidden layer: " + i);
                return;
            }
        }

        //check classes
        if (classes <= 0)
        {
            System.err.println("Invalid number of classes: " + classes);
            return;
        }

        this.trainingSet = trainingSet;
        this.testingSet = testingSet;
        this.parameters = trainingSet.getParameters();

        this.learningRate = learningRate;


        createNetwork(hiddenlayers, classes);
    }

    public int getParameters()
    {
        return this.parameters;
    }

    public InputVector getInputVector()
    {
        return this.inputVector;
    }

    public int getTarget()
    {
        return this.target;
    }

    private void createNetwork(int[] hiddenlayers, int classes)
    {
        resultNodes = new ResultNode[classes];
        hiddenNodeArray = new HiddenNode[hiddenlayers.length][];
        inputNodes = new InputNode[this.parameters];

        //make classify node
        classifyNode = new ClassifyNode(this, resultNodes.length, 0);

        //make result nodes
        for (int i = 0; i < classes; i++)
        {
            resultNodes[i] = new ResultNode(this, hiddenlayers[hiddenlayers.length - 1], 1);
        }

        //generate hidden layers
        for (int layer = 0; layer < hiddenlayers.length; layer++)
        {
            hiddenNodeArray[layer] = new HiddenNode[hiddenlayers[layer]];

            for (int hidden = 0; hidden < hiddenlayers[layer]; hidden++)
            {

                int inputs, outputs;
                if (layer == 0) outputs = resultNodes.length;
                else outputs = hiddenlayers[layer - 1];

                if (layer == hiddenlayers.length - 1) inputs = parameters;
                else inputs = hiddenlayers[layer + 1];

                hiddenNodeArray[layer][hidden] = new HiddenNode(this, inputs, outputs);
            }
        }

        //make input nodes
        for (int i = 0; i < this.parameters; i++)
        {
            inputNodes[i] = new InputNode(this, 0, hiddenlayers[hiddenlayers.length - 1], i);
        }



        //connect result nodes to classify node
        for(int result = 0; result < this.resultNodes.length; result++)
        {
            this.classifyNode.addInput(this.resultNodes[result]);
        }


        //connect result nodes to first layer of hidden nodes
        for (int result = 0; result < resultNodes.length; result++)
        {
            for (int hidden = 0; hidden < hiddenlayers[0]; hidden++)
            {
                resultNodes[result].addInput(hiddenNodeArray[0][hidden]);
            }
        }

        //connect the hidden layers together
        for (int layer = 0; layer < hiddenlayers.length - 1; layer++)
        {
            //connect each layer to the next one
            for (int hiddenParent = 0; hiddenParent < hiddenlayers[layer]; hiddenParent++)
            {
                for (int hiddenChild = 0; hiddenChild < hiddenlayers[layer + 1]; hiddenChild++)
                {
                    hiddenNodeArray[layer][hiddenParent].addInput(hiddenNodeArray[layer + 1][hiddenChild]);
                }
            }
        }

        //connect last hidden layer to input nodes
        for (int hidden = 0; hidden < hiddenlayers[hiddenlayers.length - 1]; hidden++)
        {
            for (int input = 0; input < parameters; input++)
            {
                hiddenNodeArray[hiddenNodeArray.length - 1][hidden].addInput(inputNodes[input]);
            }
        }

        System.out.println("Network created");
    }

    //epoch many times at once
    public void train(int epochs)
    {
        for (int i = 0; i < epochs; i++)
        {
            this.epoch();
        }
    }

    //run the entire training set through the network, using back propagation after every input vector
    private void epoch()
    {
        for (int input = 0; input < this.trainingSet.getSize(); input++)
        {
            //set current input vector parameters
            this.inputVector = this.trainingSet.getInputVector(input);
            this.target = this.trainingSet.getInputVector(input).getTarget();

            this.classifyNode.train();
        }
    }

    public double getErrorRate()
    {
        double correctCount = 0;
        double incorrectCount = 0;

        for (int input = 0; input < this.testingSet.getSize(); input++)
        {
            //set current input vector parameters
            this.inputVector = this.testingSet.getInputVector(input);
            this.target = this.testingSet.getInputVector(input).getTarget();

            this.classifyNode.train();
            if(this.classifyNode.correct())
            {
                correctCount++;
            }
            else
            {
                incorrectCount++;
            }
        }

        double errorRate = 1.0 - (correctCount / (correctCount + incorrectCount));

        return errorRate;
    }

    public double getLearningRate()
    {
        return this.learningRate;
    }
}
