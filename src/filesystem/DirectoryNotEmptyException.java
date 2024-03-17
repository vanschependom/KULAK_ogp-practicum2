package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class for signaling directory not empty cases.
 *
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 1.0
 */
public class DirectoryNotEmptyException extends RuntimeException {

    /**
     * Required because this class inherits from Exception.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the file to which change was denied.
     */
    private final Directory directory;

    /**
     * Initialize this new directory not empty exception with the given directory.
     * @param   directory
     *          The directory for this new directory not empty exception.
     * @post    The directory of this new directory not empty exception is equal to the directory given.
     *          | new.getDirectory() == directory
     */
    public DirectoryNotEmptyException(Directory directory){
        this.directory = directory;
    }

    /**
     * Return the directory of this directory not empty exception.
     */
    @Basic @Immutable
    public Directory getDirectory(){
        return directory;
    }
}
