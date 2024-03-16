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
 *          | isValidDiskUsage(getDiskUsage());
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
     * @effect  The parent directory is set to the given parent directory.
     *          If the given directory is not valid, an exception is thrown.
     *          | setParentDirectory(dir)
     *
     * TODO: exception voor illegal directory
     */
    @Raw
    public Item(String name, Directory dir) {
        setName(name);
        setParentDirectory(dir);
    }

    public Item(String name) {
        setName(name);
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
    @Model
    private static String getDefaultName() {
        return "new_item_" + Integer.toString(nameIndex++);
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
     * 			of this file is updated.
     *          | if (isValidName(name))
     *          | then setModificationTime()
     */
    public void changeName(String name) {
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
        return 	(date!=null) &&
                (date.getTime()<=System.currentTimeMillis());
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
     * If this file has not yet been modified after construction,
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
     * @note    This method is package private, because its children need
     *          to access it.
     */
    @Model
    void setModificationTime() {
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
     * Check whether this item can have the given directory as its
     * parent directory.
     *
     * @param   dir
     *          The directory to check.
     * @return  TODO
     */
    public boolean canHaveAsParentDirectory(Directory dir) {
        // TODO
    }

    /**
     * Set the parent directory of this item to the given directory <dir>.
     * @param   dir
     *          The directory we want the parent directory to be set to.
     * @post    TODO
     * @note    This is package private because children need to access it.
     *
     * TODO: throws [exception] en de condities voor die exception uitwerken
     */
    @Model
    void setParentDirectory(Directory dir) {
        if ( !canHaveAsParentDirectory(dir) ) {
            // throw exception
        } else {
            parentDirectory = dir;
        }
    }

    /**
     * A checker for the class invariant stating that the parent directory
     * must be legal at all times.
     *
     * @return  TODO
     */
    @Model
    private boolean hasProperParentDirectory() {
        return canHaveAsParentDirectory(getParentDirectory());
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
    public int getDiskUsage() {
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
     * @param   diskUsage
     *          The number to set the disk usage to.
     * @pre     The diskUsage to be set must be valid.
     *          | isValidDiskUsage()
     * @post    The diskUsage of this item is set to the given amount of bits.
     * @note    We implemented this nominally, so we expect a legal value.
     * @note    This is package private, since children must be able to call this method.
     */
    void setDiskUsage(int diskUsage) {
        this.diskUsage = diskUsage;
    }

    /**
     * Calculates the total disk usage of an item.
     * @return  A valid disk usage
     *          | isValidDiskUsage(result)
     */
    public int getTotalDiskUsage(){
        // TODO
        // Wordt best ge-override door directory, file and link
    }


    /**********************************************************
     * hierarchy - defensive programming
     **********************************************************/

    /**
     * TODO
     */
    public void move(Directory dir) {
        // TODO
    }

}

