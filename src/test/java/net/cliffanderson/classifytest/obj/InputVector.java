package net.cliffanderson.classifytest.obj;


/**
 * Created by andersonc12 on 11/27/2015.
 */
public class InputVector
{
    private int target;
    private int[] data;

    public InputVector(int target, int[] data)
    {
        this.target = target;
        this.data = data;
    }

    public int[] getData() {
        return data;
    }

    public int getTarget() {
        return target;
    }


}
