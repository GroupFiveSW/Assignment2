
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


            var payload = exchange.getRequestBody().readAllBytes();

            var jsonString = new String(payload);
            JSONObject json = toJson(jsonString);


            Runtime run = Runtime.getRuntime();

            Process process = run.exec("mkdir repo/");
            process.waitFor();

            System.out.println("Created temporary repo");

            String branch = json.getString("ref").split("/")[2];
            String cloneUrl = json.getJSONObject("repository").getString("clone_url");

            System.out.println("Cloning repo...");
            process =  run.exec("git clone -b " +  branch + " " + cloneUrl +  " repo/");
            process.waitFor();

            System.out.println("Cloned repo from " + cloneUrl + " using branch " + branch);


            MavenIntegration mvn = new MavenIntegration("repo/pom.xml");
            System.out.println("Running tests...");
            var result = mvn.test();

            String recipientEmail = json.getJSONObject("head_commit").getJSONObject("committer").getString("email");
            MailHandler mail = new MailHandler(new SMTPSettings("smtp.gmail.com","587", recipientEmail));
            mail.mail(result);
            System.out.println("Sent email to " + recipientEmail);

            String commitId = json.getJSONObject("head_commit").getString("id");
            String commitMessage = json.getJSONObject("head_commit").getString("message");
            BuildHistory build = new BuildHistory();
            build.addBuild(result, commitId, commitMessage);




            System.out.println("Successfully ran job");
            System.out.println(result.getLog());


            String responseText = result.isSuccessful() ? "Project was successfully compiled and tested" : "Project failed to compile";
            responseText += "\n" + result.getLog();
            exchange.sendResponseHeaders(200, responseText.length());
            OutputStream output = exchange.getResponseBody();
            output.write(responseText.getBytes(StandardCharsets.UTF_8));
            output.close();
        } catch (Exception e){
            e.printStackTrace();
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

    /**
     * Fetches all builds from database and returns html template containing build url:s
     * @param exchange
     * @throws IOException
     */
    public static void buildHistory(HttpExchange exchange)
            throws IOException
    {
        Headers headers = exchange.getResponseHeaders();
        headers.set("ContentType", "text/html;charset=utf-8");

        BuildHistory buildHistory = new BuildHistory();
        String response = "<html><body style=\"background:#FAFAFA;\"><h2>Build History: </h2><ul>";

        ArrayList<BuildHistoryResult> builds = buildHistory.getAllBuilds();

        String url = "/build";

        for (BuildHistoryResult build: builds) {
            response += "<li style=\"margin-bottom:8px;\">" +
                            "<a href=\"" + url + "/" + build.getId() + "\">" + build.getCommitMessage()+ "</a>" +
                            "<div style=\"display:flex;padding-top:6px;\">" +
                                "<h5 style=\"margin:0px 0px;\">Commit id: " + build.getCommit() +"</h5>"+
                                "<h5 style=\"margin:0px 0px;margin-left:20px;\">Date: " + build.getDate() + "</h5>"+
                            "</div>"+
                        "</li>";
        }

        response += "</ul></body></html>";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes(StandardCharsets.UTF_8));
        output.close();
    }

    /**
     * Fetches information for single build and returns html template containing
     * build date, commit id & build log.
     * @param exchange
     * @throws IOException
     */
    public static void getSingleBuild(HttpExchange exchange)
            throws IOException
    {
        Headers headers = exchange.getResponseHeaders();
        headers.set("ContentType", "text/html;charset=utf-8");

        String req = String.valueOf(exchange.getRequestURI());
        String[] reqArr = req.split("/");

        String response = "<html><body style=\"background:#FAFAFA;\">";
        if (reqArr.length > 2){
            try {
                int buildID = Integer.parseInt(reqArr[2]);
                BuildHistory buildHistory = new BuildHistory();
                BuildHistoryResult build = buildHistory.getBuildById(buildID);
                if (build == null){
                    response = "<html><body>Build not found!</body></html>";
                } else {
                    response += "<div>" +
                                    "<div style=\"display:flex;\">" +
                                    "<h2 style=\"font-size:18px;\">" + "CommitID: " + build.getCommit() + "</h2>"+
                                    "<h2 style=\"margin-left:20px;font-size:18px;\">" + "Build Date: " + build.getDate() + "</h2>"+
                                    "</div>"+
                                    "<p>" + build.getLog().replaceAll("(\r\n|\n)", "<br>") + "</p>" +
                                "</div>";
                    response += "</body></html>";
                }
            } catch (NumberFormatException e) {
                response = "<html><body>Invalid URL</body></html>";
            }
        } else {
            response = "<html><body>Invalid URL</body></html>";
        }
        exchange.sendResponseHeaders(200, response.length());
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes(StandardCharsets.UTF_8));
        output.close();
    }







 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8081"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext root = server.createContext("/ci");
        HttpContext node1 = server.createContext("/");
        HttpContext buildHistory = server.createContext("/buildHistory");
        HttpContext singleBuildHistory = server.createContext("/build");

        System.out.println("Listening on port: " + port);
        root.setHandler(ContinuousIntegrationServer::handle);

        node1.setHandler(ContinuousIntegrationServer::handle2);

        buildHistory.setHandler(ContinuousIntegrationServer::buildHistory);
        singleBuildHistory.setHandler(ContinuousIntegrationServer::getSingleBuild);


        server.start();
    }
}
