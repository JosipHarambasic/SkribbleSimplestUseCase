import java.util.List;

public class Document{

    /**
     * we need this class to covert the JSON response from the API request into an Object, so we can easily deal with all
     * these variables
     */

    String id;
    String title;
    String message;
    String document_id;
    String legislation;
    String quality;
    String signing_url;
    String status_overall;
    List<Signer> signatures;
    List<String> cc_email_addresses;
    String owner;
    List<String> read_access;
    List<String> write_access;
    String created_at;
    String updated_at;


    public Document(String id, String title, String message, String document_id, String legislation,
                    String quality, String signing_url, String status_overall, List<Signer> signatures, List<String> cc_email_addresses,
                    String owner, List<String> read_access, List<String> write_access, String created_at, String updated_at){
        this.id = id;
        this.title = title;
        this.message = message;
        this.document_id = document_id;
        this.legislation = legislation;
        this.quality = quality;
        this.signing_url = signing_url;
        this.status_overall = status_overall;
        this.signatures = signatures;
        this.cc_email_addresses = cc_email_addresses;
        this.owner = owner;
        this.read_access = read_access;
        this.write_access = write_access;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDocument_id() {
        return document_id;
    }

    public String getLegislation() {
        return legislation;
    }

    public String getQuality() {
        return quality;
    }

    public String getSigning_url() {
        return signing_url;
    }

    public String getStatus_overall() {
        return status_overall;
    }

    public List<Signer> getSignatures() {
        return signatures;
    }

    public List<String> getCc_email_addresses() {
        return cc_email_addresses;
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getRead_access() {
        return read_access;
    }

    public List<String> getWrite_access() {
        return write_access;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
