package hw9;

public class BinomialHeap {
  private BinomialNode head;

  public BinomialHeap() {
    head = null;
  }

  public void binomialInsert(BinomialNode x) {
    BinomialHeap h = new BinomialHeap();
    h.head = x;

    BinomialHeap newH = this.binomialUnion(h);
    head = newH.head;
  }

  public boolean isHeapEmpty() {
    return head == null;
  }

  private void binomialLink(BinomialNode y, BinomialNode parent) {
    y.parent = parent;
    y.sibling = parent.child;
    parent.child = y;
    parent.degree++;
  }

  public BinomialHeap binomialUnion(BinomialHeap heap2) {
    BinomialHeap h = new BinomialHeap();
    h.head = mergeWithRootList(this, heap2);
    head = null;
    heap2.head = null;

    if (h.head == null) {
      return h;
    }

    BinomialNode prev = null;
    BinomialNode curr = h.head;
    BinomialNode next = curr.sibling;

    while (next != null) {
      //Case 1 and 2
      if (curr.degree != next.degree || (next.sibling != null && next.sibling.degree == curr.degree)) {
        prev = curr;
        curr = next;
      } else {
        // Case 3
        if (curr.key < next.key) {
          curr.sibling = next.sibling;
          binomialLink(next, curr);
        } else {
          // Case 4
          if (prev == null)
            h.head = next;
          else
            prev.sibling = next;

          binomialLink(curr, next);
          curr = next;
        }
      }

      next = curr.sibling;
    }

    return h;
  }

  private BinomialNode mergeWithRootList(BinomialHeap h1, BinomialHeap h2) {
    if (h1.head == null)
      return h2.head;
    else if (h2.head == null)
      return h1.head;
    else {
      BinomialNode head;
      BinomialNode curr;
      BinomialNode nextH1 = h1.head,
              nextH2 = h2.head;

      if (h1.head.degree <= h2.head.degree) {
        head = h1.head;
        nextH1 = nextH1.sibling;
      } else {
        head = h2.head;
        nextH2 = nextH2.sibling;
      }

      curr = head;

      while (nextH1 != null && nextH2 != null) {
        if (nextH1.degree <= nextH2.degree) {
          curr.sibling = nextH1;
          nextH1 = nextH1.sibling;
        } else {
          curr.sibling = nextH2;
          nextH2 = nextH2.sibling;
        }

        curr = curr.sibling;
      }

      if (nextH1 != null) {
        curr.sibling = nextH1;
      } else {
        curr.sibling = nextH2;
      }

      return head;
    }
  }

  public int binomialExtractMin() {
    if (head == null) {
      return -1;
    }

    BinomialNode x = head;
    BinomialNode y = x.sibling;
    BinomialNode prev = x;
    BinomialNode xPrev = null;

    //Reverse the order of root list
    while (y != null) {
      if (y.key < x.key) {
        x = y;
        xPrev = prev;
      }
      prev = y;
      y = y.sibling;
    }

    //Set head to point to the actual head
    if (x == head)
      head = x.sibling;
    else {
      xPrev.sibling = x.sibling;
    }

    BinomialHeap h = new BinomialHeap();

    BinomialNode z = x.child;
    while (z != null) {
      BinomialNode next = z.sibling;
      z.sibling = h.head;
      h.head = z;
      z = next;
    }
    BinomialHeap newH = this.binomialUnion(h);
    head = newH.head;
    return x.index;
  }

  public void binomialDecreaseKey(int index, int key, BinomialNode[] list) {
    //Passing list of nodes and index in the function because searching is a costly operation
    BinomialNode x = list[index];
    if (x.key < key) {
      System.out.println("Key is greater than current key");
      return;
    }

    x.key = key;
    BinomialNode y = x;
    BinomialNode z = y.parent;

    //Bubbling up the value
    while (z != null && (y.key < z.key)) {
      int v = y.key;
      y.key = z.key;
      z.key = v;
      v = y.index;
      y.index = z.index;
      z.index = v;

      list[z.index] = z;
      list[y.index] = y;

      y = z;
      z = y.parent;
    }
  }

  public void binomialDelete(int index, BinomialNode[] list) {
    binomialDecreaseKey(index, Integer.MIN_VALUE, list);
    binomialExtractMin();
  }

  public static void main(String[] args) {
    BinomialHeap heap = new BinomialHeap();
    BinomialNode[] nodes;
    nodes = new BinomialNode[10];

    /*for (int i = 0; i < 10; i++) {
      nodes[i] = new BinomialNode(i, (int) Math.round(1000 * Math.random()));
      heap.binomialInsert(nodes[i]);
    }*/

    nodes[0] = new BinomialNode(0, 14);
    nodes[1] = new BinomialNode(1, 4001);
    nodes[2] = new BinomialNode(2, 24);
    nodes[3] = new BinomialNode(3, 34);
    nodes[4] = new BinomialNode(4, 4);
    nodes[5] = new BinomialNode(5, 54);
    nodes[6] = new BinomialNode(6, 10);
    nodes[7] = new BinomialNode(7, 7);

    heap.binomialInsert(nodes[0]);
    heap.binomialInsert(nodes[1]);
    heap.binomialInsert(nodes[2]);
    heap.binomialInsert(nodes[3]);
    heap.binomialInsert(nodes[4]);
    heap.binomialInsert(nodes[5]);
    heap.binomialInsert(nodes[6]);
    heap.binomialInsert(nodes[7]);

    heap.binomialDecreaseKey(2, 2, nodes);
    heap.binomialDecreaseKey(3, 1, nodes);
    heap.binomialDecreaseKey(1, 700, nodes);
    heap.binomialDecreaseKey(6, 6, nodes);

    heap.binomialDelete(3, nodes);

    while (!heap.isHeapEmpty()) {
      int x = heap.binomialExtractMin();
      System.out.println("Index: " + nodes[x].index + " -> "
              + "Value: " + nodes[x].key + ", Degree: " + nodes[x].degree);
    }
  }
}