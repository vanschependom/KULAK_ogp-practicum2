package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal attempts to change a file or folder.
 *
 * @invar	The referenced file or folder must be effective
 * 			| isValidItem(getItem())
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 2.0
 */
public class NotWritableException extends RuntimeException {

    /**
     * Required because this class inherits from Exception.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the file to which change was denied.
     */
    private final Item item;

    /**
     * Check whether the given item is a valid item for this Exception.
     * @param 	item
     * 			The item to check
     * @return	| result == (item != null)
     *          |   && ( (item instanceof File)
     *          |       || (item instanceof Folder) )
     */
    public static boolean isValidItem(Item item) {
        return (item != null) &&
                ((item instanceof File) ||
                        (item instanceof Directory));
    }

    /**
     * Initialize this new item not writable exception involving the
     * given folder or file.
     *
     * @param	item
     * 			The folder or file for the new not writable exception.
     * @pre		The given item must be a valid item
     * 			| isValidItem(item)
     * @post	The item involved in the new not writable exception
     * 			is set to the given item.
     * 			| new.getItem() == item
     * @effect  This new illegal amount exception is further
     *          initialized as a new runtime exception involving no
     *          diagnostic message and no cause.
     *          | super()
     */
    public NotWritableException(Item item) {
        this.item = item;
    }

    /**
     * Return the item involved in this file not writable exception.
     */
    @Basic @Immutable
    public Item getItem() {
        return item;
    }

}