package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of links, inheriting from the class of items, within a filesystem.
 * @invar   The linked item must be valid.
 *          | isValidLinkedItem(linkedItem)
 */
public class Link extends Item{

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
     * @effect  The name of the link is set to the given name.
     * 			If the given name is not valid, a default name is set.
     *          | setName(name)
     * @effect  The parent directory is set to the given directory.
     *          | new.getParentDirectory() == dir
     */
    public Link(String name, Directory dir, Item linkedItem) {
        super(name, dir);
        setLinkedItem(linkedItem);
    }

    /**********************************************************
     * linkedItem - defensive?? programming
     * @note
     **********************************************************/

    private Item linkedItem;

    /**
     * Return the linked item.
     */
    @Basic
    public Item getLinkedItem() {
        return linkedItem;
    }

    /**
     * Sets the linked item to the given item.
     */
    @Basic @Raw
    private void setLinkedItem(Item linkedItem) {
        if (isValidLinkedItem(linkedItem)){
            this.linkedItem = linkedItem;
        }
        // TODO gooi expection (of anders implementeren?)
    }

    /**
     * A link must link to a file or a directory (and not another link).
     * So this is a checker to check if the linked item is not another link
     * and that the item isn't the null pointer.
     * @param   item
     *          The item that will be linked.
     * @return  False if the item is a link
     *          False if the item is null
     *          True if the item isn't a link
     *          | !(item instanceof Link) && item != null
     */
    public static boolean isValidLinkedItem(Item item){
        return !(item instanceof Link) && item != null;
    }

    /**
     * TODO
     */
    public boolean hasProperLinkedItem(){
        return isValidLinkedItem(getLinkedItem());
    }

    /**
     * TODO
     */
    public void unlink(){

    }

    /**********************************************************
     * Overrides
     **********************************************************/

    /**
     * TODO
     * @return
     */
    @Override
    public int getTotalDiskUsage(){
        return 0; // Link doesn't have a disk usage
    }

}
