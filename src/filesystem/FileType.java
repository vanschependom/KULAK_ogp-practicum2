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

    TEXT, PDF, JAVA;

    public String getExtension() {
        switch (this) {
            case TEXT:
                return "txt";
            case PDF:
                return "pdf";
            case JAVA:
                return "java";
            default:
                return "";
        }
    }

}
