package filesystem;

import be.kuleuven.cs.som.annotate.*;

import java.util.Date;
import java.lang.String;

/**
 * A class of items within a filesystem.
 *
 * @invar   Each item must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar   Each item must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each item must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 * @invar   Each item must have a valid parent directory.
 *          | hasProperParentDirectory();
 * @invar   Each item must have a valid disk usage.
 *          | isValidDiskUsage(getTotalDiskUsage());
 *
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 2.0
 */
public abstract class Item {

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new item with given name and parentDirectory.
     * @param   name
     *          The name of the new item.
     * @param   dir
     *          The parent directory of the new item.
     * @effect  The name of the item is set to the given name.
     * 			If the given name is not valid, a default name is set.
     *          | setName(name)
     * @effect  The item is moved to the given directory.
     *          | move(dir)
     * @effect  The disk usage of the item is set to 0.
     *          | setDiskUsage(0)
     * @post    The new creation time of this item is initialized to some time during
     *          constructor execution.
     *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
     *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
     * @post    The new item has no time of last modification.
     *          | new.getModificationTime() == null
     * @throws  IllegalParentDirectoryException
     *          The directory is not legal for this item
     *          | ( ( ! (this instanceof Directory) && dir == null) || ! isAddableToDirectory(dir))
     */
    @Raw
    public Item(String name, Directory dir) throws IllegalParentDirectoryException, NullPointerException {
        setName(name);
        if (dir != null) {
            move(dir);
        } else if (!(this instanceof Directory)) {
            throw new IllegalParentDirectoryException(null);
        }
    }



    /**********************************************************
     * Destructors
     **********************************************************/

    /**
     * A variable to check whether the item is deleted or not.
     */
    protected boolean isDeleted = false;

    /**
     * A destructor for this item.
     *
     * @effect  If this item is not yet deleted and
     *          the parent directory is effective, this item is removed
     *          from that parent directory and the parent directory of this
     *          item is set to null.
     *          | if (getParentDirectory() != null) then
     *          |   getParentDirectory().removeAsItem(this)
     *          |   && setParentDirectory(null)
     * @post    If this item is not yet deleted, isDeleted
     *          is set to true.
     *          | new.isDeleted() == true
     */
    public void delete(){
        if (!isDeleted()) {
            isDeleted = true;
            if (getParentDirectory() != null) {
                getParentDirectory().removeAsItem(this);
                setParentDirectory(null);
            }
        }
    }

    /**
     * A method for checking if this item is deleted.
     */
    @Basic
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * A recursive method for deleting an item recursively.
     *
     * @post    The item is deleted
     *          | delete()
     * @throws  NotWritableException
     *          When the directory is not recursively deletable
     *          because a file or directory within is not writable
     *          | ! isRecursivelyDeletable()
     */
    public void deleteRecursive() throws NotWritableException {
        // if it is a directory, the overridden method will be used
        delete(); // if it is a different item we can just delete it normally
    }

    /**********************************************************
     * name - total programming
     **********************************************************/

    /**
     * Variable referencing the name of this item.
     */
    private String name = null;

    /**
     * Variable for keeping track of the default name index.
     */
    private static int nameIndex = 1;

    /**
     * Return the name of this item.
     */
    @Raw @Basic
    public String getName() {
        return name;
    }

    /**
     * Check whether the given name is a legal name for an item.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits, dots,
     * 			hyphens and underscores; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[a-zA-Z_0-9.-]+")
     */
    @Raw
    public boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z_0-9.-]+"));
    }

    /**
     * Set the name of this item to the given name.
     *
     * @param   name
     * 			The new name for this item.
     * @post    If the given name is valid, the name of
     *          this item is set to the given name,
     *          otherwise the name of the item is set to a valid name (the default).
     *          | if (isValidName(name))
     *          |      then new.getName().equals(name)
     *          |      else new.getName().equals(getDefaultName())
     */
    @Raw @Model
    private void setName(String name) {
        if (isValidName(name)) {
            this.name = name;
        } else {
            this.name = getDefaultName();
        }
    }

    /**
     * Return the name for a new item which is to be used when the
     * given name is not valid.
     *
     * @return   A valid item name.
     *          | isValidName(result)
     */
    @Raw @Model
    private static String getDefaultName() {
        return "new_item_" + nameIndex++;
    }

    /**
     * Change the name of this item to the given name.
     *
     * @param	name
     * 			The new name for this item.
     * @effect  The name of this item is set to the given name,
     * 			if this is a valid name and the item is writable,
     * 			otherwise there is no change.
     * 			| if (isValidName(name) && isWritable())
     *          | then setName(name)
     * @effect  If the name is valid, the modification time
     * 			of this item is updated.
     *          | if (isValidName(name))
     *          | then setModificationTime()
     */
    public void changeName(String name) {
        if (this instanceof Directory && !((Directory) this).isWritable()) throw new NotWritableException(this);
        if (this instanceof File && !((File) this).isWritable()) throw new NotWritableException(this);
        if (isValidName(name)){
            setName(name);
            setModificationTime();
        }
    }



    /**********************************************************
     * creationTime - total programming
     **********************************************************/

    /**
     * Variable referencing the time of creation.
     */
    private final Date creationTime = new Date();

    /**
     * Return the time at which this item was created.
     */
    @Raw @Basic @Immutable
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Check whether the given date is a valid creation time.
     *
     * @param  	date
     *         	The date to check.
     * @return 	True if and only if the given date is effective and not
     * 			in the future.
     *         	| result ==
     *         	| 	(date != null) &&
     *         	| 	(date.getTime() <= System.currentTimeMillis())
     */
    public static boolean isValidCreationTime(Date date) {
        return 	(date != null) &&
                (date.getTime() <= System.currentTimeMillis());
    }



    /**********************************************************
     * modificationTime - total programming
     **********************************************************/

    /**
     * Variable referencing the time of the last modification,
     * possibly null.
     */
    private Date modificationTime = null;

    /**
     * Return the time at which this item was last modified.
     * If this item has not yet been modified after construction,
     * null is returned.
     */
    @Raw @Basic
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Check whether this item can have the given date as modification time.
     *
     * @param	date
     * 			The date to check.
     * @return 	True if and only if the given date is either not effective
     * 			or if the given date lies between the creation time and the
     * 			current time.
     *         | result == (date == null) ||
     *         | ( (date.getTime() >= getCreationTime().getTime()) &&
     *         |   (date.getTime() <= System.currentTimeMillis())     )
     */
    @Raw
    public boolean canHaveAsModificationTime(Date date) {
        return (date == null) ||
                ( (date.getTime() >= getCreationTime().getTime()) &&
                        (date.getTime() <= System.currentTimeMillis()) );
    }

    /**
     * Set the modification time of this item to the current time.
     *
     * @post   The new modification time is effective.
     *         | new.getModificationTime() != null
     * @post   The new modification time lies between the system
     *         time at the beginning of this method execution and
     *         the system time at the end of method execution.
     *         | (new.getModificationTime().getTime() >=
     *         |                    System.currentTimeMillis()) &&
     *         | (new.getModificationTime().getTime() <=
     *         |                    (new System).currentTimeMillis())
     */
    @Model
    protected void setModificationTime() {
        modificationTime = new Date();
    }

    /**
     * Return whether this item and the given other item have an
     * overlapping use period.
     *
     * @param 	other
     *        	The other item to compare with.
     * @return 	False if the other item is not effective
     * 			False if the prime object does not have a modification time
     * 			False if the other item is effective, but does not have a modification time
     * 			otherwise, true if and only if the open time intervals of this item and
     * 			the other item overlap
     *        	| if (other == null) then result == false else
     *        	| if ((getModificationTime() == null)||
     *        	|       other.getModificationTime() == null)
     *        	|    then result == false
     *        	|    else
     *        	| result ==
     *        	| ! (getCreationTime().before(other.getCreationTime()) &&
     *        	|	 getModificationTime().before(other.getCreationTime()) ) &&
     *        	| ! (other.getCreationTime().before(getCreationTime()) &&
     *        	|	 other.getModificationTime().before(getCreationTime()) )
     */
    public boolean hasOverlappingUsePeriod(Item other) {
        if (other == null) return false;
        if(getModificationTime() == null || other.getModificationTime() == null) return false;
        return ! (getCreationTime().before(other.getCreationTime()) &&
                getModificationTime().before(other.getCreationTime()) ) &&
                ! (other.getCreationTime().before(getCreationTime()) &&
                        other.getModificationTime().before(getCreationTime()) );
    }



    /**********************************************************
     * parent directory - defensive programming
     **********************************************************/

    /**
     * A variable referencing the parent directory of this item.
     */
    private Directory parentDirectory = null;

    /**
     * Return the parent directory of this item, possibly null.
     */
    @Basic
    public Directory getParentDirectory() {
        return parentDirectory;
    }

    /**
     * A method for checking if this item is addable to a given directory.
     *
     * @return  True if the file is not already in the directory,
     *          the dir doesn't already contain an item with this items name
     *          and this item can have the given dir as its parent dir.
     *          | result == ( !dir.hasAsItem(this)
     *          |               && this.canHaveAsParentDirectory(dir) )
     */
    public boolean isAddableToDirectory(Directory dir) {
        return this.canHaveAsParentDirectory(dir)
                && !dir.hasAsItem(this);
    }

    /**
     * Check whether this item can have the given directory as its
     * parent directory.
     *
     * @param   dir
     *          The directory to check.
     * @return  True if the directory is effective and the name of this item is not
     *          already taken in the directory.
     *          | result == (dir != null) && !dir.containsDiskItemWithNameCaseSensitive(getName())
     */
    @Raw
    public boolean canHaveAsParentDirectory(Directory dir) {
        return dir != null && !dir.containsDiskItemWithNameCaseSensitive(getName());
	}

    /**
     * Set the parent directory of this item to the given directory <dir>.
     *
     * @param   dir
     *          The directory we want the parent directory to be set to.
     * @post    The parent directory is set to the given directory.
     *          | parentDirectory = dir
     */
    @Model @Raw
    protected void setParentDirectory(Directory dir) {
        parentDirectory = dir;
    }

    /**
     * A checker for the class invariant stating that the parent directory
     * must be legal at all times.
     *
     * @return  True if and only if the parent directory is a legal parent directory.
     *          | result == canHaveAsParentDirectory(getParentDirectory())
     */
    @Raw
    public boolean hasProperParentDirectory() {
        return canHaveAsParentDirectory(getParentDirectory());
    }

    /**
     * A recursive method for checking if this item is a direct or indirect
     * child of the given directory.
     *
     * @param   dir
     *          The directory to check.
     * @return  True if the given directory is a direct or indirect parent of this item.
     *          | result == (dir == getParentDirectory()) && ( dir != null || getParentDirectory() == null )
     *          |   || ( getParentDirectory().isDirectOrIndirectChildOf(dir) )
     */
    public boolean isDirectOrIndirectChildOf(Directory dir) {
        if (dir == getParentDirectory()) return true;
        if (dir == null || getParentDirectory() == null) return false;
        return getParentDirectory().isDirectOrIndirectChildOf(dir);
    }



    /**********************************************************
     * disk usage - nominal programming
     **********************************************************/

    /**
     * A variable referencing the disk usage of this item.
     */
    private int diskUsage = 0;

    /**
     * Return the disk usage of this item.
     */
    @Basic
    public int getTotalDiskUsage() {
        return diskUsage;
    }

    /**
     * Check if the given amount of bits is a legal diskUsage amount.
     *
     * @param   amtOfBits
     *          The amount of bits to check.
     * @return  True if the given amount of bits is positive or equal to zero.
     *          | result == (amtOfBits >= 0)
     */
    public static boolean isValidDiskUsage(int amtOfBits) {
        return (amtOfBits >= 0);
    }

    /**
     * Set the disk usage of this item.
     *
     * @param   diskUsage
     *          The number to set the disk usage to.
     * @pre     The diskUsage to be set must be valid.
     *          | isValidDiskUsage()
     * @post    The diskUsage of this item is set to the given amount of bits.
     *
     * @note    We implemented this nominally, so we expect a legal value.
     */
    @Raw
    protected void setDiskUsage(int diskUsage) {
        this.diskUsage = diskUsage;
    }


    /**********************************************************
     * hierarchy - defensive programming
     **********************************************************/

    /**
     * A method for moving this item.
     *
     * @param   dir
     *          The directory to move the item to.
     * @effect  If the given directory is a valid parent directory and
     *          the current parent directory is effective, the item is removed
     *          from the current parent directory.
     *          | if (isAddableToDirectory(dir) && getParentDirectory() != null)
     *          |   then getParentDirectory().removeAsItem(this)
     * @effect  If the given directory is a valid parent directory, the item is added
     *          to the given directory.
     *          | if (isAddableToDirectory(dir))
     *          |   then dir.addItem(this)
     * @effect  If the given directory is a valid parent directory, the parent directory
     *          of this item is set to the given directory.
     *          | if (isAddableToDirectory(dir))
     *          |   then setParentDirectory(dir)
     * @throws  IllegalParentDirectoryException
     *          The given directory is not a valid parent directory.
     *          | ! isAddableToDirectory(dir)
     * @throws  NullPointerException
     *          The given directory is null
     *          | dir == null
     */
    @Raw
    public void move(Directory dir) throws IllegalParentDirectoryException, NullPointerException {
        if (!isAddableToDirectory(dir)) {
            throw new IllegalParentDirectoryException(dir);
        }
        if (getParentDirectory() != null) {
            getParentDirectory().removeAsItem(this);
        }
        setParentDirectory(dir);
        dir.addItem(this);
    }

    /**
     * A recursive method to find the root directory in which the item is located.
     *
     * @return  The root directory of the item
     *          | if ( getParentDirectory() == null ) then
     *          |   result == this
     *          | else
     *          |   result == getParentDirectory().getRoot()
     */
    public Directory getRoot() {
        // if the parent directory is null, the item is definitely a directory
        if ((getParentDirectory() == null)) {
            return (Directory) this;
        } else {
            return getParentDirectory().getRoot();
        }
    }

    /**
     * A method to return a string with the complete path to the item.
     *
     * @return A string with the complete path to the item, starting with a slash
     *         followed by the root directory, then a slash with the next directory, ...,
     *         and ending with a slash and the name of the item.
     *         | result == "/" + getAbsolutePathRecursive()
     */
    public String getAbsolutePath() {
        return "/" + getAbsolutePathRecursive();
    }

    /**
     * A recursive help function for getAbsolutePath().
     *
     * @return  A part of the absolute path.
     *          | if isRoot() then result == getName()
     *          | else result == getParentDirectory().getAbsolutePathRecursive() + "/" + getName()
     */
    protected String getAbsolutePathRecursive() {
        if (getParentDirectory() == null) {
            return getName();
        }
        return getParentDirectory().getAbsolutePathRecursive() + "/" + getName();
    }

}