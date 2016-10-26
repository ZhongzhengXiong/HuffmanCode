import sun.rmi.server.InactiveGroupException;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by XiongZZ on 2016/10/4.
 */
public class CompressDirectory {

    //构建一颗文件树
    public static void readFile(File file, DirectoryTreeNode node) {

        int len = file.list().length;
        for (int i = 0; i < len; i++) {
            File file1 = file.listFiles()[i];
            if (file1.isDirectory()) {
                DirectoryTreeNode node1 = new DirectoryTreeNode(0, file1.getName());
                node.getChildren().add(node1);
                readFile(file1, node1);
            } else {
                DirectoryTreeNode node2 = new DirectoryTreeNode(1, file1.getName());
                node.getChildren().add(node2);
            }
        }
    }


    public static void WriteHead(DirectoryTreeNode root, BufferedOutputStream outputStream) throws IOException {

        outputStream.write(0);
        int byteNum = root.getByteNum();
        outputStream.write(Integer.rotateRight(byteNum, 24));
        outputStream.write(Integer.rotateRight(byteNum, 16));
        outputStream.write(Integer.rotateRight(byteNum, 8));
        outputStream.write(byteNum);
        outputStream.write(root.getName().getBytes().length);
        outputStream.write(root.getName().getBytes());

        ArrayList<DirectoryTreeNode> children = root.getChildren();
        int len = children.size();
        for (int i = 0; i < len; i++) {
            DirectoryTreeNode node = children.get(i);
            if (node.getType() == 0) {
                WriteHead(node, outputStream);
            } else {
                outputStream.write(node.getType());
                outputStream.write(node.getName().getBytes().length);
                outputStream.write(node.getName().getBytes());
            }
        }
    }


    public static void getBytenum(DirectoryTreeNode root) {
        root.setByteNum(root.getName().getBytes().length + 6);
        ArrayList<DirectoryTreeNode> children = root.getChildren();
        int size = children.size();
        for (int i = 0; i < size; i++) {
            DirectoryTreeNode node = children.get(i);
            if (node.getType() == 0) {
                getBytenum(node);
            } else {
                node.setByteNum(node.getName().getBytes().length + 2);
            }
            root.setByteNum(root.getByteNum() + node.getByteNum());
        }
    }

    public static void writeFile(File file, BufferedOutputStream outputStream) throws IOException {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                writeFile(listFiles[i], outputStream);
            }
        } else {
            Compress.CompressSingleFile(file, outputStream, true);
        }
    }
}



