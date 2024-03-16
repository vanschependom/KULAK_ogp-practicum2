package filesystem;

import be.kuleuven.cs.som.annotate.*;

import java.util.ArrayList;

/**
 * A class of directories, inheriting from the class Item, within a filesystem
 * @invar	Each directory must have a valid name.
 * 			| isValidName(getName())
 * @invar   Each directory must have valid contents.
 *          | hasProperItems()
 * @invar   The items in this directory are ordered at all times.
 *          | isOrdered(items)
 * @invar   The names of the items in a directory must always be unique.
 *          | for each item in Item:
 *          |       for each otherItem in Item:
 *          |           if (item != otherItem)
 *          |               item.getName() != otherItem.getName()
 * @invar   The disk-usage of a directory is always valid.
 *          | canHaveAsDiskUsage(getDiskUsage)
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 2.0
 */
public class Directory extends Item {

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new directory with given parent directory, name and writability.
     *
     * @param   dir
     *          The parent directory for this directory
     * @param   name
     *          The name for this directory
     * @param   writable
     *          The writability of this directory
     * @effect  A new item is initialized with the given name and parent directory.
     *          | super(name, dir)
     * @effect	The writability is set to the given boolean
     * 			| setWritable(writable)
     * @post    The new directory does not have any contents.
     *          | new.getNbItems() == 0
     */
    @Raw
    public Directory(Directory dir, String name, boolean writable) {
        super(name, dir);
        setWritable(writable);
    }

    /**
     * Initialize a new directory with given parent directory and name.
     *
     * @param   dir
     *          The parent directory for this directory
     * @param   name
     *          The name for this directory
     * @effect  A directory is initialized with the given name and parent directory,
     *          the writability is set to true.
     *          | this(dir, name, true)
     */
    @Raw
    public Directory(Directory dir, String name) {
        this(dir, name, true);
    }

    /**
     * Initialize a new directory with given name and writability.
     *
     * @param   name
     *          The name for this directory
     * @param   writable
     *          The writability of this directory
     * @effect  A directory is initialized with the given name and writability.
     *          | this(null, name, true)
     * @effect  The directory is set as a root directory, i.e. the directory
     *          doesn't have a parent directory.
     *          | makeRoot()
     *          |   && this.getParentDirectory() == null
     */
    @Raw
    public Directory(String name, boolean writable) {
        this(null, name, writable);
        this.makeRoot();
    }

    /**
     * Initialize a new directory with given name.
     *
     * @param   name
     *          The name for this directory
     * @effect  A directory is initialized with the given name, with no parent directory,
     *          and the writability is set to true.
     *          | this(null, name, true)
     */
    @Raw
    public Directory(String name) {
        this(null, name, true);
    }



    /**********************************************************
     * name - total programming
     **********************************************************/

    /**
     * Check whether the given name is a legal name for a directory.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits,
     * 			hyphens and underscores; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[a-zA-Z_0-9-]+")
     * @note    The name of directories cannot contain dots, as opposed to files.
     */
    @Override
    public boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z_0-9-]+"));
    }



    /**********************************************************
     * items in this directory - defensive programming
     **********************************************************/

    /**
     * A variable referencing the items within this directory.
     */
    private ArrayList<Item> items;

    /**
     * A method for adding an item to a directory
     *
     * @pre     The item must be valid for this directory
     *          | isValidItem(item)
     * @pre     The directory is writable
     *          | isWritable()
     * @effect  The modification time is set to the current time
     *          | setModificationTime()
     * @post    The item is added to the directory
     *          | getNbItems() == new.getNbItems() - 1
     * @post    The item is at the correct index so that the directory
     *          is ordered
     *          | hasProperItems()
     * @throws  NotWritableException
     *          TODO
     */
    public void addItem(Item item) throws NotWritableException {
        // 1. check if valid:
        // TODO
        // 2. get index for the (valid) item, based on its name:
        int index = getIndexForItem(item);
        // 3. insert item at the right index so that the ordering is kept:
        insertItemAtIndex(index, item);
        // NOTE: no order() method needed this way (much more overhead)
    }

    /**
     * A method for checking the index of a certain item
     *
     * @pre     The item must be in the directory
     *          | hasAsItem(item)
     * @return  The index of this item
     *          | result == indexOf(item)
     */
    private int getIndexForItem(Item item) {
        // TODO
    }

    /**
     * A method for inserting an item at a given index
     *TODO
     * @pre     The item must be valid in this directory
     *          | isValidItem(item)
     * @pre     The index must be valid for this item
     * //TODO
     *          | new.isOrdered()
     *
     * @pre     The directory is writable
     *          | isWritable()
     * @post    The item is inserted at the given index
     *          | items.add(index, item)
     *          TODO
     */
    private void insertItemAtIndex(int index, Item item) throws NotWritableException, IllegalItemException, IllegalIndexException {
        if(!isWritable()) throw new NotWritableException(this);
        if(!isValidItem(item)) throw new IllegalItemException(item);
        if(!isValidIndex(index, item)) throw new IllegalIndexException();
        items.add(index, item);
    }

    /**
     * A method for getting an item with a given name
     *
     * @return  The item with the given name.
     *          | result.getName().equals(name)
     *          TODO dit klopt denk ik niet
     * @throws  IllegalArgumentException
     *          There is no item with the given name in the directory.
     *          | ! containsDiskItemWithName(name)
     */
    public Item getItem(String name) throws IllegalArgumentException{
        // TODO
    }

    /**
     * A method for getting an item with a given index
     *
     * @pre     The given index must be a positive integer (including zero)
     *          smaller than the size of items.
     *          | index >= 0 && index < getNbOfItems()
     * @return  The item with a given index is returned.
     *          | result == items.get(index)
     * @throws  IndexOutOfBoundsException
     *          If the index is not between the given bounds.
     */
    public Item getItemAt(int index) throws IndexOutOfBoundsException {
        if (isValidIndex(index)) return items.get(index);
        throw new IndexOutOfBoundsException();
    }

    /**
     * A method for getting the index of an item.
     *
     * @pre     The item must be an item in the directory
     *          | hasAsItem(item)
     * @return  The index of the given item in items.
     *          | items.indexOf(item)
     * @throws  IllegalArgumentException
     *          The item is not in the directory.
     *          | ! hasAsItem(item)
     */
    public int getIndexOf(Item item) throws IllegalArgumentException {
        if (!hasAsItem(item)) {
            throw new IllegalArgumentException("Item is not in directory.");
        }
        return items.indexOf(item);
    }

    /**
     * A method for checking if an index is a valid index for
     * accessing an item in items.
     * @param   index
     *          The index to check.
     * @return  True if the index is a positive integer (including zero)
     *          and smaller than the size of items.
     *          | result == ( (index >= 0)
     *          |   && (index < getNbOfItems()) )
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < getNbOfItems();
    }

    /**
     * A method for checking if an item is present in a directory
     *
     * @return  True if the item is present, false otherwise
     *          | result == items.contains(item)
     */
    public boolean hasAsItem(Item item) {
        return items.contains(item);
    }

    /**
     * A method for checking if a directory contains a given item.
     *
     * @return  True if a given name is present in items, false otherwise
     */
    public Boolean containsDiskItemWithName(String name) {
        try {
            getItem(name);
        } catch (ItemNotInDirectoryException a) {
            return false;
        }
        return true;
    }

    /**
     * A method for checking the size of items
     *
     * @return  The size of items
     *          | items.size()
     */
    public int getNbOfItems() {
        return items.size();
    }

    /**
     * A method for checking if an item is valid in a directory
     *
     * @return  True if the item can have this directory as its parent
     *          directory, false otherwise
     *          | result == canHaveAsParentDirectory(this)
     */
    public boolean isValidItem(Item item) {
        return item.canHaveAsParentDirectory(this);
    }

    /**
     * A method for checking if a directory has proper items.
     *
     * @return  True if every item in the directory is valid,
     *          has this directory as the parent directory
     *          and can have this directory as the parent directory.
     *          Still true if items is ordered, false otherwise.
     *          | for each item in items:
     *          |   isValidItem(item) && item.getParentDirectory() == this &&
     *          |   item.canHaveAsParentDirectory(this)
     *          | isOrdered()
     *          TODO dit klopt niet helemaal
     */
    public boolean hasProperItems() {
        for (Item item : items) {
            if (!isValidItem(item) || item.getParentDirectory() != this || !item.canHaveAsParentDirectory(this))
                return false;
        }
        return isOrdered();
    }

    /**
     * A method for checking if a directory is ordered lexicographically.
     * TODO
     *          | for each otherItem with an index smaller than index:
     *          |       (item.getName()).compareTo(otherItem.getName()) > 0
     *          | for each otherItem with an index equal to or bigger than index:
     *          |       (item.getName()).compareTo(otherItem.getName()) < 0
     */
    public boolean isOrdered() {
        //TODO
        // ik dacht eerder aan zoiets
        for (int i = 1; i < getNbOfItems(); i++) {
            if ((items.get(i-1).getName()).compareTo(items.get(i).getName()) >= 0) return false;
        }
        return true;

        // zoiets?
        for (Item item : items) {
            if ( getItemAt((getIndexOf(item))) != item ) {
                return false;
            }
        }
        return true;
    }



    /**********************************************************
     * parent directory - defensive programming
     **********************************************************/

    /**
     * A method for making a directory a root directory
     *
     * @effect  The parent directory is set to null
     *          | setParentDirectory(null)
     */
    public void makeRoot() {
        setParentDirectory(null);
    }

    /**
     * Check whether this item can have the given directory as its
     * parent directory.
     *
     * @param   dir
     *          The directory to check.
     * @return  True if adding dir to this directory would not create a loop
     *          and if there is no other item in the parent directory
     *          with the same name as this directory
     *          | !dir.getAbsolutePath().contains(getName()) &&
     *          |   dir.containsDiskItemWithName(getName())
     */
    @Override
    public boolean canHaveAsParentDirectory(Directory dir) {
        return !dir.getAbsolutePath().contains(getName()) && dir.containsDiskItemWithName(getName());
    }



    /**********************************************************
     * disk usage - nominal programming
     **********************************************************/

    // TODO zorgen dat bij elke nieuwe file de diskUsage wordt aangepast

    /**
     * A method for checking if the disk usage of this directory is valid.
     * @param   nbOfBytes
     *          The amount of bytes (size) to check.
     * @return  True if the given number of bytes is positive and if this
     *          number is equal to the sum of all disk usages of every item
     *          in this directory.
     *          | result ==
     *          |    ( nbOfBytes >= 0 ) &&
     *          |    ( nbOfBytes == getSumOfDiskUsages() )
     */
    public boolean canHaveAsDiskUsage(int nbOfBytes) {
        return (nbOfBytes >= 0) && (nbOfBytes == getSumOfDiskUsages());
    }

    /**
     * A method for calculating the som of all disk usages of the items in this directory.
     * @return  The sum of all disk usages of the items in this directory.
     *          | TODO
     */
    @Model
    private int getSumOfDiskUsages() {
        int sum = 0;
        for (Item item : items) {
            sum += item.getTotalDiskUsage();
        }
        return sum;
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


}
