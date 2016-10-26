/**
 * Created by XiongZZ on 2016-10-13.
 */
public class Debug {
    public static void Traversal(BinaryNode node) {
        System.out.println(node.getValue() + " " + node.getFreq());
        if (node.isLeaf()) {
            return;
        }
        Traversal(node.getLeft());
        Traversal(node.getRight());
    }
}
