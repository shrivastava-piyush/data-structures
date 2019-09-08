package hw9;

import java.util.LinkedList;

/**
 * hw9.CircularNode representing Fibonacci Heap data.
 */
public class CircularNode {

  int degree;
  CircularNode parent;
  int key;
  LinkedList<CircularNode> children;
  boolean mark;
  CircularNode prev;
  CircularNode next;

  CircularNode(int key) {
    this.key = key;
    this.degree = 0;
    this.parent = null;
    this.mark = false;
    this.children = null;
    this.prev = this;
    this.next = this;
  }
}