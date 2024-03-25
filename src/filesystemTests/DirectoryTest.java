package filesystemTests;

import filesystem.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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
        file1 = new File(subsubDir, "file1", FileType.PDF);
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
        assertTrue(dir.isRoot());

    }

    @Test
    public void testDirectoryDirStringBooleanType_IllegalName() {
        Directory dir = new Directory(subDir, "dir.-hello", true);
        assertEquals("new_item_1", dir.getName());
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
        assertEquals("new_item_1", dir.getName());
    }

    @Test
    public void testDirectoryDirStringType_Root(){
        Directory dir = new Directory(null, "dir");
        assertTrue(dir.isRoot());
    }

    @Test
    public void testDirectoryStringBooleanType_LegalCase() {
        sleep();
        assertEquals("dir", dirNameAndWriteable.getName());
        assertTrue(dirNameAndWriteable.isRoot());
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
        // this is the 3rd test with a wrong name so the 3rd new item
        assertEquals("new_item_1", dir.getName());
    }

    @Test
    public void testDirectoryStringType_LegalCase() {
        sleep();
        assertEquals("rootDir", rootDir.getName());
        assertTrue(rootDir.isRoot());
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
        assertEquals("new_item_1", dir.getName());
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
    public void testDirectoryDelete_IllegalCase() {
        assertThrows(DirectoryNotEmptyException.class, () -> {
            rootDir.delete();
        });
    }

    @Test
    public void testDirectoryRecursivelyDelete_LegalCase() {
        rootDir.deleteRecursive();
        assertEquals(0, rootDir.getNbOfItems());
        assertEquals(0, subDir.getNbOfItems());
        assertEquals(0, subsubDir.getNbOfItems());
        assertNull(subDir.getParentDirectory());
        assertNull(subsubDir.getParentDirectory());
        assertNull(file1.getParentDirectory());
        assertTrue(rootDir.isDeleted());
        assertTrue(subDir.isDeleted());
        assertTrue(subsubDir.isDeleted());
        assertTrue(file1.isDeleted());
        assertEquals(0, rootDir.getNbOfItems());
        assertEquals(0, subDir.getNbOfItems());
        assertEquals(0, subsubDir.getNbOfItems());
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


    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
