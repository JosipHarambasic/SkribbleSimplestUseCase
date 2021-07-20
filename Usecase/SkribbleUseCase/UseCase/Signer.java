public class Signer {
    String sid;
    String signer_email_address;
    SignerIdentityData signer_identity_data;
    int order;
    String status_code;
    boolean notify;

    public Signer(String sid, String signer_email_address,SignerIdentityData signer_identity_data, int order, String status_code, boolean notify){
        this.sid = sid;
        this.signer_email_address = signer_email_address;
        this.signer_identity_data = signer_identity_data;
        this.order = order;
        this.status_code = status_code;
        this.notify = notify;
    }
}
