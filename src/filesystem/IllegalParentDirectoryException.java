package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal attempts to use an invalid parent directory.
 *
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 1.0
 */
public class IllegalParentDirectoryException extends RuntimeException {

    /**
     * Required because this class inherits from Exception.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the file to which change was denied.
     */
    private final Directory directory;

    /**
     * Initialize this new illegal parent directory exception with the given directory.
     * @param   directory
     *          The directory for this new illegal parent directory exception.
     * @post    The directory of this new illegal parent directory exception is equal to the directory given.
     *          | new.getDirectory() = directory
     * @effect  This new illegal parent directory exception is further
     *          initialized as a new runtime exception involving no
     *          diagnostic message and no cause.
     *          | super()
     */
    public IllegalParentDirectoryException(Directory directory){
        this.directory = directory;
    }

    /**
     * Return the directory of this illegal parent directory exception.
     */
    @Basic @Immutable
    public Directory getDirectory(){
        return directory;
    }
}
