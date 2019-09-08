import java.util.InputMismatchException;
import java.util.Scanner;

public class RBTree {

  private Node root;

  /**
   * Sort following the in-order traversal
   */
  public void sort(Node x) {
    if (x == null) {
      System.out.println("Tree is empty.");
      return;
    }

    if (x.left != null) {
      sort(x.left);
    }
    System.out.println(x.key);
    if (x.right != null) {
      sort(x.right);
    }
  }

  public Node search(int key) {
    Node x = root;
    while (x != null && key != x.key) {
      if (key < x.key) {
        x = x.left;
      } else {
        x = x.right;
      }
    }
    return x;
  }

  public Node findMin(Node x) {
    while (x.left != null) {
      x = x.left;
    }
    return x;
  }

  public Node findMax(Node x) {
    while (x.right != null) {
      x = x.right;
    }
    return x;
  }

  public Node findTreeSuccessor(int key) {

    Node x = search(key);

    if (x == null) {
      return null;
    }

    if (x.right != null) {
      //If right child is not null, find and return successor from here
      //Find minimum possible value that is greater than x.key
      return findMin(x.right);
    }

    //Initially define y as x's parent
    Node y = x.parent;
    while (y != null && x == y.right) {
      x = y;
      y = y.parent;
    }
    return y;
  }

  public Node findTreePredecessor(int key) {

    Node x = search(key);

    if (x == null) {
      return null;
    }

    if (x.left != null) {
      //If left child is not null, find and return successor from here
      return findMax(x.left);
    }

    //Initially define y as x's parent
    Node y = x.parent;
    while (y != null && x == y.left) {
      x = y;
      y = y.parent;
    }
    return y;
  }

  public void insert(Node z) {
    if (root == null) {
      root = new Node(z.key);
      root.right = null;
      root.left = null;
      root.nodeColor = Color.Black;
      return;
    }

    Node currNode = root;
    Node prev = root;

    while (currNode != null) {
      prev = currNode;

      if (z.key < currNode.key) {
        currNode = currNode.left;
      } else {
        currNode = currNode.right;
      }
    }

    z.parent = prev;

    if (z.key < prev.key) {
      prev.left = z;
    } else {
      prev.right = z;
    }

    z.left = null;
    z.right = null;
    z.nodeColor = Color.Red;
    insertFix(z);
  }

  public void delete(Node root, Node z) {

  }

  public void deleteFix(Node z) {

  }

  public void insertFix(Node z) {
    while (z != null && z.parent != null && z.parent.nodeColor == Color.Red) {
      //When the parent is a left child
      if (z.parent.parent != null && z.parent == z.parent.parent.left) {

        Node uncle = z.parent.parent.right;
        //Case 1
        if (uncle != null && uncle.nodeColor == Color.Red) {
          z.parent.nodeColor = Color.Black;
          uncle.nodeColor = Color.Black;
          z.parent.parent.nodeColor = Color.Red;
          z = z.parent.parent;
        }
        //Case 2
        else {
          if (z == z.parent.right) {
            z = z.parent;
            leftRotate(z);
          }

          //Case 3
          if (z.parent != null && z.parent.parent != null) {
            z.parent.nodeColor = Color.Black;
            z.parent.parent.nodeColor = Color.Red;
            rightRotate(z.parent.parent);
          }
        }

      } else if (z.parent.parent != null) {
        //Case 1
        Node uncle = z.parent.parent.left;
        if (uncle != null && uncle.nodeColor == Color.Red) {
          z.parent.nodeColor = Color.Black;
          uncle.nodeColor = Color.Black;
          z.parent.parent.nodeColor = Color.Red;
          z = z.parent.parent;
        }
        else {
          //Case 2
          if (z == z.parent.left) {
            z = z.parent;
            rightRotate(z);
          }

          //Case 3
          if (z.parent != null && z.parent.parent != null) {
            z.parent.nodeColor = Color.Black;
            z.parent.parent.nodeColor = Color.Red;
            leftRotate(z.parent.parent);
          }
        }
      }
    }
    root.nodeColor = Color.Black;
  }

  private void leftRotate(Node z) {
    if (z == null) {
      return;
    }

    Node y = z.right;

    if (y == null) {
      return;
    }

    z.right = y.left;

    if (y.left != null) {
      y.left.parent = z;
    }

    y.parent = z.parent;
    if (z.parent == null) {
      root = y;
    } else if (z == z.parent.left) {
      z.parent.left = y;
    } else {
      z.parent.right = y;
    }
    y.left = z;
    z.parent = y;
  }

  private void rightRotate(Node z) {
    if (z == null) {
      return;
    }

    Node y = z.left;
    if (y == null) {
      return;
    }

    z.left = y.right;

    if (y.right != null) {
      y.right.parent = z;
    }

    y.parent = z.parent;
    if (z.parent == null) {
      root = y;
    } else if (z == z.parent.right) {
      z.parent.right = y;
    } else {
      z.parent.left = y;
    }
    y.right = z;
    z.parent = y;
  }

  /**
   * Main function to execute the interactive program.
   */
  public static void main(String args[]) {

    RBTree tree = new RBTree();

    Scanner sc = new Scanner(System.in);
    sc.useDelimiter("[\r\n/]");

    //Infinite loop for user to enter the values for hash table
    while (true) {

      showOptions();

      int key;
      int choice;

      try {
        choice = sc.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Invalid choice. Taking the default choice 1.");
        choice = 1;
      }

      switch (choice) {
        case 1:
          System.out.println("Please enter the key:");
          key = sc.nextInt();
          tree.insert(new Node(key));
          break;
        case 2:
          System.out.println("Please enter the key:");
          key = sc.nextInt();
          tree.delete(tree.root, new Node(key));
          break;
        case 3:
          tree.sort(tree.root);
          break;
        case 4:
          System.out.println("Please enter the key:");
          key = sc.nextInt();
          Node searchNode = tree.search(key);
          if (searchNode != null) {
            System.out.println("Node found with key: " + searchNode.key);
          } else {
            System.out.println("Node not found.");
          }
          break;
        case 5:
          Node min = tree.findMin(tree.root);
          if (min != null) {
            System.out.println("Minimum value is " + min.key);
          }
          break;
        case 6:
          Node max = tree.findMax(tree.root);
          if (max != null) {
            System.out.println("Maximum value is " + max.key);
          }
          break;
        case 7:
          System.out.println("Please enter the key:");
          key = sc.nextInt();
          Node successor = tree.findTreeSuccessor(key);
          if (successor != null) {
            System.out.println("Successor for " + key + " is " + successor.key);
          } else {
            System.out.println("Successor doesn't exist for " + key);
          }
          break;
        case 8:
          System.out.println("Please enter the key:");
          key = sc.nextInt();
          Node predecessor = tree.findTreePredecessor(key);
          if (predecessor != null) {
            System.out.println("Predecessor for " + key + " is " + predecessor.key);
          } else {
            System.out.println("Predecessor doesn't exist for " + key);
          }
          break;
        case 9:
          System.exit(0);
          break;
        default:
      }
    }
  }

  public static void showOptions() {
    System.out.println("\nPlease choose amongst the options:" +
            "\n1. Insert a key into tree" +
            "\n2. Delete a key from tree" +
            "\n3. Sort" +
            "\n4. Search a key" +
            "\n5. Find minimum value in tree" +
            "\n6. Find maximum value in tree" +
            "\n7. Get successor for a key" +
            "\n8. Get predecessor for a key" +
            "\n9. Exit\n");
  }
}