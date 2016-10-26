import java.io.*;
import java.util.ArrayList;

 /**
 * Created by XiongZZ on 2016-10-16.
 */
public class DecompressDirectory {

    public static void ParseHead(BufferedInputStream inputStream, DirectoryTreeNode root) throws IOException {
        int count = 0;
        int nameLength = root.getName().getBytes().length;
        while (count < root.getByteNum()-6-nameLength) {
            int read;
            read = inputStream.read();
            int type = read;
            if (type == 0) {

                int byteNum1 = inputStream.read();
                int byteNum2 = inputStream.read();
                int byteNum3 = inputStream.read();
                int byteNum4 = inputStream.read();
                int byteNum = Integer.rotateLeft(byteNum1, 24) + Integer.rotateLeft(byteNum2, 16) + Integer.rotateLeft(byteNum3, 8) + byteNum4;
                int namelength = inputStream.read();
                byte[] name = new byte[namelength];
                inputStream.read(name);

                DirectoryTreeNode node = new DirectoryTreeNode(0, new String(name));
                node.setByteNum(byteNum);
                root.getChildren().add(node);
                count += byteNum;
                ParseHead(inputStream, node);
            } else {

                int namelength = inputStream.read();
                byte[] name = new byte[namelength];
                inputStream.read(name);
                DirectoryTreeNode node = new DirectoryTreeNode(1, new String(name));
                root.getChildren().add(node);
                count += (2 + namelength);
            }
        }
    }

    //建立文件夹
    public static void BuildFile(DirectoryTreeNode root, String path) throws IOException {
        int type = root.getType();
        path = path + "\\" + root.getName();
        File file1 = new File(path);
        if (type == 0) {
            file1.mkdir();
            ArrayList<DirectoryTreeNode> children = root.getChildren();
            for (int i = 0; i < children.size(); i++) {
                DirectoryTreeNode node = children.get(i);
                BuildFile(node, path);
            }
        } else {
            file1.createNewFile();
        }

    }

    //解压文件夹
    public static void parseFile(BufferedInputStream inputStream, File file) throws IOException {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                parseFile(inputStream, listFiles[i]);
            }
        } else {
            Decompress.Decompress(inputStream, file);
        }
    }

}
