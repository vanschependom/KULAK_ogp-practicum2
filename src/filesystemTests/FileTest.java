package filesystemTests;

import filesystem.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileTest {

	static String legalNameLetters = "legalNameJustLetters";
	static String legalNameSymbols = "legal_Name_With_Symbols.-";
	static String illegalNameEmpty = "";
	static String illegalNameSymbols = "illegalName*%";
	static String illegalNameSpaces = "illegal name";
	static String illegalNameNull = null;

	Directory basicDirectory = new Directory("aBasicDirectory");

	/**********************************************************
	 * Constructors
	 **********************************************************/

	@Test
	public void mostExtendedConstructor() {

		// legal names
		File myFile1 = new File(basicDirectory, legalNameLetters, 0, true, FileType.TEXT);
		File myFile2 = new File(basicDirectory, legalNameSymbols, 1000, false, FileType.TEXT);
		assertEquals(myFile1.getName(),legalNameLetters);
		assertEquals(myFile2.getName(),legalNameSymbols);

		// illegal name
		File myFile3 = new File(basicDirectory, illegalNameEmpty, 5000, true, FileType.TEXT);
		File myFile4 = new File(basicDirectory, illegalNameSymbols, 0, true, FileType.TEXT);
		File myFile5 = new File(basicDirectory, illegalNameSpaces, 0, true, FileType.TEXT);
		File myFile6 = new File(basicDirectory, illegalNameNull, 0, true, FileType.TEXT);
		assertEquals(myFile3.getName(),"new_item_1");
		assertEquals(myFile4.getName(),"new_item_2");
		assertEquals(myFile5.getName(),"new_item_3");
		assertEquals(myFile6.getName(),"new_item_4");

		// sizes are implemented nominally, so we don't test illegal cases.
		assertEquals(myFile1.getSize(), 0);
		assertEquals(myFile2.getSize(), 1000);
		assertEquals(myFile3.getTotalDiskUsage(), 5000);
		assertEquals(myFile4.getTotalDiskUsage(), 0);

		// writability
		assertTrue(myFile1.isWritable());
		assertFalse(myFile2.isWritable());

		// DIRECTORY
		// null reference
		assertThrows(IllegalParentDirectoryException.class ,()->{
			File myFile = new File(null, legalNameLetters, 5000, true, FileType.TEXT);
		});
		// file already in dir
		assertThrows(IllegalParentDirectoryException.class ,()->{
			File myFile = new File(basicDirectory, legalNameLetters, 5000, true, FileType.TEXT);
		});
		assertEquals(myFile1.getParentDirectory(), basicDirectory);

		// modification time
		assertNull(myFile1.getModificationTime());

		// filetype
		assertThrows(IllegalArgumentException.class, ()->{
			File myFile = new File(basicDirectory, "unusedName", 50, false, null);
		});
		assertEquals(myFile1.getFileType(), FileType.TEXT);

	}


}
