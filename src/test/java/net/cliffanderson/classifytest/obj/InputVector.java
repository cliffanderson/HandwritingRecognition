package net.cliffanderson.classifytest.obj;


/**
 * Created by andersonc12 on 11/27/2015.
 */
public class InputVector
{
    String target;
    int[] data;

    public InputVector(String target, int[] data)
    {



        this.target = target;
        this.data = data;
    }

    public int[] getData() {
        return data;
    }

    public String getTarget() {
        return target;
    }


}
