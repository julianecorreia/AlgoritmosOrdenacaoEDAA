package br.unioeste.ppgcomp.edaa;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.TreeVisitor;

public class BinaryTree {

    Node root;

    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    private Node insertRecursive(Node current, int value) {
        if (current == null) {
            return new Node(value);
        }

        if (value < current.getValue()) {
            current.setLeft(insertRecursive(current.getLeft(), value));
        } else if (value > current.getValue()) {
            current.setRight(insertRecursive(current.getRight(), value));
        } else {
            return current;
        }

        return current;
    }

    public int getHeight(Node node) {
        if (node == null) {
            return -1;
        } else {
            int leftHeight = getHeight(node.getLeft());
            int rightHeight = getHeight(node.getRight());
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    public int getHeight() {
        return getHeight(root);
    }
}
