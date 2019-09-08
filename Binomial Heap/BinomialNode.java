package hw9;

public class BinomialNode {
  //Value contained in the node
  public int key;
  //Useful parameter to keep track of, when the heap has to be used for prim's algo
  int index;
  BinomialNode parent;
  BinomialNode child;
  BinomialNode sibling;
  int degree;

  BinomialNode(int index, int key) {
    this.key = key;
    this.index = index;
    parent = null;
    child = null;
    sibling = null;
    degree = 0;
  }
}
