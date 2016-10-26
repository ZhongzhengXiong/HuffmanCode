import java.io.*;
import java.nio.Buffer;
import java.util.*;

/**
 * Created by XiongZZ on 2016/10/3.
 */
public class Compress {

    public static void Encode(BinaryNode node, String code, String[] codeTable) {
        if (node.isLeaf()) {
            node.setCode(code);
            codeTable[node.getValue()] = code;
            if(node.isRoot())
                codeTable[node.getValue()] = "0";
            return;
        }
        Encode(node.getLeft(), code + "0", codeTable);
        Encode(node.getRight(), code + "1", codeTable);
        return;
    }

    public static void CompressSingleFile(File file, BufferedOutputStream outputStream, boolean isFolder) throws IOException {
        BufferedInputStream in = null;
        String[] codeTable = new String[256];
        int[] freq = new int[256];
        String name = file.getName();
        int filelength = (int) file.length();
        //写入文件名称大小以及名称
        if(!isFolder){
            outputStream.write(name.getBytes().length);
            outputStream.write(name.getBytes());
        }
        //写入文件总的字符数
        outputStream.write(Integer.rotateRight(filelength, 24));
        outputStream.write(Integer.rotateRight(filelength, 16));
        outputStream.write(Integer.rotateRight(filelength, 8));
        outputStream.write(filelength);
        if(filelength != 0){
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                int tempbyte;
                int i = 0;
                while ((tempbyte = in.read()) != -1) {
                    i++;
                    freq[tempbyte]++;
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //遍历树得到相应的编码表
            BinaryNode root = HuffmanTree.HuffTree(freq);
            root.setRoot(true);
            Encode(root, "", codeTable);
            //写入压缩文件



            //第一步，写头文件
            int count = 0;
            for (int i = 0; i < freq.length; i++) {
                if (freq[i] != 0)
                    count++;
            }
            outputStream.write(count - 1);
            for (int i = 0; i < freq.length; i++) {
                int tmp = freq[i];
                if (tmp != 0) {
                    outputStream.write(i);//存入原有的index
                    //存入频率， 8位一存，共32位
                    outputStream.write(Integer.rotateRight(tmp, 24));
                    outputStream.write(Integer.rotateRight(tmp, 16));
                    outputStream.write(Integer.rotateRight(tmp, 8));
                    outputStream.write(tmp);
                }
            }
            //第二步，将文件转化成由01构成的字符串并写入文件
            StringBuilder content = new StringBuilder("");
            in = new BufferedInputStream(new FileInputStream(file));
            int tempbyte;//读取源文件的当前byte
            String tmpString;//当前byte对应的01字符串

            while ((tempbyte = in.read()) != -1) {
                content.append(codeTable[tempbyte]);
                if (content.length() > 8) {
                    int i;
                    for (i = 0; i < content.length() - 8; i += 8) {
                        outputStream.write(Integer.parseInt(content.substring(i, i + 8), 2));
                    }
                    content.delete(0, i);
                }
            }
            tmpString = content.substring(0);
            int tmpLen = tmpString.length();
            for (int i = 0; i < 8 - tmpLen; i++) {
                content.append('0');
            }
            outputStream.write(Integer.parseInt(content.toString(), 2));
            in.close();
        }
    }

}
