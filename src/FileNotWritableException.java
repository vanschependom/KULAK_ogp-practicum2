import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal attempts to change a file.
 * 
 * @invar	The referenced file must be effective
 * 			| isValidFile(getFile())
 * @author 	Tommy Messelis
 * @version	2.3
 */
public class FileNotWritableException extends RuntimeException {

	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Variable referencing the file to which change was denied.
	 */
	private final File file;
	
	/**
	 * Check whether the fiven file is a valid file for this Exception.
	 * @param 	file
	 * 			The file to check
	 * @return	result == (file != null)
	 */
	public static boolean isValidFile(File file) {
		return file != null;
	}

	/**
	 * Initialize this new file not writable exception involving the
	 * given file.
	 * 
	 * @param	file
	 * 			The file for the new file not writable exception.
	 * @pre		The given file must be a valid file
	 * 			| isValidFile(file)
	 * @post	The file involved in the new file not writable exception
	 * 			is set to the given file.
	 * 			| new.getFile() == file
	 */
	public FileNotWritableException(File file) {
		this.file = file;
	}
	
	/**
	 * Return the file involved in this file not writable exception.
	 */
	@Basic @Immutable
	public File getFile() {
		return file;
	}
	
}