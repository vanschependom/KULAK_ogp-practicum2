package filesystemTests;

import filesystem.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A JUnit 5 test class for testing the public methods of the Directory Class
 *
 * @author Flor Demeulemeester
 * @author Arne Claerhout
 * @author Vincent Van Schependom
 */
public class DirectoryTest {

    Date timeBeforeCreation = new Date();
    Date timeDuringCreation;
    Directory rootDir, subDir, subsubDir, dirFull, dirNameAndWriteable;
    File file1;
    Link link;

    @BeforeEach
    public void setUpFixture(){
        sleep();
        rootDir = new Directory("rootDir");
        subDir = new Directory(rootDir,"subDir");
        timeDuringCreation = new Date();
        subsubDir = new Directory(subDir,"subsubDir");
        file1 = new File(subsubDir, "file1", 10, true, FileType.PDF);
        dirFull = new Directory(rootDir, "dir", true);
        dirNameAndWriteable = new Directory("dir", false);
        //link = new Link("link", subDir, file1);
    }

    @Test
    public void testDirectoryDirStringBooleanType_LegalCase() {
        sleep();
        assertEquals("dir", dirFull.getName());
        assertEquals(rootDir, dirFull.getRoot());
        assertTrue(dirFull.isWritable());
        assertEquals(0, dirFull.getNbOfItems());
        Date timeNow = new Date();
        assertTrue(timeBeforeCreation.before(dirFull.getCreationTime()));
        assertTrue(dirFull.getCreationTime().before(timeNow));
        assertNull(dirFull.getModificationTime());
    }

    @Test
    public void testDirectoryDirStringBooleanType_Root() {
        Directory dir = new Directory(null, "dir", true);
        assertNull(dir.getParentDirectory());
    }

    @Test
    @Order(1)
    public void testDirectoryDirStringBooleanType_IllegalName() {
        Directory dir = new Directory(subDir, "dir.-hello", true);
        assertTrue(dir.getName().contains("new_item"));
    }

    @Test
    public void testDirectoryDirStringBooleanType_IllegalName2() {
        assertThrows(IllegalParentDirectoryException.class, () -> {
            Directory dir = new Directory(rootDir, "dir", true);
        });
    }

    @Test
    public void testDirectoryDirStringType_LegalCase() {
        sleep();
        assertEquals("subDir", subDir.getName());
        assertEquals(rootDir, subDir.getRoot());
        assertTrue(subDir.isWritable());
        assertEquals(1, subDir.getNbOfItems());
        Date timeNow = new Date();
        assertTrue(timeBeforeCreation.before(subDir.getCreationTime()));
        assertTrue(subDir.getCreationTime().before(timeNow));
        assertEquals(timeDuringCreation, subDir.getModificationTime());
    }

    @Test
    public void testDirectoryDirStringType_IllegalName(){
        Directory dir = new Directory(subsubDir, "dir.#g s");
        assertTrue(dir.getName().contains("new_item"));
    }

    @Test
    public void testDirectoryDirStringType_Root(){
        Directory dir = new Directory(null, "dir");
        assertNull(dir.getParentDirectory());
    }

    @Test
    public void testDirectoryStringBooleanType_LegalCase() {
        sleep();
        assertEquals("dir", dirNameAndWriteable.getName());
        assertNull(dirNameAndWriteable.getParentDirectory());
        assertFalse(dirNameAndWriteable.isWritable());
        assertEquals(0, dirNameAndWriteable.getNbOfItems());
        Date timeNow = new Date();
        assertTrue(timeBeforeCreation.before(dirNameAndWriteable.getCreationTime()));
        assertTrue(dirNameAndWriteable.getCreationTime().before(timeNow));
        assertNull(dirNameAndWriteable.getModificationTime());
    }

    @Test
    public void testDirectoryStringBooleanType_IllegalName() {
        Directory dir = new Directory("dir.#g s", true);
        assertTrue(dir.getName().contains("new_item"));
    }

    @Test
    public void testDirectoryStringType_LegalCase() {
        sleep();
        assertEquals("rootDir", rootDir.getName());
        assertNull(rootDir.getParentDirectory());
        assertTrue(rootDir.isWritable());
        // rootDir has subDir and dirFull
        assertEquals(2, rootDir.getNbOfItems());
        Date timeNow = new Date();
        assertTrue(timeBeforeCreation.before(rootDir.getCreationTime()));
        assertTrue(rootDir.getCreationTime().before(timeNow));
        // Not null because of subDir
        assertNotNull(rootDir.getModificationTime());
    }

    @Test
    public void testDirectoryStringType_IllegalName() {
        Directory dir = new Directory("dir.#g s");
        assertTrue(dir.getName().contains("new_item"));
    }

    @Test
    public void testDirectoryDelete_LegalCase() {
        Directory dir = new Directory(rootDir, "dirDelete", true);
        int index = rootDir.getIndexOf(dir);
        dir.delete();
        assertNotEquals(dir, rootDir.getItemAt(index));
        assertNull(dir.getParentDirectory());
        assertTrue(dir.isDeleted());
    }

    @Test
    public void testDirectoryDelete_IllegalCase1() {
        assertThrows(DirectoryNotEmptyException.class, () -> {
            rootDir.delete();
        });
    }

    @Test
    public void testDirectoryDelete_IllegalCase2() {
        assertThrows(NotWritableException.class, () -> {
            dirNameAndWriteable.delete();
        });
    }

    @Test
    public void testDirectoryRecursivelyDelete_LegalCase1() {
        // We will delete rootDir
        // Everything has to be deleted
        rootDir.deleteRecursive();
        // All the directories must be empty
        assertEquals(0, rootDir.getNbOfItems());
        assertEquals(0, subsubDir.getNbOfItems());
        assertEquals(0, subDir.getNbOfItems());
        // All the parent directories must be null
        assertNull(subDir.getParentDirectory());
        assertNull(subsubDir.getParentDirectory());
        assertNull(file1.getParentDirectory());
        assertNull(dirFull.getParentDirectory());
        // All items must be deleted
        assertTrue(rootDir.isDeleted());
        assertTrue(subDir.isDeleted());
        assertTrue(subsubDir.isDeleted());
        assertTrue(file1.isDeleted());
        assertTrue(dirFull.isDeleted());
    }

    @Test
    public void testDirectoryRecursivelyDelete_LegalCase2() {
        // We will delete subdir
        subDir.deleteRecursive();
        assertEquals(0, subsubDir.getNbOfItems());
        assertEquals(0, subDir.getNbOfItems());
        // There still is one item in rootDir (see SetUpFixture)
        assertEquals(1, rootDir.getNbOfItems());
        assertNull(subDir.getParentDirectory());
        assertNull(subsubDir.getParentDirectory());
        assertNull(file1.getParentDirectory());
        assertTrue(subDir.isDeleted());
        assertTrue(subsubDir.isDeleted());
        assertTrue(file1.isDeleted());
    }

    @Test
    public void testDirectoryRecursivelyDelete_LegalCase3() {
        // We will delete subsubdir
        subsubDir.deleteRecursive();
        assertEquals(0, subsubDir.getNbOfItems());
        assertEquals(0, subDir.getNbOfItems());
        // There still items in rootDir (see SetUpFixture)
        assertEquals(2, rootDir.getNbOfItems());
        assertNull(subsubDir.getParentDirectory());
        assertNull(file1.getParentDirectory());
        assertTrue(subsubDir.isDeleted());
        assertTrue(file1.isDeleted());
    }

    @Test
    public void testDirectoryRecursivelyDelete_IllegalCase1() {
        assertThrows(NotWritableException.class, () -> {
            dirNameAndWriteable.deleteRecursive();
        });
    }

    @Test
    public void testDirectoryRecursivelyDelete_IllegalCase2() {
        Directory dir = new Directory(rootDir, "directory", false);
        assertThrows(NotWritableException.class, () -> {
            rootDir.deleteRecursive();
        });
    }

    @Test
    public void testDirectoryIsRecursivelyDeletable() {
        assertFalse(dirNameAndWriteable.isRecursivelyDeletable());
        assertTrue(rootDir.isRecursivelyDeletable());
        assertTrue(subsubDir.isRecursivelyDeletable());

    }
    @Test
    public void testDirectoryGetItem_LegalCase() {
        assertEquals(file1, subsubDir.getItem("file1"));
    }

    @Test
    public void testDirectoryGetItem_IllegalCaseNameNotInDir() {
        assertThrows(IllegalArgumentException.class, () -> {
            subsubDir.getItem("file2");
        });
    }

    @Test
    public void testDirectoryGetItem_IllegalCaseNameNotValid() {
        assertThrows(IllegalArgumentException.class, () -> {
            subsubDir.getItem("file2 #p>");
        });
    }

    @Test
    public void testDirectoryGetItemAt_LegalCase() {
        File file = new File(subsubDir, "hello", FileType.JAVA);
        assertEquals(file,subsubDir.getItemAt(1));
    }

    @Test
    public void testDirectoryGetItemAt_IllegalCase() {
        File file = new File(subsubDir, "hello", FileType.JAVA);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            subsubDir.getItemAt(6);
        });
    }

    @Test
    public void testDirectoryGetIndexOf_LegalCase() {
        File file = new File(subsubDir, "hello", FileType.JAVA);
        assertEquals(1,subsubDir.getIndexOf(file));
    }

    @Test
    public void testDirectoryGetIndexOf_IllegalCaseItemIsNull() {
        assertThrows(NullPointerException.class, () -> {
            subsubDir.getIndexOf(null);
        });
    }

    @Test
    public void testDirectoryGetIndexOf_IllegalCaseItemNotInDirectory() {
        File file = new File(rootDir, "file1", FileType.PDF);
        assertThrows(IllegalArgumentException.class, () -> {
            subsubDir.getIndexOf(file);
        });
    }

    @Test
    public void testDirectoryCanHaveAsIndex() {
        File file = new File(rootDir, "file1", FileType.PDF);
        File file2 = new File(rootDir, "file2", FileType.PDF);
        assertTrue(rootDir.canHaveAsIndex(1));
        assertFalse(rootDir.canHaveAsIndex(-1));
        assertFalse(rootDir.canHaveAsIndex(6));
    }

    @Test
    public void testDirectoryHasAsItem_LegalCases() {
        File file = new File(rootDir, "file1", FileType.PDF);
        File file2 = new File(rootDir, "file2", FileType.PDF);
        assertTrue(rootDir.hasAsItem(file));
        assertTrue(rootDir.hasAsItem(subDir));
        assertTrue(rootDir.hasAsItem(file2));
        assertFalse(rootDir.hasAsItem(file1));
        assertFalse(rootDir.hasAsItem(subsubDir));
    }

    @Test
    public void testDirectoryHasAsItem_IllegalCase() {
        assertThrows(NullPointerException.class, () -> {
            rootDir.hasAsItem(null);
        });
    }

    @Test
    public void testDirectoryContainsDiskItemWithName_LegalCase() {
        // test different ways to write subdir
        assertTrue(rootDir.containsDiskItemWithName("subdir"));
        assertTrue(rootDir.containsDiskItemWithName("SubdIR"));
        assertTrue(rootDir.containsDiskItemWithName("subDir"));
        // test different ways to write file
        assertTrue(subsubDir.containsDiskItemWithName("fILe1"));
        assertTrue(subsubDir.containsDiskItemWithName("file1"));
        assertFalse(subsubDir.containsDiskItemWithName("file2"));
        assertFalse(subsubDir.containsDiskItemWithName("FILE3"));
    }

    @Test
    public void testDirectoryContainsDiskItemWithName_IllegalCase(){
        assertThrows(IllegalArgumentException.class, () -> {
            rootDir.containsDiskItemWithName("file2 q@p>");
        });
    }

    @Test
    public void testDirectoryContainsDiskItemWithNameCaseSensitive_LegalCase() {
        // test different ways to write subdir
        assertFalse(rootDir.containsDiskItemWithNameCaseSensitive("subdir"));
        assertFalse(rootDir.containsDiskItemWithNameCaseSensitive("SubdIR"));
        assertTrue(rootDir.containsDiskItemWithNameCaseSensitive("subDir"));
        // test different ways to write file1
        assertFalse(subsubDir.containsDiskItemWithNameCaseSensitive("fILe1"));
        assertTrue(subsubDir.containsDiskItemWithNameCaseSensitive("file1"));
    }

    @Test
    public void testDirectoryContainsDiskItemWithNameCaseSensitive_IllegalCase(){
        assertThrows(IllegalArgumentException.class, () -> {
            rootDir.containsDiskItemWithNameCaseSensitive("file2 q@p>");
        });
    }

    @Test
    public void testDirectoryCanHaveAsItem() {
        File file2 = new File(subsubDir, "file6", FileType.PDF);
        assertFalse(rootDir.canHaveAsItem(file2));
        assertFalse(subDir.canHaveAsItem(file2));
        assertTrue(subsubDir.canHaveAsItem(file2));
    }

    @Test
    public void testDirectoryHasProperItems() {
        assertTrue(rootDir.hasProperItems());
        assertTrue(subDir.hasProperItems());
        assertTrue(subsubDir.hasProperItems());
    }

    @Test
    public void testDirectoryIsOrdered() {
        assertTrue(rootDir.isOrdered());
        assertTrue(subDir.isOrdered());
        assertTrue(subsubDir.isOrdered());
        assertTrue(dirFull.isOrdered());
        assertTrue(dirNameAndWriteable.isOrdered());
    }

    @Test
    public void testDirectoryMakeRoot() {
        Directory dir = new Directory(rootDir, "Directory");
        dir.makeRoot();
        assertNull(dir.getParentDirectory());
        assertNotNull(dir.getModificationTime());
    }

    @Test
    public void testDirectoryCanHaveAsParentDirectory() {
        assertFalse(subDir.canHaveAsParentDirectory(rootDir));
        assertTrue(subsubDir.canHaveAsParentDirectory(rootDir));
        assertFalse(subsubDir.canHaveAsParentDirectory(subDir));
        assertFalse(subDir.canHaveAsParentDirectory(subsubDir));
        assertFalse(rootDir.canHaveAsParentDirectory(rootDir));
    }

    @Test
    public void testDirectoryCanHaveAsDiskUsage() {
        File file = new File(rootDir, "item", 20, true, FileType.TEXT);
        assertTrue(rootDir.canHaveAsDiskUsage(30));
        assertTrue(subsubDir.canHaveAsDiskUsage(10));
        assertTrue(dirFull.canHaveAsDiskUsage(0));
        assertFalse(rootDir.canHaveAsDiskUsage(-20));
        assertFalse(subsubDir.canHaveAsDiskUsage(12));
        assertFalse(dirFull.canHaveAsDiskUsage(-30));
    }

    @Test
    public void testDirectoryGetTotalDiskUsage() {
        File file = new File(rootDir, "item", 20, true, FileType.TEXT);
        assertEquals(30, rootDir.getTotalDiskUsage());
        assertEquals(10, subDir.getTotalDiskUsage());
        assertEquals(10, subsubDir.getTotalDiskUsage());
        assertEquals(0, dirFull.getTotalDiskUsage());
        File file2 = new File(subDir, "item2", 50, true, FileType.JAVA);
        assertEquals(80, rootDir.getTotalDiskUsage());
        assertEquals(60, subDir.getTotalDiskUsage());
        assertEquals(10, subsubDir.getTotalDiskUsage());
        assertEquals(0, dirFull.getTotalDiskUsage());
    }


    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}