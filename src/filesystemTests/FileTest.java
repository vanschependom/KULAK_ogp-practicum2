package filesystemTests;
import filesystem.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A JUnit 6 test class for testing the public methods of the File Class
 * @author Flor Demeulemeester
 * @author Arne Claerhout
 * @author Vincent Van Schependom
 *
 */
public class FileTest {

	static String legalNameLetters = "legalNameJustLetters";
	static String legalNameSymbols = "legal_Name_With_Symbols.-";
	static String illegalNameEmpty = "";
	static String illegalNameSymbols = "illegalName*%";
	static String illegalNameSpaces = "illegal name";
	static String illegalNameNull = null;

	File fileDirStringIntBooleanType;
	File fileDirStringType;
	Date timeBeforeConstruction, timeAfterConstruction;

	File fileNotWritable;
	Date timeBeforeConstructionNotWritable, timeAfterConstructionNotWritable;
	Directory basicDirectory;
	Directory anotherDirectory;

	@BeforeEach
	public void setUpFixture(){
		basicDirectory = new Directory("aBasicDirectory");
		anotherDirectory = new Directory("anotherDirectory");
		timeBeforeConstruction = new Date();
		fileDirStringIntBooleanType = new File(basicDirectory, legalNameLetters, 200, true, FileType.TEXT);
		fileDirStringType = new File(basicDirectory, legalNameSymbols, FileType.TEXT);
		timeAfterConstruction = new Date();

		timeBeforeConstructionNotWritable = new Date();
		fileNotWritable = new File(basicDirectory, "anotherName", 50, false, FileType.JAVA);
		timeAfterConstructionNotWritable = new Date();
	}

	@Test
	public void testFileDirStringIntBooleanType_LegalCase() {
		assertEquals(legalNameLetters,fileDirStringIntBooleanType.getName());
		assertEquals(200,fileDirStringIntBooleanType.getSize());
		assertTrue(fileDirStringIntBooleanType.isWritable());
		assertNull(fileDirStringIntBooleanType.getModificationTime());
		assertFalse(timeBeforeConstruction.after(fileDirStringIntBooleanType.getCreationTime()));
		assertFalse(fileDirStringIntBooleanType.getCreationTime().after(timeAfterConstruction));
		assertEquals(fileDirStringIntBooleanType.getFileType(), FileType.TEXT);
		assertEquals(fileDirStringIntBooleanType.getParentDirectory(), basicDirectory);
	}

	@Test
	public void testFileStringIntBooleanType_IllegalName() {
		timeBeforeConstruction = new Date();
		fileDirStringIntBooleanType = new File(basicDirectory, illegalNameEmpty, 200, true, FileType.TEXT);
		timeAfterConstruction = new Date();
		assertTrue(fileDirStringIntBooleanType.isValidName(fileDirStringIntBooleanType.getName()));
		assertTrue(fileDirStringIntBooleanType.isWritable());
		assertNull(fileDirStringIntBooleanType.getModificationTime());
		assertFalse(timeBeforeConstruction.after(fileDirStringIntBooleanType.getCreationTime()));
		assertFalse(fileDirStringIntBooleanType.getCreationTime().after(timeAfterConstruction));
		assertEquals(fileDirStringIntBooleanType.getFileType(), FileType.TEXT);
		assertEquals(fileDirStringIntBooleanType.getParentDirectory(), basicDirectory);
	}

	@Test
	public void testFileStringIntBooleanType_IllegalParentDirectory() {
		assertThrows(IllegalParentDirectoryException.class, () -> {
			File file = new File(null, legalNameLetters, 200, true, FileType.TEXT);
		});
		// name already in directory
		assertThrows(IllegalParentDirectoryException.class, ()->{
			File file = new File(basicDirectory, legalNameLetters, 200, true, FileType.TEXT);
		});
	}

	@Test
	public void testFileStringIntBooleanType_IllegalType() {
		assertThrows(IllegalArgumentException.class, () -> {
			File file = new File(basicDirectory, "unusedName", 50, false, null);
		});
	}

	@Test
	public void testFileDirStringType_LegalCase() {
		assertEquals(legalNameSymbols,fileDirStringType.getName());
		assertEquals(0,fileDirStringType.getSize());
		assertTrue(fileDirStringType.isWritable());
		assertNull(fileDirStringType.getModificationTime());
		assertFalse(timeBeforeConstruction.after(fileDirStringType.getCreationTime()));
		assertFalse(fileDirStringType.getCreationTime().after(timeAfterConstruction));
		assertEquals(fileDirStringType.getFileType(), FileType.TEXT);
		assertEquals(fileDirStringType.getParentDirectory(), basicDirectory);
	}

	@Test
	public void testFileDirStringType_IllegalName() {
		timeBeforeConstruction = new Date();
		fileDirStringType = new File(basicDirectory, illegalNameEmpty, FileType.TEXT);
		timeAfterConstruction = new Date();
		assertEquals(0, fileDirStringType.getSize());
		assertTrue(fileDirStringType.isValidName(fileDirStringType.getName()));
		assertTrue(fileDirStringType.isWritable());
		assertNull(fileDirStringType.getModificationTime());
		assertFalse(timeBeforeConstruction.after(fileDirStringType.getCreationTime()));
		assertFalse(fileDirStringType.getCreationTime().after(timeAfterConstruction));
		assertEquals(fileDirStringType.getFileType(), FileType.TEXT);
		assertEquals(fileDirStringType.getParentDirectory(), basicDirectory);
	}

	@Test
	public void testFileDirStringType_IllegalParentDirectory() {
		assertThrows(IllegalParentDirectoryException.class, () -> {
			File file = new File(null, legalNameLetters, FileType.TEXT);
		});
		// name already in directory
		assertThrows(IllegalParentDirectoryException.class, ()->{
			File file = new File(basicDirectory, legalNameLetters, FileType.TEXT);
		});
	}

	@Test
	public void testFileDirStringType_IllegalType() {
		assertThrows(IllegalArgumentException.class, () -> {
			File file = new File(basicDirectory, "unusedName", null);
		});
	}

	@Test
	public void testIsValidName_LegalCase() {
		assertTrue(fileDirStringType.isValidName("abcDEF123-_."));
	}

	@Test
	public void testIsValidName_IllegalCase() {
		assertFalse(fileDirStringType.isValidName(null));
		assertFalse(fileDirStringType.isValidName(""));
		assertFalse(fileDirStringType.isValidName(illegalNameSymbols));
	}

	@Test
	public void testChangeName_LegalCase() {
		Date timeBeforeSetName = new Date();
		fileDirStringType.changeName("NewLegalName");
		Date timeAfterSetName = new Date();

		assertEquals("NewLegalName",fileDirStringType.getName());
		assertNotNull(fileDirStringType.getModificationTime());
		assertFalse(fileDirStringType.getModificationTime().before(timeBeforeSetName));
		assertFalse(timeAfterSetName.before(fileDirStringType.getModificationTime()));
	}

	@Test
	public void testChangeName_FileNotWritable() {
		NotWritableException thrown = assertThrows(NotWritableException.class, () -> {
			fileNotWritable.changeName("NewLegalName");
		});
	}

	@Test
	public void testChangeName_IllegalName() {
		fileDirStringType.changeName("$IllegalName$");
		assertEquals(legalNameSymbols,fileDirStringType.getName());
		assertNull(fileDirStringType.getModificationTime());
	}

	@Test
	public void testIsValidSize_LegalCase() {
		assertTrue(File.isValidSize(0));
		assertTrue(File.isValidSize(File.getMaximumSize()/2));
		assertTrue(File.isValidSize(File.getMaximumSize()));
	}

	@Test
	public void testIsValidSize_IllegalCase() {
		assertFalse(File.isValidSize(-1));
		if (File.getMaximumSize() < Integer.MAX_VALUE) {
			assertFalse(File.isValidSize(File.getMaximumSize()+1));
		}
	}

	@Test
	public void testEnlarge_LegalCase() {
		File file = new File(basicDirectory,"file",File.getMaximumSize()-1,true,FileType.TEXT);
		Date timeBeforeEnlarge = new Date();
		file.enlarge(1);
		Date timeAfterEnlarge = new Date();
		assertEquals(file.getSize(),File.getMaximumSize());
		assertNotNull(file.getModificationTime());
		assertFalse(file.getModificationTime().before(timeBeforeEnlarge));
		assertFalse(timeAfterEnlarge.before(file.getModificationTime()));
	}

	@Test
	public void testEnlarge_FileNotWritable() {
		NotWritableException thrown = assertThrows(NotWritableException.class, () -> {
			fileNotWritable.enlarge(1);
		});
	}

	@Test
	public void testShorten_LegalCase() {
		fileDirStringIntBooleanType.shorten(1);
		Date timeAfterShorten = new Date();
		assertEquals(fileDirStringIntBooleanType.getSize(),199);
		assertNotNull(fileDirStringIntBooleanType.getModificationTime());
		assertFalse(fileDirStringIntBooleanType.getModificationTime().before(timeAfterConstruction));
		assertFalse(timeAfterShorten.before(fileDirStringIntBooleanType.getModificationTime()));
	}

	@Test
	public void testShorten_FileNotWritable() {
		NotWritableException thrown = assertThrows(NotWritableException.class, () -> {
			fileNotWritable.shorten(1);
		});
	}

	@Test
	public void testIsValidCreationTime_LegalCase() {
		Date now = new Date();
		assertTrue(File.isValidCreationTime(now));
	}

	@Test
	public void testIsValidCreationTime_IllegalCase() {
		assertFalse(File.isValidCreationTime(null));
		Date inFuture = new Date(System.currentTimeMillis() + 1000*60*60);
		assertFalse(File.isValidCreationTime(inFuture));
	}

	@Test
	public void testcanHaveAsModificationTime_LegalCase() {
		assertTrue(fileDirStringType.canHaveAsModificationTime(null));
		assertTrue(fileDirStringType.canHaveAsModificationTime(new Date()));
	}

	@Test
	public void testcanHaveAsModificationTime_IllegalCase() {
		assertFalse(fileDirStringType.canHaveAsModificationTime(new Date(timeAfterConstruction.getTime() - 1000*60*60)));
		assertFalse(fileDirStringType.canHaveAsModificationTime(new Date(System.currentTimeMillis() + 1000*60*60)));
	}

	@Test
	public void testHasOverlappingUsePeriod_UnmodifiedFiles() {
		// one = implicit argument ; other = explicit argument
		File one = new File(basicDirectory, "one", FileType.TEXT);
		sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
		File other = new File(basicDirectory, "other", FileType.TEXT);

		//1 Test unmodified case
		assertFalse(one.hasOverlappingUsePeriod(other));

		//2 Test one unmodified case
		other.enlarge(File.getMaximumSize());
		assertFalse(one.hasOverlappingUsePeriod(other));

		//3 Test other unmodified case
		//so re-initialise the other file
		other = new File(anotherDirectory, "other", FileType.TEXT);
		one.enlarge(File.getMaximumSize());
		assertFalse(one.hasOverlappingUsePeriod(other));

	}

	@Test
	public void testHasOverlappingUsePeriod_ModifiedNoOverlap() {
		// one = implicit argument ; other = explicit argument
		File one, other;
		one = new File(basicDirectory, "one", FileType.TEXT);
		sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
		other = new File(basicDirectory, "other", FileType.TEXT);

		//1 Test one created and modified before other created and modified case
		one.enlarge(File.getMaximumSize());
		sleep();
		//re-initialise the other
		other = new File(anotherDirectory, "other", FileType.TEXT);
		other.enlarge(File.getMaximumSize());
		assertFalse(one.hasOverlappingUsePeriod(other));

		//2 Test other created and modified before one created and modified
		other.enlarge(File.getMaximumSize());
		sleep();
		one = new File(basicDirectory, "one2", FileType.TEXT);
		one.enlarge(File.getMaximumSize());
		assertFalse(one.hasOverlappingUsePeriod(other));

	}

	@Test
	public void testHasOverlappingUsePeriod_ModifiedOverlap_A() {
		// one = implicit argument ; other = explicit argument
		//A Test one created before other created before one modified before other modified
		File one, other;
		one = new File(basicDirectory, "one", FileType.TEXT);
		sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
		other = new File(basicDirectory, "other", FileType.TEXT);

		one.enlarge(File.getMaximumSize());
		sleep();
		other.enlarge(File.getMaximumSize());
		assertTrue(one.hasOverlappingUsePeriod(other));
	}

	@Test
	public void testHasOverlappingUsePeriod_ModifiedOverlap_B() {
		// one = implicit argument ; other = explicit argument
		//B Test one created before other created before other modified before one modified
		File one, other;
		one = new File(basicDirectory, "one", FileType.TEXT);
		sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
		other = new File(basicDirectory, "other", FileType.TEXT);

		other.enlarge(File.getMaximumSize());
		sleep();
		one.enlarge(File.getMaximumSize());
		assertTrue(one.hasOverlappingUsePeriod(other));
	}

	@Test
	public void testHasOverlappingUsePeriod_ModifiedOverlap_C() {
		// one = implicit argument ; other = explicit argument
		//C Test other created before one created before other modified before one modified
		File one, other;
		other = new File(basicDirectory, "other", FileType.TEXT);
		sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
		one = new File(basicDirectory, "one", FileType.TEXT);

		other.enlarge(File.getMaximumSize());
		sleep();
		one.enlarge(File.getMaximumSize());
		assertTrue(one.hasOverlappingUsePeriod(other));
	}

	@Test
	public void testHasOverlappingUsePeriod_ModifiedOverlap_D() {
		// one = implicit argument ; other = explicit argument
		//D Test other created before one created before one modified before other modified
		File one, other;
		other = new File(basicDirectory, "one", FileType.TEXT);
		sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
		one = new File(basicDirectory, "other", FileType.TEXT);

		one.enlarge(File.getMaximumSize());
		sleep();
		other.enlarge(File.getMaximumSize());
		assertTrue(one.hasOverlappingUsePeriod(other));
	}

	@Test
	public void testSetWritable() {
		fileDirStringIntBooleanType.setWritable(false);
		fileNotWritable.setWritable(true);
		assertFalse(fileDirStringIntBooleanType.isWritable());
		assertTrue(fileNotWritable.isWritable());
	}

	private void sleep() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
