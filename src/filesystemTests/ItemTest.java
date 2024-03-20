package filesystemTests;

import filesystem.*;
import org.junit.jupiter.api.*;

/**
 * A JUnit 5 test class for testing the public methods of the Item Class.
 *
 * @author  Flor Demeulemeester
 * @author  Arne Claerhout
 * @author  Vincent Van Schependom
 * @version 1.0
 */
public class ItemTest {

    Directory rootDir, subDir, subsubDir;
    File file1, main;
    Link link;

    @BeforeEach
    public void setUpFixture(){
        rootDir = new Directory("rootDir");
        subDir = new Directory(rootDir,"subDir");
        subsubDir = new Directory(subDir,"subsubDir");
        file1 = new File(subsubDir, "file1", FileType.PDF);
        main = new File(subDir, "main", FileType.JAVA);
        link = new Link("link_to_main", rootDir, main);
    }

    @Test
    public void absolutePathTest1(){
        Assertions.assertEquals("/rootDir/subDir/subsubDir/file1.pdf", file1.getAbsolutePath());
    }

    @Test
    public void absolutePathTest2(){
        Assertions.assertEquals("/rootDir/subDir/main.java", main.getAbsolutePath());
    }

    @Test
    public void absolutePathTest3(){
        Assertions.assertEquals("/rootDir/link_to_main", link.getAbsolutePath());
    }

    @Test
    public void absolutePathTest4(){
        Assertions.assertEquals("/rootDir", rootDir.getAbsolutePath());
    }

    @Test
    public void absolutePathTest5(){
        Assertions.assertEquals("/rootDir/subDir", subDir.getAbsolutePath());
    }

    @Test
    public void absolutePathTest6(){
        Assertions.assertEquals("/rootDir/subDir/subsubDir", subsubDir.getAbsolutePath());
    }

}
