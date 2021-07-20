import com.google.gson.Gson;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Main {

    public Main() {}
    public static void main(String[] args) throws IOException {

        /**first we need an base64 encoded pdf
         it's necessary, because we need the encoded string as input to send it*/
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file path of your document: ");
        String filepath = scanner.nextLine();

        /**we need to encode our pdf into base64 and pass it as content in the JSON request body*/
        String content = encodePDF(filepath);
        //"C:\\Users\\Desktop\\something.pdf"


        /** We can know create a list of all the users which have to receive the Document that has to be signed
         * here we add only one signer email, we can extend it later for more than one*/
        System.out.println("Add the email address from the signer: ");
        String emailAddress = scanner.nextLine();
        List<DocumentSigner> SignerList = new ArrayList<>();
        SignerList.add(new DocumentSigner(emailAddress));
        //SignerList.add(new DocumentSigner("something@gmail.com"));

        /** We can now make an InputDocument which contains all the information needed to make an API
         * we also need to convert the InputDocument to a JSON file, so the API call has a valid request Body*/
        System.out.println("Set the document title: ");
        String title = scanner.nextLine();
        System.out.println("Send a message to the signer: ");
        String message = scanner.nextLine();
        InputDocument inputDocument = new InputDocument(title, message, content, SignerList);
        Gson gson = new Gson();
        String documentAsJSON = gson.toJson(inputDocument);
        //System.out.println(documentAsJSON);

        /** here we create a TokenObject */
        Token t = new Token();

        /** the return type of the createToken() is a StringBuilder*/
        StringBuilder token = t.createToken();
        //System.out.println(token);

        /** set the token and build the connection to the API.
         with this connection we can do an Signature Request*/
        HttpURLConnection conn = t.setToken(token);

        /** we send here the Signature request to the Signers and receive a responseBody*/
        String responseBody = sendSignatureRequest(conn, documentAsJSON);
        //System.out.println(responseBody);

        /** we need to transform the responseBody to an DocumentObject
         *  so we easily handle it and make further API requests
         */
        //get the JSON data and transform it into a Document object
        Document document = gson.fromJson(responseBody,Document.class);
        //we can retrieve every Document attribute
        //System.out.println(document.message);

        // this is our polling code. every 10sek it makes a request and checks
        // for the overall status of the document if it's signed or not
        Poller poller = new Poller(token);
        poller.pollingDocument(document, token,10); //pollingInterval in sek
    }

    /**
     * with this function we send a POST request with a JSON request body and receive an response Body
     * which we can use to make further API calls
     */
    public static String sendSignatureRequest(HttpURLConnection connection, String documentAsJSON){
        try(OutputStream os1 = connection.getOutputStream()){
            byte[] input1 = documentAsJSON.getBytes(StandardCharsets.UTF_8);
            os1.write(input1,0, input1.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String responseData = "";
        try(BufferedReader br1 = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response1 = new StringBuilder();
            String responseLine1 = null;
            while ((responseLine1 = br1.readLine()) != null) {
                response1.append(responseLine1.trim());
                responseData += responseLine1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    /**
     * we encode our pdf into base64, because it's necessary to pass it into the request body as content
     */
    public static String encodePDF(String documentPath) throws IOException {
        File file = new File(documentPath);
        byte[] bytes = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(bytes);
    }
}
