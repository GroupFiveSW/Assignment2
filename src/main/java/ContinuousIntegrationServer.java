
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import org.json.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Server that receives HTTP requests from GitHub in order
 * to run the CI steps on a particular version.
 */
public class ContinuousIntegrationServer
{
    /**
     * Method that parses a JSON string into an JSONObject
     * @param jsonString JSON formatted string
     * @return JSONObject
     */
    public static JSONObject toJson (String jsonString) {
        JSONParser parser = new JSONParser();
        Object json = null;
        JSONObject jsonObject = null;
        try{
            json = parser.parse(jsonString);
        } catch (ParseException e){
            e.printStackTrace();
        }
        try{
            json = new JSONTokener(json.toString()).nextValue();
        } catch (JSONException e){
            e.printStackTrace();
        }
        if (json instanceof  JSONObject){
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }

    public static void handle(HttpExchange exchange)
            throws IOException
    {
        try{
            Headers headers = exchange.getResponseHeaders();
            headers.set("ContentType", "application/json");


            System.out.println(exchange.getRequestURI());

            // here you do all the continuous integration tasks
            // for example
            // 1st clone your repository
            // 2nd compile the code


            var payload = exchange.getRequestBody().readAllBytes();

            var jsonString = new String(payload);
            JSONObject json = toJson(jsonString);
            System.out.println("JSON Object retrieved successfully.");

            System.out.println("Getting runtime..");
            Runtime run = Runtime.getRuntime();

            Process process = run.exec("mkdir repo/");
            process.waitFor();

            System.out.println("Created temporary repo");

            process =  run.exec("git clone -b main https://github.com/GroupFiveSW/TestRepo.git repo/");
            process.waitFor();

            System.out.println("Cloned repo into temporary folder");


            String responseText = "CI job done";
            exchange.sendResponseHeaders(200, responseText.length());
            OutputStream output = exchange.getResponseBody();
            output.write(responseText.getBytes(StandardCharsets.UTF_8));
            output.close();
        } catch (Exception e){
            System.out.println("Process failed");
            String responseText = "CI job failed";
            exchange.sendResponseHeaders(500, responseText.length());
            OutputStream output = exchange.getResponseBody();
            output.write(responseText.getBytes(StandardCharsets.UTF_8));
            output.close();
        }

    }


    public static void handle2(HttpExchange exchange)
            throws IOException
    {
        Headers headers = exchange.getResponseHeaders();
        headers.set("ContentType", "text/html;charset=utf-8");


        System.out.println(exchange.getRequestURI());

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        String responseText = "CI job done 2";
        exchange.sendResponseHeaders(200, responseText.length());
        OutputStream output = exchange.getResponseBody();
        output.write(responseText.getBytes(StandardCharsets.UTF_8));
        output.close();
    }






 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        ContinuousIntegrationServer cis = new ContinuousIntegrationServer();
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8081"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext root = server.createContext("/ci");
        HttpContext node1 = server.createContext("/");

        System.out.println("Listening on port: " + port);
        root.setHandler(ContinuousIntegrationServer::handle);

        node1.setHandler(ContinuousIntegrationServer::handle2);
        
        server.start();
    }
}
