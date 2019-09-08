import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class to implement hashing function with ability to insert, delete, find data, increase the
 * value and list the keys.
 */
class Hasher {

  public Node[] hashTable;
  public static final int MAXHASH = 701;

  Hasher() {
    hashTable = new Node[MAXHASH];
  }

  /**
   * Hash function based on the division method. MAXHASH is chosen as a prime far way from powers of
   * 2.
   */
  public int hash(String word) {
    int sum = 0, j = 0;
    while (j < word.length()) {
      sum = sum + word.charAt(j) * ++j * j;
    }

    return Math.abs(sum % MAXHASH);
  }

  /**
   * Main function to execute the interactive program.
   */
  public static void main(String args[]) {

    //Default file is 'ALice in Wonderland' provided in the homework.
    String input = readFile("res/alice_in_wonderland.txt");
    Hasher hasher = new Hasher();

    Scanner sc = new Scanner(System.in);
    sc.useDelimiter("[\r\n/]");
    boolean isInputDone = false;

    //Infinite loop for user to enter the values for hash table
    while (true) {
      if (!isInputDone) {
        showInputPrompt(sc, input, hasher);
        isInputDone = true;
      } else {
        showOptions();

        String key;
        int value;

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
            key = sc.next();
            System.out.println("Please enter the value:");
            value = sc.nextInt();
            hasher.insert(key.toLowerCase(), value, true);
            break;
          case 2:
            System.out.println("Please enter the key:");
            key = sc.next();
            hasher.delete(key.toLowerCase());
            break;
          case 3:
            System.out.println("Please enter the key:");
            key = sc.next();
            System.out.println("Please enter the value:");
            value = sc.nextInt();
            hasher.increase(key.toLowerCase(), value);
            break;
          case 4:
            System.out.println("Please enter the key:");
            key = sc.next();
            hasher.find(key.toLowerCase());
            break;
          case 5:
            hasher.listKeys();
            break;
          case 6:
            System.exit(0);
            break;
          default:

        }
      }
    }
  }

  public static void showOptions() {
    System.out.println("\nPlease choose amongst the options:" +
            "\n1. Insert a key into hash" +
            "\n2. Delete a key from hash" +
            "\n3. Increase value of a key" +
            "\n4. Find a key" +
            "\n5. List all keys" +
            "\n6. Exit\n");
  }

  /**
   * Function to prompt the user for input string. If the input string given by user is empty, the
   * default one is taken and inserted into the hashtable.
   */
  public static void showInputPrompt(Scanner scanner, String input, Hasher hasher) {
    System.out.println("Please enter the string. \nIf string is empty, " +
            "text will be taken from a default file:");

    String line = scanner.nextLine();
    if (line.isEmpty()) {
      System.out.println("Input was empty. Taking data from the file 'Alice in Wonderland'.");
    } else {
      input = line;
    }

    String[] words = input
            .replaceAll("\\p{P}", "")
            .toLowerCase()
            .split("\\s+");

    if (words.length > 0) {
      for (String word : words) {
        hasher.insert(word, 1, false);
      }
    }

    System.out.println("Data inserted into the hash.");
  }

  /**
   * Function to read the file, if hash table is to be created from a file.
   */
  public static String readFile(String filePath) {
    String content = "/res/alice_in_wonderland.txt";

    try {
      content = new String(Files.readAllBytes(Paths.get(filePath)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return content;
  }

  /**
   * Node representing hash table data with key as word and value as count.
   */
  class Node {
    String word;
    long count;
    Node next;

    Node(String word, int count) {
      this.word = word;
      this.count = count;
    }
  }

  /**
   * Insertion for the new element is in the front, while for an old element, the value is updated.
   */
  public void insert(String key, int value, boolean showMessage) {

    if (key == null || key.isEmpty()) {
      System.out.println("Word cannot be empty.");
      return;
    }

    if (value < 0) {
      System.out.println("Count cannot be negative.");
      return;
    }

    int hashKey = hash(key);
    Node head = hashTable[hashKey];

    //If head is null, insert value at head
    if (head == null) {
      head = new Node(key, value);
      hashTable[hashKey] = head;
      if (showMessage) {
        System.out.println("Value inserted into the hash.");
      }
      return;
    }

    Node temp = head;
    Node prev = head;

    while (temp != null && !temp.word.equals(key)) {
      prev = temp;
      temp = temp.next;
    }

    //Check if word exists. If not create a new node and link it to the linked list.
    if (temp == null) {
      temp = new Node(key, value);
      prev.next = temp;
      temp.next = null;
    } else {
      //If the node exists, increase the count.
      temp.count = temp.count + value;
    }
    if (showMessage) {
      System.out.println("Value inserted into the hash.");
    }
  }

  /**
   * Increase the value of key if it exists.
   */
  public void increase(String key, int value) {

    if (key == null || key.isEmpty()) {
      System.out.println("Word cannot be empty.");
    }

    if (value < 0) {
      System.out.println("Count cannot be negative.");
      return;
    }

    int hashKey = hash(key);
    Node head = hashTable[hashKey];

    if (head == null) {
      System.out.println("Word cannot be found.");
      return;
    }

    Node temp = head;

    //Traverse through the list to find the node with key
    while (temp != null && !temp.word.equals(key)) {
      temp = temp.next;
    }

    if (temp == null) {
      System.out.println("Word cannot be found.");
    } else {
      //If word is found, increase the count
      temp.count = temp.count + value;
      System.out.println("Value for " + temp.word + " increased to " + temp.count);
    }
  }

  /**
   * Delete the node from list if it exists.
   */
  public void delete(String word) {

    if (word == null || word.isEmpty()) {
      System.out.println("Word cannot be empty.");
      return;
    }

    int hashKey = hash(word);
    Node head = hashTable[hashKey];

    /*
     * Check if head is to be deleted.
     * */
    if (head != null && head.word.equals(word)) {
      //If head is the only element in the list, delete it,
      // otherwise assign next element to head.
      if (head.next == null) {
        hashTable[hashKey] = null;
      } else {
        hashTable[hashKey] = head.next;
      }
      System.out.println("Value deleted from the hash.");
      return;
    }

    Node temp = head;
    //Maintain the previous node for assigning to the next one after deletion
    Node prev = head;

    //traverse through the list to find node
    while (temp != null && !temp.word.equals(word)) {
      prev = temp;
      temp = temp.next;
    }

    if (temp != null) {
      //Assign previous.next address to the node next to temp
      prev.next = temp.next;
      temp.next = null;
      System.out.println("Value deleted from the hash.");
    } else {
      System.out.println("Word cannot be found.");
    }

  }

  public void find(String key) {

    if (key == null || key.isEmpty()) {
      System.out.println("Word cannot be empty.");
      return;
    }

    int hashKey = hash(key);
    Node head = hashTable[hashKey];

    //If head is the key to be found, print it
    if (head != null && head.word.equals(key)) {
      System.out.println("Key: " + head.word + " \nValue: " + head.count);
      return;
    }

    Node temp = head;

    //traverse through the list to find the node
    while (temp != null && !temp.word.equals(key)) {
      temp = temp.next;
    }

    if (temp != null) {
      System.out.println("Key: " + temp.word + "\nValue: " + temp.count);
      return;
    }

    System.out.println("Key could not be found.");
  }

  public void listKeys() {

    int total = 0;
    //Loop to traverse through the hash table
    for (Node n :
            hashTable) {
      if (n != null) {
        System.out.println("\n" + n);
        int size = 0;
        //Loop to traverse through the corresponding list
        while (n != null) {
          System.out.println("Key: " + n.word + " \nCount: " + " " + n.count + "\n");
          n = n.next;
          size++;
        }
        System.out.println("Linked List Size:" + size);
        total = +size;
      }
    }

    if (total == 0) {
      System.out.println("Hash table is empty.");
    }
  }

}