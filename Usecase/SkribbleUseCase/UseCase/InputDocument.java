import java.util.List;

public class InputDocument {

    String title;
    String message;
    String content;
    List<DocumentSigner> signatures;

    public InputDocument(String title, String message, String content, List<DocumentSigner> signatures){
        this.title = title;
        this.message = message;
        this.content = content;
        this.signatures = signatures;
    }



}
