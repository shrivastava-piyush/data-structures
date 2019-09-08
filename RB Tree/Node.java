/**
   * Node representing RB Tree data.
   */
  public class Node {
    Node left;
    Node right;
    Node parent;
    int key;
    Color nodeColor;

    Node(int key) {
      this.key = key;
      this.right = null;
      this.left = null;
    }
  }