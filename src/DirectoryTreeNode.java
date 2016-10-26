import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiongZZ on 2016-10-16.
 */
public class DirectoryTreeNode {

    private long size;
    private String name;
    private ArrayList<DirectoryTreeNode> children = new ArrayList<>();
    private int type;
    private int byteNum;

    DirectoryTreeNode(int type, String name){
        this.type = type;
        this.name = name;
    }

    public long getSize(){
        return size;
    }

    public String getName(){
        return name;
    }

    public DirectoryTreeNode getChildren(int i){
        return children.get(i);
    }

    public ArrayList<DirectoryTreeNode> getChildren(){
        return children;
    }

    public void setSize(long size){
        this.size = size;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setChildren(int i, DirectoryTreeNode node){
        children.set(i, node);
    }

    public void setChildren(ArrayList<DirectoryTreeNode> children){
        this.children = children;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public void setByteNum(int Bytenum){
        this.byteNum = Bytenum;
    }

    public int getByteNum(){
        return byteNum;
    }
}
