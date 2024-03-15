package filesystem;

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
