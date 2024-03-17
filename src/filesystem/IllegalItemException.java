package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal attempts to link with an invalid item.
 *
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 1.0
 */
public class IllegalItemException extends RuntimeException {

    /**
     * Required because this class inherits from Exception.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the file to which change was denied.
     */
    private final Item item;

    /**
     * Initialize this new illegal item exception with the given item.
     * @param   item
     *          The item for this new illegal item exception.
     * @post    The item of this new illegal item exception is equal to the item given.
     *          | new.getItem() = item
     * @effect  This new illegal amount exception is further
     *          initialized as a new runtime exception involving no
     *          diagnostic message and no cause.
     *          | super()
     */
    public IllegalItemException(Item item){
        this.item = item;
    }

    /**
     * Return the item of this illegal item exception.
     */
    @Basic @Immutable
    public Item getItem(){
        return item;
    }

}
