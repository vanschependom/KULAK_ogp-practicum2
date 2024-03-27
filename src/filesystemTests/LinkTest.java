package filesystemTests;

import filesystem.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A JUnit 5 test class for testing the public methods of the Link Class.
 *
 * @author  Flor Demeulemeester
 * @author  Arne Claerhout
 * @author  Vincent Van Schependom
 * @version 1.0
 */
public class LinkTest {

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

    /**********************************************************
     * Constructors
     **********************************************************/

    @Test
    public void testConstructor_LegalCase(){
        Link new_link = new Link("new_link",subSubDir, file1);
        Assertions.assertEquals("new_link",new_link.getName());
        Assertions.assertEquals(subSubDir,new_link.getParentDirectory());
        Assertions.assertEquals(file1,new_link.getLinkedItem());
    }

    @Test
    public void testConstructor_IllegalCase1(){
        // We try to link with another link, so we catch an exception
        assertThrows(IllegalItemException.class, () -> {
            Link new_link = new Link("new_link",subSubDir, link);
        });
    }

    @Test
    public void testConstructor_IllegalCase2(){
        Link new_link = new Link("new_ &[l%%ink",subSubDir, file1);
        Assertions.assertEquals("new_item_1",new_link.getName());
    }

    /**********************************************************
     * linkedItem
     **********************************************************/

    @Test
    public void testGetItem(){
        Assertions.assertEquals(main,link.getLinkedItem());
    }

    @Test
    public void testIsValidLinkedItem(){
        Assertions.assertTrue(Link.isValidLinkedItem(file1));
        Assertions.assertFalse(Link.isValidLinkedItem(link));
    }

    @Test
    public void testHasProperLinkedItem(){
        Assertions.assertTrue(link.hasProperLinkedItem());
    }

}