package filesystem;

/**
 * An enum class with filetypes, used for files.
 *
 * @author  Vincent Van Schependom
 * @author  Flor De Meulemeester
 * @author  Arne Claerhout
 * @version 1.0
 */
public enum FileType {

    TEXT("txt"), PDF("pdf"), JAVA("java");

    private String extension = null;

    private FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

}
