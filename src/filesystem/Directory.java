package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of directories, inheriting from the class Item, within a filesystem
 * @invar	Each directory must have a valid name.
 * 			| isValidName(getName())
 * @invar   Each directory must have valid contents.
 *          | for each item in Item:
 *          |       item.canHaveAsParent(this)
 * @invar   The contents of the variable items are always ordered.
 *          | isOrdered(items)
 * @invar   The names of the contents of a directory are always unique.
 *          | for each item in Item:
 *          |       for each otherItem in Item:
 *          |           if (item != otherItem)
 *          |               item.getName() != otherItem.getName()
 * @invar   The disk-usage of the contents of a directory are always valid.
 *          TODO: ik zou dit gewoon aanpassen naar 1 variabele diskusage die we altijd aanpassen per item dat wordt toegevoegd
 *          | for each item in Item:
 *          |       item.isValidDiskUsage(item.getDiskUsage())
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
     * @effect  The name of the directory is set to the given name.
     *          TODO: we kunnen dit weglaten en vervangen door super(...)
     * 			If the given name is not valid, a default name is set.
     *          | setName(name)
     * @effect	The writability is set to the given boolean
     * 			| setWritable(writable)
     * @post    The new creation time of this directory is initialized to some time during
     *          constructor execution.
     *          TODO: we kunnen dit weglaten en vervangen door super(...)
     *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
     *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
     * @post    The new directory has no time of last modification.
     *          TODO: we kunnen dit weglaten en vervangen door super(...)
     *          | new.getModificationTime() == null
     * @post    The new directory does not have any contents.
     *          | new.getNbItems() == 0
     */
    @Raw
    public Directory(Directory dir, String name, boolean writable) {

    }

    /**
     * Initialize a new directory with given parent directory and name.
     *
     * @param   dir
     *          The parent directory for this directory
     * @param   name
     *          The name for this directory
     * @effect  The parent directory is set to the given directory and the name is set to the given name,
     *          the writability is set to true
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
     * @effect  The directory is set as a root directory, the name is set to the given name,
     *          the writability is set to the given writability
     *          | this(null, name, true)
     *          | makeRoot()
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
     * @effect  The directory is set as a root directory, the name is set to the given name,
     *          the writability is set to the default value true
     *          | this(null, name, true)
     *          | makeRoot()
     */
    @Raw
    public Directory(String name) {
        this(null, name, true);
        this.makeRoot();
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
     */
    @Override
    public boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z_0-9-]+"));
    }





}
