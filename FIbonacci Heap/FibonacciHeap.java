package hw9;

import java.util.LinkedList;

public class FibonacciHeap {

  private FibNode H = new FibNode(null, new CircularLinkedList());
  private int nodeCount = 0;

  public void fibInsert(CircularNode x) {
    if (x == null) {
      System.out.println("Empty node cannot be inserted.");
      return;
    }

    x.degree = 0;
    x.parent = null;
    x.children = null;

    if (H.min == null) {
      //Heap is empty
      H.list = new CircularLinkedList();
      H.min = x;
      H.list.insert(x);
    } else {
      //Insert in the root list and check if the new key is smallest
      H.list.insert(x);
      if (x.key < H.min.key) {
        H.min = x;
      }
    }
    nodeCount++;
  }

  public CircularNode fibExtractMin() {
    CircularNode z = H.min;
    if (z != null) {
      if (z.children != null) {
        //Bring all the children at the root list level
        for (CircularNode child :
                z.children) {
          if (child != null) {
            H.list.insert(child);
            child.parent = null;
          }
        }
      }
      //Delete z from root list
      H.list.delete(z.key);
      if (z == z.next) {
        H.min = null;
      } else {
        H.min = z.next;
        fibConsolidate();
      }
      nodeCount--;
    }
    return z;
  }

  public void fibConsolidate() {
    int maxDegree;
    if (H.list.getSize() == 1) {
      maxDegree = 1;
    } else {
      //Max degree calculated using the theorem proving maxDegree = logn base fibonacci number
      maxDegree = (int) Math.ceil(Math.log(nodeCount) / Math.log((1 + Math.sqrt(5) / 2)));
    }

    CircularNode[] A = new CircularNode[maxDegree + 1];
    for (int i = 0; i <= maxDegree; i++) {
      A[i] = null;
    }

    CircularNode tempNode = H.list.head;
    do {
      CircularNode parent = tempNode;
      int d = parent.degree;
      while (A[d] != null) {
        //Link two nodes of same degree
        CircularNode futureChild = A[d];

        //Check if parent needs to be swapped with child
        if (parent.key > futureChild.key) {
          CircularNode temp = parent;
          parent = futureChild;
          futureChild = temp;
        }
        fibLink(futureChild, parent);
        A[d] = null;
        d = d + 1;
      }
      A[d] = parent;
      if (tempNode == tempNode.next) {
        //Reassign tempNode to deal with concurrency condition
        tempNode = parent.next;
      } else {
        tempNode = tempNode.next;
      }
      //Iterate through the root list
    } while (tempNode != H.list.tail.next);

    H.min = null;

    for (int j = 0; j <= maxDegree; j++) {
      if (A[j] != null) {
        if (H.min == null) {
          H.list = new CircularLinkedList();
          H.list.insert(A[j]);
          H.min = A[j];
        } else {
          H.list.insert(A[j]);
          if (A[j].key < H.min.key) {
            H.min = A[j];
          }
        }
      }
    }
  }

  private void fibLink(CircularNode futureChild, CircularNode parent) {
    if (futureChild == null || parent == null) {
      return;
    }

    //Delete child to be linked from root list
    H.list.delete(futureChild.key);
    if (parent.children == null) {
      parent.children = new LinkedList<>();
    }
    futureChild.parent = parent;
    //Ensure circularity of nodes
    futureChild.prev = futureChild;
    futureChild.next = futureChild;
    //Increment parent degree
    parent.degree = parent.degree + 1;
    //Mark child as false, since it is a new child of this parent.
    futureChild.mark = false;
    parent.children.add(futureChild);
  }

  //Union in constant time
  public void fibUnion(FibNode H2) {

    H.list.tail.next = H2.list.head;
    H2.list.head.prev = H.list.tail;
    H2.list.tail.next = H.list.head;
    H.list.tail = H2.list.tail;

    if (H.min == null || (H2.min != null && H2.min.key < H.min.key)) {
      H.min = H2.min;
    }
  }

  public void fibCut(CircularNode x, CircularNode y) {

    //Remove x from children of y
    if (y.children != null) {
      y.children.remove(x);
    }
    //Decrement y's degree
    y.degree = y.degree - 1;
    //Insert x in the root list
    H.list.insert(x);
    x.parent = null;
    //Make x unmarked
    x.mark = false;
  }

  //Cascading cut to recursively go up the tree for moving previously marked nodes to root list.
  public void fibCascadingCut(CircularNode y) {
    CircularNode z = y.parent;
    if (z != null) {
      if (!y.mark) {
        y.mark = true;
      } else {
        fibCut(y, z);
        fibCascadingCut(z);
      }
    }
  }

  public void fibDecreaseKey(CircularNode x, int key) {
    if (x == null) {
      return;
    }

    if (key > x.key) {
      System.out.println("New key cannot be greater than the current key");
      return;
    }

    x.key = key;
    CircularNode xParent = x.parent;
    if (xParent != null && x.key < xParent.key) {
      fibCut(x, xParent);
      fibCascadingCut(xParent);
    }

    if (x.key < H.min.key) {
      H.min = x;
    }
  }

  public void fibDelete(CircularNode x) {
    fibDecreaseKey(x, Integer.MIN_VALUE);
    fibExtractMin();
  }

  //Print the fibonacci heap nodes to depth 3 from root list
  public void fibPrint() {
    if (H == null || H.min == null) {
      System.out.println("Heap is empty");
      return;
    }

    System.out.println("\nPrinting the heap");

    System.out.println("\nMin key: " + H.min.key + "\n");
    CircularNode tempNode = H.list.head;
    do {
      System.out.println("Key: " + tempNode.key + "\nDegree: " + tempNode.degree + "\n");
      if (tempNode.children != null) {
        for (CircularNode child :
                tempNode.children) {
          if (child != null) {
            System.out.println("Child key: " + child.key + "\nDegree: " + child.degree + "\n");
            if (child.children != null) {
              for (CircularNode grandChild :
                      child.children) {
                if (grandChild != null) {
                  System.out.println("Grand child key: " + grandChild.key + "\nDegree: " + grandChild.degree + "\n");
                  if (grandChild.children != null) {
                    for (CircularNode grandGrandChild :
                            grandChild.children) {
                      if (grandGrandChild != null) {
                        System.out.println("Grand grand child key: " + grandGrandChild.key + "\nDegree: " + grandGrandChild.degree + "\n");
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      tempNode = tempNode.next;
    } while (tempNode != H.list.tail.next);
  }

  public static void main(String args[]) {
    FibonacciHeap heap = new FibonacciHeap();
    CircularNode x = new CircularNode(11);
    heap.fibInsert(x);
    CircularNode x1 = new CircularNode(2);
    heap.fibInsert(x1);
    CircularNode x2 = new CircularNode(1);
    heap.fibInsert(x2);
    CircularNode x3 = new CircularNode(32);
    heap.fibInsert(x3);
    CircularNode x4 = new CircularNode(31);
    heap.fibInsert(x4);
    heap.fibExtractMin();

    FibonacciHeap heap2 = new FibonacciHeap();
    CircularNode y = new CircularNode(17);
    heap2.fibInsert(y);
    CircularNode y1 = new CircularNode(3);
    heap2.fibInsert(y1);
    CircularNode y2 = new CircularNode(5);
    heap2.fibInsert(y2);
    CircularNode y3 = new CircularNode(22);
    heap2.fibInsert(y3);
    CircularNode y4 = new CircularNode(100);
    heap2.fibInsert(y4);
    heap2.fibExtractMin();

    heap.fibUnion(heap2.H);
    heap.fibPrint();
    System.out.println("Size of root list: " + heap.H.list.getSize());

    heap.fibDelete(x4);
    heap.fibPrint();

    /*heap.fibExtractMin();

    heap.fibPrint();

    heap.fibExtractMin();

    heap.fibPrint();

    heap.fibDelete(x3);

    heap.fibPrint();*/

    /*heap.fibExtractMin();
    System.out.println(heap.H.min.key + "\n");
    heap.fibExtractMin();
    System.out.println(heap.H.min.key + "\n");
    heap.fibExtractMin();
    System.out.println(heap.H.min.key + "\n");
    heap.fibExtractMin();
    System.out.println(heap.H.min.key + "\n");*/
  }
}
