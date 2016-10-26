import java.util.PriorityQueue;

/**
 * Created by XiongZZ on 2016-10-14.
 */
public class HuffmanTree {

    //构建一棵树
    //使用优先队列构建哈夫曼树
    public static BinaryNode HuffTree(int[] freq){
        PriorityQueue<BinaryNode> freqQueue = new PriorityQueue<>(257);
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] != 0) {
                BinaryNode node = new BinaryNode(i, freq[i]);
                freqQueue.add(node);
            }
        }
        BinaryNode node1, node2, interNode;
        int freq1, freq2;
        while (freqQueue.size() > 1) {
            node1 = freqQueue.poll();
            node2 = freqQueue.poll();
            freq1 = node1.getFreq();
            freq2 = node2.getFreq();
            interNode = new BinaryNode(-1, freq1 + freq2);
            interNode.setLeft(node1);
            interNode.setRight(node2);
            freqQueue.add(interNode);
        }
        return freqQueue.poll();
    }



}
