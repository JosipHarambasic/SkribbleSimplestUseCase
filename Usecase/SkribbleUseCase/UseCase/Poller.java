import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class Poller {
    private static StringBuilder token;

    public Poller(StringBuilder token){
        Poller.token = token;
    }

    public void pollingDocument(Document document, StringBuilder token, Integer pollingInterval){
        Gson gson = new Gson();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("https://api.scribital.com/v1/signature-requests/"+document.id);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection connection = null;
                try {
                    assert url != null;
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    assert connection != null;
                    setConnectionRequestProperties(connection, "GET", token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = null;
                try {
                    data = GETRequestResponseContent(url,token);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Document doc1 = gson.fromJson(data,Document.class);
                if (doc1.status_overall.equals("DECLINED")) {
                    System.out.println("Your request was declined");
                    timer.cancel();

                } else if (doc1.status_overall.equals("SIGNED")) {
                    System.out.println("Your document is now SIGNED and downloaded");
                    try {
                        // we need the connection from above, else it won't download a pdf that is usable
                        HttpURLConnection signedPDFConnection = processGetRequest(doc1);
                        // now we download the signed PDF
                        downloadPDF(signedPDFConnection,"C:\\Users\\41786\\OneDrive\\Desktop\\haha.pdf");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    timer.cancel();
                }
                //System.out.println(doc1.status_overall);

                connection.disconnect();
            }
        }, 0, pollingInterval*1000);
    }

    /**
     * we make a GET request and receive the response content of the request as a JSON format,
     * we can use it and transform it with GSON into an Object
     * so the handling with all the variables get easier
     */
    public static String GETRequestResponseContent(URL url, StringBuilder token) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization","Bearer "+ token.toString());
        // set the type of request (POST, GET, PUT, DELETE)
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        String line;
        StringBuffer responseContent = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        while ((line = reader.readLine()) != null){ responseContent.append(line);
        }
        return responseContent.toString();
    }
    /**
     * we set the request properties for our connection, so it can be connected properly
     */
    public static void setConnectionRequestProperties(HttpURLConnection connection, String requestType, StringBuilder token) throws IOException {
        // set the type of request (POST, GET, PUT, DELETE)
        connection.setRequestProperty("Authorization", "Bearer " + token.toString());
        connection.setRequestMethod(requestType);
        // type of content, in this case it is a JSON file
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);
        // set the Timeout, will disconnect if the connection did not work, avoid infinite waiting
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(6000);
    }
    /**
     * we download the signed file on our local machine and have just to specify the location
     */
    public static void downloadPDF(HttpURLConnection connection, String pathToBeStored){
        try  {
            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(pathToBeStored);
            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * we make a get request to retrieve the signed document so we can download it
     */
    public static HttpURLConnection processGetRequest(Document document) throws IOException {
        //GET REQUEST TO RETRIEVE PDF
        URL url = new URL("https://api.scribital.com/v1/documents/"+document.getDocument_id()+"/content");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        setConnectionRequestProperties(connection, "GET", token);
        return connection;
    }
}
