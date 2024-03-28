package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of links, inheriting from the class of items, within a filesystem.
 *
 * @invar   The linked item must be valid.
 *          | isValidLinkedItem(getLinkedItem())
 *
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 2.0
 */
public class Link extends Item {

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new Link with given parent directory, name and a linked Item.
     *
     * @param   name
     *          The name of the new link.
     * @param   dir
     *          The parent directory of the new link.
     * @param   linkedItem
     *          The item which the link is referring to.
     * @effect  A new item is initialized with the given name and directory.
     *          | super(name, dir)
     * @post    The linked item of this link is set to the given linked item.
     *          | new.getLinkedItem() == linkedItem
     */
    @Raw
    public Link(String name, Directory dir, Item linkedItem) throws IllegalItemException {
        super(name, dir);
        if (!isValidLinkedItem(linkedItem))
            throw new IllegalItemException(linkedItem);
        // set the final variable
        this.linkedItem = linkedItem;
    }


    /**********************************************************
     * linkedItem - defensive programming
     **********************************************************/

    /**
     * A variable referencing the item which the link is referring to.
     * @note    The link cannot be changed over time.
     *          It is thus marked as final.
     */
    private final Item linkedItem;

    /**
     * A method for getting the linked item of this link.
     */
    @Basic
    public Item getLinkedItem() {
        if (!isDeleted()){
            return linkedItem;
        } else {
            return null;
        }
    }

    /**
     * A link must link to a file or a directory (and not another link).
     * So this is a checker to check if the linked item is not another link
     * and that the item isn't the null pointer.
     *
     * @param   item
     *          The item that will be linked.
     * @return  False if the item is a link
     *          False if the item is null
     *          True if the item isn't a link
     *          | result ==
     *          |   ( !(item instanceof Link)
     *          |       && (item != null) )
     */
    @Raw
    public static boolean isValidLinkedItem(Item item) {
        return !(item instanceof Link)
                && item != null;
    }

    /**
     * Checks whether the link is linking to a legal item.
     */
    @Raw
    public boolean hasProperLinkedItem(){
        return isValidLinkedItem(getLinkedItem());
    }

}
