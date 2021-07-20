import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Token {

    StringBuilder Token;

    /**
     * we create a token, this is necessary to continue and use the API calls
     */
    public StringBuilder createToken() throws IOException {
        // build a connection to the API call
        URL url = new URL("https://api.scribital.com/v1/access/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // set the type of request (POST, GET, PUT, DELETE)
        connection.setRequestMethod("POST");

        // type of content in this case it is a JSON file
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        // set the Timeout, will disconnect if the connection did not work, avoid infinite waiting
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(6000);

        //set the RequestBody
        String jsonInputString = "{\"username\": \"api_demo_skribble_d901_0\", \"api-key\":\"118d6d49-1415-4f8e-bd16-2a0ef03beaf9\"}";
        try(OutputStream os = connection.getOutputStream()){
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input,0, input.length);
        }

        // read the response Body
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
                Token = response;
            }
        }
        return Token;
    }

    /**
     * after the token was created we set it as Bearer Token for the authentication
     */
    public HttpURLConnection setToken(StringBuilder token) throws IOException {
        URL url1 = new URL("https://api.scribital.com/v1/signature-requests");
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        // took the same token from above for the authorization
        connection.setRequestProperty("Authorization","Bearer "+ token.toString());
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }
}
