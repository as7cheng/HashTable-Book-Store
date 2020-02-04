//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: BookHashTable.java
// Files: BookHashTable.java BookHashTableTest.java Book.java
// Course: Comp Sci 400, section 002
//
// Author: Shihan Cheng
// Email: scheng93@wisc.edu
// Lecturer's Name: Debra Deppeler
// Description: This program is designed to create a hashTable to store node of
// data-pairs of String and Book.
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

// TODO: comment and complete your HashTableADT implementation
//
// TODO: implement all required methods
// DO ADD REQUIRED PUBLIC METHODS TO IMPLEMENT interfaces
//
// DO NOT ADD ADDITIONAL PUBLIC MEMBERS TO YOUR CLASS
// (no public or package methods that are not in implemented interfaces)
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// if open addressing: describe probe sequence
// if buckets: describe data structure for each bucket
//
// **Answer: The hashTable is an ArrayList and each bucket is also an ArrayList.
// The structure of the hashTable is ArrayList of ArrayList
//
// TODO: explain your hashing algorithm here
// **Answer: Use buckets with ArrayList of ArrayList to solve the collision. Get
// the hash
// index by dividing the table size by the hash index provided by java's
// hashCode method, add book value into the ArrayList at that index. When load
// factor meet the load factor threshold, create a new ArrayList with size of
// (double the original size) + 1, then rehash all values and put them into the
// new ArrayList.
//
// NOTE: you are not required to design your own algorithm for hashing,
// since you do not know the type for K,
// you must use the hashCode provided by the <K key> object

/**
 * HashTable implementation that uses:
 * 
 * @param <String> unique identifier for each <K,V> pair, may not be null
 * @param <Book>   associated value with a key, value may be null
 */
public class BookHashTable implements HashTableADT<String, Book> {

  /**
   * Inner Class to create a pair of key and book, in order to avoid people
   * insert same book with different keys
   */
  private class Node {

    // Inner class field
    private String key;
    private Book value;

    /**
     * Constructor for Node
     * 
     * @param key   Unique key for the book in the hashTable
     * @param value Book value associated with the key
     */
    private Node(String key, Book value) {
      this.key = key;
      this.value = value;
    }

    /**
     * Get the key of the node
     * 
     * @return key of the node
     */
    private String getKey() {
      return this.key;
    }

    /**
     * Get the book of the node
     * 
     * @return value of the node
     */
    private Book getBook() {
      return this.value;
    }
  }

  /** The initial capacity that is used if none is specified user */
  static final int DEFAULT_CAPACITY = 101;

  /** The load factor that is used if none is specified by user */
  static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;

  /** The Collision Resolution Scheme */
  static final int CollisionResolutionScheme = 4;

  // Outer class field
  private ArrayList<ArrayList<Node>> hashTable;
  private int capacity;
  private double loadFactorThreshold;
  private int numKeys;

  /**
   * REQUIRED default no-arg constructor Uses default capacity and sets load
   * factor threshold for the newly created hash table.
   */
  public BookHashTable() {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR_THRESHOLD);
  }

  /**
   * Creates an empty hash table with the specified capacity and load factor.
   * 
   * @param initialCapacity     number of elements table should hold at start.
   * @param loadFactorThreshold the ratio of items/capacity that causes table to
   *                            resize and rehash
   */
  public BookHashTable(int initialCapacity, double loadFactorThreshold) {
    // Complete a constructor that accepts initial capacity
    // and load factor threshold and initializes all fields
    this.capacity = initialCapacity;
    this.loadFactorThreshold = loadFactorThreshold;
    this.numKeys = 0;
    this.hashTable = new ArrayList<ArrayList<Node>>(this.capacity);
    // Create ArrayList for every position of hashTable
    for (int i = 0; i < this.capacity; i++) {
      this.hashTable.add(new ArrayList<Node>());
    }
  }

  /**
   * Method to insert a node with unique key and book value into this hashTable
   * 
   * @param key   Key of the node
   * @param value Book value of the node
   * @throws IllegalNullKeyException If the parameter key is null
   * @throws DuplicateKeyException   If the node is already in this hasTable or
   *                                 other node is already with this key
   */
  @Override
  public void insert(String key, Book value)
      throws IllegalNullKeyException, DuplicateKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    if (this.contains(key))
      throw new DuplicateKeyException();

    // Check if this hashTable needs to be rehashed
    if (this.reHash()) {
      this.reHashing();
    }
    // Insert a new node with given key and value in this hashTable and increase
    // the number of keys
    int index = this.hashIndex(key);
    this.hashTable.get(index).add(new Node(key, value));
    this.numKeys++;
  }

  /**
   * Helper Method to get the hash index for a key
   * 
   * @param key Key of the node
   * @return Hash index of the node
   */
  private int hashIndex(String key) {
    return Math.abs(key.hashCode() % this.capacity);
  }

  /**
   * Method to remove a node by a key from this hashTable
   * 
   * @param key Key of the node that needs to be removed
   * @return true if remove the node successfully. False otherwise
   * @throws IllegalNullKeyException If the parameter key is null
   */
  @Override
  public boolean remove(String key) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();

    // Check if the node is in this hashTbale through checking its key
    if (this.contains(key)) {
      // Get the node's hash index
      int index = this.hashIndex(key);
      // Check the bucket ArrayList and find the match key, remove it and return
      // true
      for (Node node : this.hashTable.get(index)) {
        if (node.getKey().equals(key)) {
          this.hashTable.get(index).remove(node);
          this.numKeys--;
          return true;
        }
      }
    }
    // Nothing found, return false
    return false;
  }

  /**
   * Method to get the book value of the node by its key
   * 
   * @param key Key of the node
   * @return Book value of the node
   * @throws IllegalNullKeyException If the parameter key is null
   * @throws KeyNotFoundException    If the node is not in this hashTable
   */
  @Override
  public Book get(String key)
      throws IllegalNullKeyException, KeyNotFoundException {
    if (key == null)
      throw new IllegalNullKeyException();
    if (!this.contains(key))
      throw new KeyNotFoundException();

    // Get the hash index of the key of the node
    int index = this.hashIndex(key);
    // Iterate the ArrayList at the index of this hashTable to find the matching
    // key of the node, then return the book value of the node
    for (Node node : this.hashTable.get(index)) {
      if (node.getKey().equals(key)) {
        return node.getBook();
      }
    }
    // No matching key found, return null
    return null;
  }

  /**
   * Method to get number of nodes in this hashTable
   * 
   * @return Number of nodes in this hashTable
   */
  @Override
  public int numKeys() {
    return this.numKeys;
  }

  /**
   * Method to get the load factor threshold of this hashTable
   * 
   * @return The load factor threshold
   */
  @Override
  public double getLoadFactorThreshold() {
    return this.loadFactorThreshold;
  }

  /**
   * Method to get the capacity of this hashTable
   * 
   * @return The capacity of this hashTable
   */
  @Override
  public int getCapacity() {
    return this.capacity;
  }

  /**
   * Method to get the collision resolution scheme used for this hash table.
   * 
   * @return CollisionResolutionScheme
   */
  @Override
  public int getCollisionResolutionScheme() {
    return CollisionResolutionScheme;
  }

  /**
   * Method to check if the Node by given key is in this hashTable
   * 
   * @param key Key of the node
   * @return true if the node's key is in this hashTable. False otherwise
   */
  private boolean contains(String key) {
    // If this hashTable is empty, return false
    if (this.tableEmpty())
      return false;
    // Get the hash index of the key of the node
    int index = this.hashIndex(key);
    // Iterate the ArrayList at the index of this hashTable to find the match
    // key, then return true
    if (!this.hashTable.get(index).isEmpty()) {
      for (Node value : this.hashTable.get(index)) {
        if (value.getKey().equals(key)) {
          return true;
        }
      }
    }
    // No match key found, return false
    return false;
  }

  /**
   * Helper method to check if current hashTable needs to be resized by
   * comparing current load factor and the load factor threshold
   * 
   * @return true if current load factor is greater than or equal to the load
   *         factor threshold. False otherwise
   */
  private boolean reHash() {
    return (double) this.numKeys() >= this.getLoadFactorThreshold()
        * this.getCapacity();
  }

  /**
   * Helper method to check if current hashTable is empty by inspecting the
   * number of nodes in the hashTable
   * 
   * @return true if no node in the hashTable. false otherwise
   */
  private boolean tableEmpty() {
    return this.numKeys() <= 0;
  }

  /**
   * Helper method to re-size the hashTable and rehash all nodes in the
   * hashTable
   */
  private void reHashing() {
    // Increase the capacity of the hashTable
    this.capacity = this.capacity * 2 + 1;
    // Create a temporary list to store the original hashTable
    ArrayList<ArrayList<Node>> temp = this.hashTable;
    // Increase the size of the hashTable by double the size + 1
    this.hashTable = new ArrayList<ArrayList<Node>>(this.capacity);
    // Iterate the temporary list and add all original nodes into the new
    // hashTable
    for (int i = 0; i < this.capacity; i++) {
      this.hashTable.add(new ArrayList<Node>());
    }

    for (int i = 0; i < temp.size(); i++) {
      for (Node node : temp.get(i)) {
        try {
          this.insert(node.getKey(), node.getBook());
        } catch (Exception e) {
        }
      }
    }
  }

}
