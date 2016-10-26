import java.io.*;

/**
 * Created by XiongZZ on 2016-10-14.
 */
public class Decompress {


    public static void Decompress(BufferedInputStream inputStream, File output) throws IOException {

        //写入新的文件
        if (!output.exists()) {
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //读取源文件长度
        int fileLength1 = inputStream.read();
        int fileLength2 = inputStream.read();
        int fileLength3 = inputStream.read();
        int fileLength4 = inputStream.read();
        int fileLength = Integer.rotateLeft(fileLength1,24)+Integer.rotateLeft(fileLength2, 16)+
                Integer.rotateLeft(fileLength3, 8)+fileLength4;
        if(fileLength != 0){
            //读取头部文件创建huffman树
            int[] freq = new int[256];
            int arrayCount = inputStream.read() + 1;
            for (int i = 0; i < arrayCount; i++) {
                int j = inputStream.read();
                int j1, j2, j3, j4;
                j1 = Integer.rotateLeft(inputStream.read(), 24);
                j2 = Integer.rotateLeft(inputStream.read(), 16);
                j3 = Integer.rotateLeft(inputStream.read(), 8);
                j4 = inputStream.read();
                freq[j] = j1 + j2 + j3 + j4;
            }
            //构建哈夫曼树
            BinaryNode root = HuffmanTree.HuffTree(freq);
            //开始写入新的文件
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(output));
            String str;
            int tempByte;
            BinaryNode tmpNode = root;
            long count = 0;//计数器采集最后一位
            while (count != fileLength) {
                tempByte = inputStream.read();
                str = Integer.toBinaryString(tempByte);
                int len = str.length();
                for (int i = 0; i < 8 - len; i++) {
                    str = '0' + str;
                }
                for (int i = 0; i < 8; i++) {
                    if(!tmpNode.isLeaf()){
                        if (str.charAt(i) == '0') {
                            tmpNode = tmpNode.getLeft();
                        } else {
                            tmpNode = tmpNode.getRight();
                        }
                    }

                    if (tmpNode.isLeaf()) {
                        out.write(tmpNode.getValue());
                        count++;
                        if(count == fileLength)
                            break;
                        tmpNode = root;
                    }
                }
            }
            out.close();
        }

    }


}


