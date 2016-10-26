import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by XiongZZ on 2016/10/3.
 */
public class BinaryNode implements Comparable<BinaryNode>{
    private BinaryNode left;
    private BinaryNode right;
    private int value;
    private int freq;
    private String code;
    private boolean isRoot = false;

    BinaryNode(int value, int freq) {
        this.value = value;
        this.freq = freq;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public BinaryNode getRight() {
        return right;
    }

    public void setRight(BinaryNode right) {
        this.right = right;
    }

    public void setLeft(BinaryNode left) {
        this.left = left;
    }

    public boolean isLeaf() {
        if (left == null & right == null)
            return true;
        return false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getFreq() {
        return freq;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRoot(boolean isRoot){
        this.isRoot = isRoot;
    }

    public boolean isRoot(){
        return isRoot;
    }

    @Override
    public int compareTo(BinaryNode o) {
        if(getFreq() > o.getFreq())
            return 1;
        else if(getFreq() < o.getFreq())
            return -1;
        else
            return 0;
    }
}
