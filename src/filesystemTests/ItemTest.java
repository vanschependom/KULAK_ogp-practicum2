package filesystemTests;

import filesystem.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A JUnit 5 test class for testing the public methods of the Item Class.
 *
 * @author  Flor Demeulemeester
 * @author  Arne Claerhout
 * @author  Vincent Van Schependom
 * @version 1.0
 */
public class ItemTest {

    Directory rootDir, subDir, subSubDir;
    File file1, main;
    Link link;


    @BeforeEach
    public void setUpFixture(){
        rootDir = new Directory("rootDir");
        subDir = new Directory(rootDir,"subDir");
        subSubDir = new Directory(subDir,"subSubDir");
        file1 = new File(subSubDir, "file1", FileType.PDF);
        main = new File(subDir, "main", FileType.JAVA);
        link = new Link("link_to_main", rootDir, main);
    }

    @Test
    public void absolutePathTest1(){
        assertEquals("/rootDir/subDir/subSubDir/file1.pdf", file1.getAbsolutePath());
    }

    @Test
    public void absolutePathTest2(){
        assertEquals("/rootDir/subDir/main.java", main.getAbsolutePath());
    }

    @Test
    public void absolutePathTest3(){
        assertEquals("/rootDir/link_to_main", link.getAbsolutePath());
    }

    @Test
    public void absolutePathTest4(){
        assertEquals("/rootDir", rootDir.getAbsolutePath());
    }

    @Test
    public void absolutePathTest5(){
        assertEquals("/rootDir/subDir", subDir.getAbsolutePath());
    }

    @Test
    public void absolutePathTest6(){
        assertEquals("/rootDir/subDir/subSubDir", subSubDir.getAbsolutePath());
    }

    @Test
    public void testItemChangeName() {
        assertEquals("file1", file1.getName());
        file1.changeName("g $d#@a");
        assertEquals("file1", file1.getName());
        assertNull(file1.getModificationTime());
        file1.changeName("mamamia");
        assertEquals("mamamia", file1.getName());
        assertNotNull(file1.getModificationTime());
        File file2 = new File(rootDir, "file2", 12, false, FileType.TEXT);
        assertThrows(NotWritableException.class, () -> {
            file2.changeName("test");
        });
        Directory dir = new Directory(rootDir, "testDir", false);
        assertThrows(NotWritableException.class, () -> {
            dir.changeName("test");
        });

    }

    @Test
    public void testItemIsAddableToDirectory() {
        assertTrue(file1.isAddableToDirectory(rootDir));
        assertTrue(file1.isAddableToDirectory(subDir));
        assertFalse(file1.isAddableToDirectory(subSubDir));
        File file2 = new File(rootDir, "file1", FileType.PDF);
        assertFalse(file2.isAddableToDirectory(rootDir));
        assertTrue(file2.isAddableToDirectory(subDir));
        assertFalse(file2.isAddableToDirectory(subSubDir));
        assertFalse(file2.isAddableToDirectory(null));
    }

    @Test
    public void testItemIsDirectOrIndirectChildOf() {
        // test different files and directories
        assertTrue(file1.isDirectOrIndirectChildOf(rootDir));
        assertTrue(file1.isDirectOrIndirectChildOf(subDir));
        assertTrue(file1.isDirectOrIndirectChildOf(subSubDir));
        assertTrue(main.isDirectOrIndirectChildOf(rootDir));
        assertTrue(main.isDirectOrIndirectChildOf(subDir));
        assertFalse(main.isDirectOrIndirectChildOf(subSubDir));
        assertFalse(rootDir.isDirectOrIndirectChildOf(rootDir));
        assertFalse(rootDir.isDirectOrIndirectChildOf(subDir));
        assertFalse(rootDir.isDirectOrIndirectChildOf(subSubDir));
    }

    @Test
    public void testItemMove_LegalCase_1() {
        assertTrue(subSubDir.hasAsItem(file1));
        file1.move(rootDir);
        assertFalse(subSubDir.hasAsItem(file1));
        assertTrue(rootDir.hasAsItem(file1));
        assertEquals(rootDir, file1.getParentDirectory());
    }

    @Test
    public void testItemMove_LegalCase_2() {
        Directory dir = new Directory("dirTest");
        rootDir.move(dir);
        assertTrue(dir.hasAsItem(rootDir));
        assertEquals(dir, rootDir.getParentDirectory());
    }

    @Test
    public void testItemMove_IllegalCase_1() {
        assertTrue(subDir.hasAsItem(subSubDir));
        assertThrows(IllegalParentDirectoryException.class, () -> {
            subDir.move(subSubDir);
        });
    }

    @Test
    public void testItemMove_IllegalCase_2() {
        assertTrue(subDir.hasAsItem(subSubDir));
        assertThrows(NullPointerException.class, () -> {
            subDir.move(null);
        });
    }

    @Test
    public void testItemMove_IllegalCase_3() {
        assertTrue(subDir.hasAsItem(subSubDir));
        assertThrows(IllegalParentDirectoryException.class, () -> {
            subDir.move(subDir);
        });
    }

    @Test
    public void testDeleteRecursive_LegalCase1(){
        // We will delete rootDir
        // Everything has to be deleted
        rootDir.deleteRecursive();
        // All the directories must be empty
        assertEquals(0, rootDir.getNbOfItems());
        assertEquals(0, subSubDir.getNbOfItems());
        assertEquals(0, subDir.getNbOfItems());
        // All the parent directories must be null
        assertNull(subDir.getParentDirectory());
        assertNull(subSubDir.getParentDirectory());
        assertNull(file1.getParentDirectory());
        assertNull(main.getParentDirectory());
        assertNull(link.getParentDirectory());
        // All items must be deleted
        assertTrue(rootDir.isDeleted());
        assertTrue(subDir.isDeleted());
        assertTrue(subSubDir.isDeleted());
        assertTrue(file1.isDeleted());
        assertTrue(main.isDeleted());
        assertTrue(link.isDeleted());
    }

    @Test
    public void testDeleteRecursive_LegalCase2(){
        // We will delete main
        main.deleteRecursive();
        assertEquals(2, rootDir.getNbOfItems());
        assertEquals(1, subSubDir.getNbOfItems());
        assertEquals(1, subDir.getNbOfItems());
        assertNull(main.getParentDirectory());
        assertTrue(main.isDeleted());
    }

    @Test
    public void testDeleteRecursive_IllegalCase1() {
        rootDir.setWritable(false);
        assertThrows(NotWritableException.class, () -> {
            rootDir.deleteRecursive();
        });
    }

    @Test
    public void testDeleteRecursive_IllegalCase2() {
        file1.setWritable(false);
        assertThrows(NotWritableException.class, () -> {
            file1.deleteRecursive();
        });
    }

}
