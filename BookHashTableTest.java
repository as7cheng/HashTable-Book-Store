//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: BookHashTableTest.java
// Files: BookHashTable.java BookHashTableTest.java Book.java
// Course: Comp Sci 400, section 002
//
// Author: Shihan Cheng
// Email: scheng93@wisc.edu
// Lecturer's Name: Debra Deppeler
// Description: This program is designed to create a Data Structure storing the
// data of pairs of key and value, to create tests to test if the Data Structure
// works via using JUnit test. This Data Structure implements AVL tree
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////


import org.junit.After;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test HashTable class implementation to ensure that required functionality
 * works for all cases.
 */
public class BookHashTableTest {

  // Default name of books data file
  public static final String BOOKS = "books_clean.csv";

  // Empty hash tables that can be used by tests
  static BookHashTable bookObject;
  static ArrayList<Book> bookTable;

  static final int INIT_CAPACITY = 2;
  static final double LOAD_FACTOR_THRESHOLD = 0.49;

  static Random RNG = new Random(0); // seeded to make results repeatable
                                     // (deterministic)

  /** Create a large array of keys and matching values for use in any test */
  @BeforeAll
  public static void beforeClass() throws Exception {
    bookTable = BookParser.parse(BOOKS);
  }

  /** Initialize empty hash table to be used in each test */
  @BeforeEach
  public void setUp() throws Exception {
    // TODO: change HashTable for final solution
    bookObject = new BookHashTable(INIT_CAPACITY, LOAD_FACTOR_THRESHOLD);
  }

  /** Not much to do, just make sure that variables are reset */
  @AfterEach
  public void tearDown() throws Exception {
    bookObject = null;
  }

  private void insertMany(ArrayList<Book> bookTable)
      throws IllegalNullKeyException, DuplicateKeyException {
    for (int i = 0; i < bookTable.size(); i++) {
      bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
    }
  }

  /**
   * IMPLEMENTED AS EXAMPLE FOR YOU Tests that a HashTable is empty upon
   * initialization
   */
  @Test
  public void test000_collision_scheme() {
    if (bookObject == null)
      fail("Gg");
    int scheme = bookObject.getCollisionResolutionScheme();
    if (scheme < 1 || scheme > 9)
      fail("collision resolution must be indicated with 1-9");
  }

  /**
   * IMPLEMENTED AS EXAMPLE FOR YOU Tests that a HashTable is empty upon
   * initialization
   */
  @Test
  public void test000_IsEmpty() {
    // "size with 0 entries:"
    assertEquals(0, bookObject.numKeys());
  }

  /**
   * IMPLEMENTED AS EXAMPLE FOR YOU Tests that a HashTable is not empty after
   * adding one (key,book) pair
   * 
   * @throws DuplicateKeyException
   * @throws IllegalNullKeyException
   */
  @Test
  public void test001_IsNotEmpty()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    String expected = "" + 1;

    // "size with one entry:"
    assertEquals(expected, "" + bookObject.numKeys());
  }

  /**
   * IMPLEMENTED AS EXAMPLE FOR YOU Test if the hash table will be resized after
   * adding two (key,book) pairs given the load factor is 0.49 and initial
   * capacity to be 2.
   */
  @Test
  public void test002_Resize()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    int cap1 = bookObject.getCapacity();
    bookObject.insert(bookTable.get(1).getKey(), bookTable.get(1));
    int cap2 = bookObject.getCapacity();

    // "size with one entry:"
    assertTrue(cap2 > cap1 & cap1 == 2);
  }

  /**
   * Test if we can successfully insert a single data pair into the hashTable
   */
  @Test
  public void test003_insert_one_book()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject = new BookHashTable(10, 0.8);
    bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    int cap = bookObject.getCapacity();

    assertTrue(bookObject.numKeys() == 1);
  }

  /**
   * Test if we can successfully insert multiple data pairs into the hashTable
   */
  @Test
  public void test004_insert_more_books()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject = new BookHashTable(1000, 0.8);
    insertMany(bookTable);

    if (bookObject.numKeys() != bookTable.size())
      fail("Test004: number of book in hashTable is " + bookObject.numKeys()
          + ", number of books in bookTable is" + bookTable.size());
  }

  /**
   * Test if the DuplicateKeyException can be thrown successfully when insert
   * books with same keys
   */
  @Test
  public void test005_insert_duplicate()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject = new BookHashTable(10, 0.8);

    try {
      bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
      bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
      fail("Test005: when the seconde time inserting the same key, "
          + "DuplicateKeyException should be thrown");
    } catch (DuplicateKeyException e) {
    }
  }

  /**
   * Test if we can successfully insert one data pair then remove it from the
   * hashTable
   */
  @Test
  public void test006_insert_one_remove_one()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject = new BookHashTable(10, 0.8);


    bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    boolean remove = bookObject.remove(bookTable.get(0).getKey());

    if (remove != true || bookObject.numKeys() != 0)
      fail("Test006: After removing the only data pair from the hashTable, "
          + "the number of keys should be 0, but it is "
          + bookObject.numKeys());
  }

  /**
   * Test if we can successfully insert multiple data pairs then remove them
   * from the hashTable
   */
  @Test
  public void test007_insert_mult_remove_mult()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject = new BookHashTable(1000, 0.8);
    insertMany(bookTable);

    for (int i = 0; i < bookTable.size(); i++) {
      boolean remove = bookObject.remove(bookTable.get(i).getKey());
      if (remove != true)
        fail("Test007 part 1: when remove a data pair, the remove method "
            + "should return true");
    }

    if (bookObject.numKeys() != 0)
      fail("Test007: After removing all data pairs from the hashTable, "
          + "the number of keys should be 0, but it is "
          + bookObject.numKeys());
  }

  /**
   * Test if false be returned when trying to remove a data pair not in the
   * hashTable
   */
  @Test
  public void test008_remove_key_not_in_hashTable()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject = new BookHashTable(10, 0.8);
    bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    boolean remove = bookObject.remove(bookTable.get(1).getKey());

    if (remove != false)
      fail("Test008: When try to remove a data pair not in the hashTable, "
          + "it should return false");
  }

  /**
   * Test if we can successfully get correct book after inserting multiple data
   * pairs into the hashTable
   */
  @Test
  public void test009_get_book() throws IllegalNullKeyException,
      DuplicateKeyException, KeyNotFoundException {
    bookObject = new BookHashTable(1000, 0.8);
    insertMany(bookTable);
    for (int i = 0; i < bookTable.size(); i++) {
      String key = bookTable.get(i).getKey();
      if (!bookObject.get(key).equals(bookTable.get(i)))
        fail("Test009: The index at " + i + " should be book "
            + bookTable.get(i).toString() + ", but it is "
            + bookObject.get(key).toString());
    }
  }

  /**
   * Test if KeyNotFoundException be thrown when trying to get a book not in the
   * hashTable
   */
  @Test
  public void test010_get_book_not_in_hashTable()
      throws IllegalNullKeyException, DuplicateKeyException,
      KeyNotFoundException {
    bookObject = new BookHashTable(10, 0.8);
    
    try {
      bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
      bookObject.get(bookTable.get(1).getKey());
      fail(
          "Test010: When try to get a book not in the hashTable, KeyNotFoundException"
              + " should be thrown");
    } catch (KeyNotFoundException e) {
    }
  }
  
}
