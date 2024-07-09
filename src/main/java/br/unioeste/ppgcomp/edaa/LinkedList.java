package br.unioeste.ppgcomp.edaa;

public class LinkedList {
    Node head;

    public void insertAtStart(int value) {
        Node newNode = new Node(value);
        newNode.setRight(head);
        head = newNode;
    }

    public void insertAtEnd(int value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
        } else {
            Node last = head;
            while (last.getRight() != null) {
                last = last.getRight();
            }
            last.setRight(newNode);
        }
    }

    public void remove(int index) {
        if (index == 0) {
            head = head.getRight();
            return;
        }

        Node current = head;
        int count = 0;
        while (current != null) {
            if (count == index - 1) {
                if (current.getRight() != null) {
                    current.setRight(current.getRight().getRight());
                }
                return;
            }
            count++;
            current = current.getRight();
        }
    }

    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.getValue());
            current = current.getRight();
        }
        System.out.println();
    }
}
