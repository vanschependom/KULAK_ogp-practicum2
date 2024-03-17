package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of files, inheriting from the class of items, within a filesystem.
 *
 * @invar	Each file must have a valid size.
 * 			| isValidSize(getSize())
 * @invar   Each file must have a valid filetype.
 *          | isValidFileType(getFileType())
 *
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 2.0
 */
public class File extends Item {

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new File with given parent directory, name,
     * size, writability and filetype.
     *
     * @param   dir
     *          The parent directory of the new file.
     * @param   name
     *          The name of the new file.
     * @param   size
     *          The size of the new file.
     * @param   writable
     *          The writability of the new file.
     * @param   type
     *          The type of the new file
     * @effect  The name of the file is set to the given name.
     * 			If the given name is not valid, a default name is set.
     *          | setName(name)
     * @effect	The size is set to the given size (must be valid)
     * 			| setSize(size)
     * @effect	The writability is set to the given flag
     * 			| setWritable(writable)
     * @effect  The parent directory is set to the given directory.
     *          | setParentDirectory(dir)
     * @post    The new creation time of this file is initialized to some time during
     *          constructor execution.
     *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
     *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
     * @post    The new file has no time of last modification.
     *          | new.getModificationTime() == null
     * @throws  IllegalParentDirectoryException
     *          The provided parent directory is not a valid parent directory.
     *          | ! canHaveAsParentDirectory(dir)
     * @throws  IllegalArgumentException
     *          The provided file type is not a valid type.
     *          | ! isValidFileType()
     */
    public File(Directory dir, String name, int size, boolean writable, FileType type) throws IllegalParentDirectoryException, IllegalArgumentException {
        super(name, dir); // This throws IllegalParentDirectoryException
        if (!isValidFileType(type)) {
            throw new IllegalArgumentException("This is not a valid file type.");
        }
        // final variable must be initialized this way
        fileType = type;
        setWritable(writable);
        setSize(size); // this also sets the disk usage
    }

    /**
     * Initialize a new file with a given parent directory, given name and given file type.
     *
     * @param   dir
     *          The parent directory of the new file.
     * @param   name
     *          The name of the new file.
     * @param   type
     *          The type of the new file.
     * @effect  This new file is initialized with the given parent directory, the given name,
     *          a size of 0 bytes, a writablity of true and the given file type.
     *          | this(dir, name, 0, true, type)
     */
    public File(Directory dir, String name, FileType type) throws IllegalParentDirectoryException, IllegalArgumentException {
        this(dir, name, 0, true, type);
    }



    /**********************************************************
     * Destructors
     **********************************************************/

    /**
     * TODO
     */
    @Override
    public void terminate(){
        super.terminate();
        // TODO
    }



    /**********************************************************
     * size - nominal programming
     **********************************************************/

    /**
     * Variable registering the size of this file (in bytes).
     */
    private int size = 0;

    /**
     * Variable registering the maximum size of any file (in bytes).
     */
    private static final int maximumSize = Integer.MAX_VALUE;


    /**
     * Return the size of this file (in bytes).
     */
    @Raw @Basic
    public int getSize() {
        return size;
    }

    /**
     * Set the size of this file to the given size.
     *
     * @param  size
     *         The new size for this file.
     * @pre    The given size must be legal.
     *         | isValidSize(size)
     * @effect The disk usage is set to the valid filesize.
     *         | setDiskUsage(size)
     * @post   The given size is registered as the size of this file.
     *         | new.getSize() == size
     */
    @Raw @Model
    private void setSize(int size) {
        this.size = size;
        setDiskUsage(size);
    }

    /**
     * Return the maximum file size.
     */
    @Basic @Immutable
    public static int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Check whether the given size is a valid size for a file.
     *
     * @param  size
     *         The size to check.
     * @return True if and only if the given size is positive and does not
     *         exceed the maximum size.
     *         | result == ((size >= 0) && (size <= getMaximumSize()))
     */
    public static boolean isValidSize(int size) {
        return ((size >= 0) && (size <= getMaximumSize()));
    }

    /**
     * Increases the size of this file with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this file
     *          must be increased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this file is increased with the given delta.
     *          | changeSize(delta)
     */
    public void enlarge(int delta) throws NotWritableException {
        changeSize(delta);
    }

    /**
     * Decreases the size of this file with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this file
     *          must be decreased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this file is decreased with the given delta.
     *          | changeSize(-delta)
     */
    public void shorten(int delta) throws NotWritableException {
        changeSize(-delta);
    }

    /**
     * Change the size of this file with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this file
     *         must be increased or decreased.
     * @pre    The given delta must not be 0
     *         | delta != 0
     * @effect The size of this file is adapted with the given delta.
     *         | setSize(getSize()+delta)
     * @effect The modification time is updated.
     *         | setModificationTime()
     * @throws NotWritableException(this)
     *         This file is not writable.
     *         | ! isWritable()
     */
    @Model
    private void changeSize(int delta) throws NotWritableException{
        if (isWritable()) {
            setSize(getSize()+delta);
            setModificationTime();
        }else{
            throw new NotWritableException(this);
        }
    }



    /**********************************************************
     * writable - defensive programming
     **********************************************************/

    /**
     * Variable registering whether this file is writable.
     */
    private boolean isWritable = true;

    /**
     * Check whether this file is writable.
     */
    @Basic
    public boolean isWritable() {
        return isWritable;
    }

    /**
     * Set the writability of this file to the given writability.
     *
     * @param isWritable
     *        The new writability
     * @post  The given writability is registered as the new writability
     *        for this file.
     *        | new.isWritable() == isWritable
     */
    @Raw
    public void setWritable(boolean isWritable) {
        this.isWritable = isWritable;
    }



    /**********************************************************
     * filetype
     **********************************************************/

    /**
     * Variable referencing the fileType of this file.
     * @note    This must be initialized in every constructor.
     */
    private final FileType fileType;

    /**
     * Return the fileType of this file.
     */
    @Basic @Immutable
    public FileType getFileType() {
        return fileType;
    }

    /**
     * Check whether the given filetype is a legal filetype for a file.
     *
     * @param  	type
     *			The type to be checked
     * @return	True if the given type is not a null reference.
     * 			| result ==
     * 			|	(type != null)
     */
    public static boolean isValidFileType(FileType type) {
        return (type != null);
    }


    /**********************************************************
     * name - total programming
     **********************************************************/

    /**
     * Change the name of this file to the given name.
     *
     * @param   name
     * 			The new name for this item.
     * @throws  NotWritableException
     *          This file is not writable.
     *          | ! isWritable()
     * @note    This needs to be overriden because the superclass doesn't
     *          have an implementation for writability.
     */
    @Override
    public void changeName(String name) throws NotWritableException {
        if (isWritable()) {
            super.changeName(name);
        } else {
            throw new NotWritableException(this);
        }
    }



    /**********************************************************
     * parent directory - defensive programming
     **********************************************************/

    /**
     * A method for checking if this file has a proper parent directory.
     *
     * @return  True if and only if the parent directory is not null
     *          and satisfies the hasProperParentDirectory() method
     *          of the superclass.
     *          | result == ( (getParentDirectory() != null)
     *          |               && super.hasProperParentDirectory() )
     */
    @Override
    public boolean hasProperParentDirectory() {
        return (getParentDirectory() != null)
            && (super.hasProperParentDirectory());
    }

}
