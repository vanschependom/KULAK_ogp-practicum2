package filesystem;

import be.kuleuven.cs.som.annotate.*;

import java.util.ArrayList;

/**
 * A class of directories, inheriting from the class Item, within a filesystem
 * @invar	Each directory must have a valid name.
 * 	        | isValidName(getName())
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
     * Destructors
     **********************************************************/

    /**
     * A destructor for this directory.
     *
     * @effect  The destructor for the superclass Item is called.
     *          | super.delete()
     * @throws  DirectoryNotEmptyException
     *          The directory is not empty.
     *          | getNbOfItems() != 0
     */
    @Override
    public void delete() throws DirectoryNotEmptyException {
        if (getNbOfItems() != 0) {
            throw new DirectoryNotEmptyException(this);
        }
        super.delete();
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
     * 			| result == ( (name != null)
     * 		    |	&& (name.matches("[a-zA-Z_0-9-]+")) )
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
     * @effect  The modification time is set to the current time
     *          | setModificationTime()
     * @post    The item is added to the directory
     *          | new.getNbItems() == getNbItems() + 1
     * @post    The item is at the correct index so that the directory
     *          is ordered.
     *          | new.hasProperItems()
     * @throws  NotWritableException
     *          The directory is not writable.
     *          | ! isWritable()
     * @throws  IllegalItemException
     *          The item is not a valid item to be added this directory.
     *          | ! isAddableItem(item)
     */
    public void addItem(Item item) throws
            NullPointerException, NotWritableException, IllegalItemException, IllegalArgumentException {
        if (!isWritable()) {
            throw new NotWritableException(this);
        }
        if (!isAddableItem(item)) {
            throw new IllegalItemException(item);
        }
        int index = getIndexForItem(item);
        insertItemAtIndex(index, item);
        setModificationTime();
        changeDiskUsageBy(item.getTotalDiskUsage());
    }

    /**
     * A method for checking the index of a certain item
     *
     * @return  The index of this item based on the lexigraphical order within the directory.
     *          | (items.getItemAt(result-1).getName().compareTo(item.getName()) < 0)
     *          |   && (items.getItemAt(result+1).getName().compareTo(item.getName()) > 0)
     * @throws  IllegalArgumentException
     *          The item is not in the directory
     *          | ! hasAsItem(item)
     */
    private int getIndexForItem(Item item) {
        if (!hasAsItem(item)) {
            throw new IllegalArgumentException("Item is not in directory.");
        }
        // get the index based on the lexigraphical order
        int index = 0;
        for (Item otherItem : items) {
            if (otherItem.getName().compareTo(item.getName()) < 0) {
                index++;
            }
        }
        return index;
    }

    /**
     * A method for inserting an item at a given index.
     *
     * @param   index
     *          The index at which the item should be inserted.
     * @param   item
     *          The item to be inserted.
     * @post    The item is inserted at the given index
     *          | items.getItemAt(index) == item
     * @throws  NotWritableException
     *          The directory is not writable
     *          | ! isWritable()
     * @throws  IllegalItemException
     *          The item is not a valid item to be added to this directory
     *          | ! isAddableItem(item)
     * @throws  IndexOutOfBoundsException
     *          The index is not a valid index for this directory
     *          | ! canHaveAsIndex(index)
     */
    private void insertItemAtIndex(int index, Item item) throws NotWritableException, IllegalItemException, IndexOutOfBoundsException {
        if(!isWritable()) {
            throw new NotWritableException(this);
        }
        if(!isAddableItem(item)) {
            throw new IllegalItemException(item);
        }
        if(!canHaveAsIndex(index)) {
            throw new IndexOutOfBoundsException();
        }
        items.add(index, item);
    }

    /**
     * A method for getting an item with a given name
     *
     * @return  The item with the given name is returned, if it is present in the directory.
     *          | result.getName().equals(name)
     * @throws  IllegalArgumentException
     *          The name is not valid.
     *          | ! isValidName(name)
     * @throws  IllegalArgumentException
     *          There is no item with the given name in the directory.
     *          | ! containsDiskItemWithNameCaseSensitive(name)
     */
    public Item getItem(String name) throws IllegalArgumentException {
        if (!isValidName(name)) throw new IllegalArgumentException("Name is not valid.");
        if (!containsDiskItemWithNameCaseSensitive(name)) {
            throw new IllegalArgumentException("No item with the given name in the directory.");
        }
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        throw new IllegalArgumentException("No item with the given name in the directory.");
    }

    /**
     * A method for getting an item with a given index
     *
     * @return  The item with a given index is returned.
     *          | result == items.get(index)
     * @throws  IndexOutOfBoundsException
     *          If the index is not between the given bounds.
     *          | ! canHaveAsIndex(index)
     */
    public Item getItemAt(int index) throws IndexOutOfBoundsException {
        if (!canHaveAsIndex(index)) {
            throw new IndexOutOfBoundsException("Index is not valid.");
        }
        return items.get(index);
    }

    /**
     * A method for getting the index of an item.
     *
     * @return  The index of the given item in item, if it is present in the directory.
     *          | result == items.indexOf(item)
     * @throws  NullPointerException
     *          The item is null.
     *          | item == null
     * @throws  IllegalArgumentException
     *          The item is not in the directory.
     *          | ! hasAsItem(item)
     */
    public int getIndexOf(Item item) throws NullPointerException, IllegalArgumentException {
        if (item == null) throw new NullPointerException("Item is null.");
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
    public boolean canHaveAsIndex(int index) {
        return index >= 0 && index < getNbOfItems();
    }

    /**
     * A method for checking if an item is present in a directory
     *
     * @return  True if the item is present, false otherwise
     *          | result == items.contains(item)
     * @throws  NullPointerException
     *          The item is null.
     *          | item == null
     */
    public boolean hasAsItem(Item item) {
        if (item == null) {
            throw new NullPointerException("Item is null.");
        }
        return items.contains(item);
    }

    /**
     * A method for checking if a directory contains a given item with a name,
     * regardless of the case of the letters.
     *
     * @return  True if a given name is present in items, false otherwise.
     *          Lowercase and uppercase letters are considered equal.
     *          | result == ( for some item in items:
     *          |   item.getName().equalsIgnoreCase(name) )
     * @throws  IllegalArgumentException
     *          The name is not valid.
     *          | ! isValidName(name)
     */
    public Boolean containsDiskItemWithName(String name) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name is not valid.");
        }
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A method for checking if a directory contains a given item with a name,
     * where the case of the letters matters.
     *
     * @return  True if a given name is present in items, false otherwise.
     *          Lowercase and uppercase letters are considered equal.
     *          | result == ( for some item in items:
     *          |   item.getName().equals(name) )
     * @throws  IllegalArgumentException
     *          The name is not valid.
     *          | ! isValidName(name)
     */
    public Boolean containsDiskItemWithNameCaseSensitive(String name) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name is not valid.");
        }
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A method for getting the number of items in a directory.
     */
    public int getNbOfItems() {
        return items.size();
    }

    /**
     * A method for checking if an item is valid in a directory
     *
     * @return  TODO
     */
    public boolean canHaveAsItem(Item item) {
        return (item != null)
                && item.getParentDirectory() == this;
    }

    /**
     * A method for checking if a given item can be added as an item within this directory.
     *
     * @param   item
     *          The item to check
     * @return  TODO
     */
    public boolean isAddableItem(Item item) {
        return (item != null)
                && !hasAsItem(item)
                && item.getParentDirectory() == null
                && !containsDiskItemWithNameCaseSensitive(item.getName())
                && item.canHaveAsParentDirectory(this);
    }

    /**
     * A method for checking if a directory has proper items.
     *
     * @return  True if every item in the directory is valid,
     *          has this directory as the parent directory
     *          and can have this directory as the parent directory.
     *          Still true if items is ordered, false otherwise.
     *          | result == ( for each item in items:
     *          |               canHaveAsItem(item) )
     *          |           && ( isOrdered() )
     */
    public boolean hasProperItems() {
        for (Item item : items) {
            if ( !canHaveAsItem(item) ) {
                return false;
            }
        }
        return isOrdered();
    }

    /**
     * A method for checking if a directory is ordered lexicographically.
     *
     * @return  True if the items in the directory are ordered lexicographically.
     *         | result == ( for each item in items:
     *         |               item.getName().compareTo(getItemAt(getIndexOf(item)+1).getName()) < 0 )
     *         |           && ( for each item in items:
     *         |               item.getName().compareTo(getItemAt(getIndexOf(item)-1).getName()) > 0 )
     */
    public boolean isOrdered() {
        for (Item item : items) {
            if (item.getName().compareTo(getItemAt(getIndexOf(item)+1).getName()) < 0) {
                return false;
            }
        }
        for (Item item : items) {
            if (item.getName().compareTo(getItemAt(getIndexOf(item)-1).getName()) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * A method for removing an item from a directory.
     *
     * @param   item
     *          The item to be removed.
     * @throws  IllegalItemException
     *          The item is not valid.
     *          | ! hasAsItem(item)
     */
    protected void removeAsItem(Item item) throws NullPointerException, IllegalItemException {
        if (!hasAsItem(item)) {
            throw new IllegalItemException(item);
        }
        items.remove(item);
    }



    /**********************************************************
     * parent directory - defensive programming
     **********************************************************/

    /**
     * A method for making a directory a root directory
     *
     * @effect  The parent directory is set to null
     *          | setParentDirectory(null)
     * @effect  The modification time is set to the current time
     *          | setModificationTime()
     */
    public void makeRoot() {
        setParentDirectory(null);
        setModificationTime();
    }

    /**
     * Check whether this directory can have the given directory as its
     * parent directory.
     *
     * @param   dir
     *          The directory to check.
     * @return  True if the directory can be the parent directory
     *          of an item and if no loops wil be created.
     *          | result == ( super.canHaveAsParentDirectory(dir)
     *          |  && !isDirectOrIndirectChildOf(dir) )
     */
    @Override
    public boolean canHaveAsParentDirectory(Directory dir) {
        return ( super.canHaveAsParentDirectory(dir) )
                // check for loops
                && ( !isDirectOrIndirectChildOf(dir) );
    }



    /**********************************************************
     * disk usage - nominal programming
     **********************************************************/

    /**
     * @note variable diskUsage and getter getDiskUsage() are inherited from Item.
     */

    /**
     * A method for incrementing or decrementing the disk usage of this directory.
     *
     * @param   delta
     *          The number of bytes to change the disk usage by.
     *          This can be a positive or negative number.
     * @effect  The disk usage is set to the current disk usage, increased with
     *          the given delta (positive or negative).
     *          | setDiskUsage(getDiskUsage() + delta);
     */
    private void changeDiskUsageBy(int delta) {
        setDiskUsage(getTotalDiskUsage() + delta);
    }

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
