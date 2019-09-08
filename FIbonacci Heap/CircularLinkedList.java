package hw9;

public class CircularLinkedList {

  public CircularNode head;
  public CircularNode tail;

  public void insert(CircularNode node) {
    if (head == null) {
      head = node;
      tail = node;
      head.prev = tail;
      tail.next = head;
      return;
    }

    //Making node the new head and deferring the old head by one position
    node.next = head;
    head.prev = node;
    head = node;
    //maintaining the circular condition
    head.prev = tail;
    tail.next = head;
  }

  public void delete(int key) {
    if (head == null) {
      return;
    }

    CircularNode nodeToDelete = search(key);
    if (nodeToDelete == null) {
      return;
    }

    //If the node to be deleted is head and size of list is 1
    if (head.next == null && head.prev == null) {
      head = null;
      tail = null;

      //If the node to be deleted is head
    } else if (nodeToDelete == head) {
      head = head.next;
      head.prev = tail;
      tail.next = head;

      //If the node to be deleted is tail
    } else if (nodeToDelete == tail) {
      tail = tail.prev;
      tail.next = head;
      head.prev = tail;
    } else {

      //Detach the nodeTodelete from both sides

      //Replace previous node's next value with current node's next value
      nodeToDelete.prev.next = nodeToDelete.next;
      //Replace next node's previous pointer with current node's previous pointer
      nodeToDelete.next.prev = nodeToDelete.prev;
    }

  }

  public CircularNode search(int key) {

    if (head == null) {
      return null;
    }

    CircularNode selectedNode = head;
    do {
      if (selectedNode.key == key) {
        return selectedNode;
      } else {
        selectedNode = selectedNode.next;
      }
    } while (selectedNode != tail.next);

    return null;
  }

  public int getSize() {

    int count = 0;

    if (head == null) {
      return 0;
    }

    CircularNode selectedNode = head;

    do {
        count = count + 1;
      selectedNode = selectedNode.next;
    } while (selectedNode != tail.next);

    return count;
  }


  public int getMaxDegree() {

    int maxDegree = 0;

    if (head == null) {
      return 0;
    }

    if (head.degree > maxDegree) {
      maxDegree = head.degree;
    }

    CircularNode selectedNode = head.next;
    while (selectedNode != tail.next) {
      if (selectedNode.degree > maxDegree) {
        maxDegree = selectedNode.degree;
      }
      selectedNode = selectedNode.next;
    }

    return maxDegree;
  }

  public void printList(){
    CircularNode node = head;
    do {
      System.out.println(node.key + "\n");
      node = node.next;
    } while (node != tail.next);
  }

  public static void main(String[] args) {
    CircularLinkedList list = new CircularLinkedList();
    list.insert(new CircularNode(3));
    list.insert(new CircularNode(4));
    list.insert(new CircularNode(6));
    list.insert(new CircularNode(1));
    list.insert(new CircularNode(31));
    list.insert(new CircularNode(313));
    list.delete(4);

    list.printList();
  }

}
