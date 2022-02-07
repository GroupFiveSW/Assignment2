
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import org.json.*;

/**
 * Server that receives HTTP requests from GitHub in order
 * to run the CI steps on a particular version.
 */
public class ContinuousIntegrationServer
{
    public static void handle(HttpExchange exchange)
            throws IOException
    {
        Headers headers = exchange.getResponseHeaders();
        headers.set("ContentType", "text/html;charset=utf-8");




        System.out.println(exchange.getRequestURI());

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code


        var payload = exchange.getRequestBody().readAllBytes();

        var jsonString = new String(payload);

//        System.out.print(jsonString);

//        System.out.print("hej");

        JSONObject jsonObj = new JSONObject(jsonString);

        System.out.print("test print: " + jsonObj.toString());

        System.out.print(jsonObj.getJSONObject("repository").getString("clone_url") );
//        System.out.print(jsonObj.getString("ref"));
        Runtime run = Runtime.getRuntime();

        run.exec("rm -rf");
        run.exec("mkdir repo/");
        run.exec("git clone -b main https://github.com/GroupFiveSW/TestRepo.git repo/");
        run.exec("ls");


//        String jsonStringg = jsonObj.getString();

//        System.out.print("printing the jsonstring:  " + jsonString);






        String responseText = "CI job done";
        exchange.sendResponseHeaders(200, responseText.length());
        OutputStream output = exchange.getResponseBody();
        output.write(responseText.getBytes(StandardCharsets.UTF_8));
        output.close();
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


    public void codeFetch(URL url){
//        InputStream is = url.openStream();

        try{
//            System.out.print("hej");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String input;
            while((input = reader.readLine()) != null){
//                System.out.print("hej");
                System.out.print(input);
            }
            reader.close();
        }catch(Exception e){

        }
        return;
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

//        URL herokuURL = new URL("https://kolkrabbi.heroku.com/hooks/github");
//        URL herokuURL = new URL("https://api.github.com/users/GroupFiveSW");
//        URL repoURL = new URL("https://api.github.com/users/GroupFiveSW/events");
//        URL repoURL = new URL("https://api.github.com/repos/Carnoustie/practiceProject/events");


//        HttpURLConnection con = (HttpURLConnection) new URL("https://api.github.com/users/GroupFiveSW").openConnection();

//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));



//        cis.codeFetch(repoURL);

        server.start();
    }
}
