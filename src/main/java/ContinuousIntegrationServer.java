
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
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

        String responseText = "CI job done";
        exchange.sendResponseHeaders(200, responseText.length());
        OutputStream output = exchange.getResponseBody();
        output.write(responseText.getBytes(StandardCharsets.UTF_8));
        output.close();
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext root = server.createContext("/");
        root.setHandler(ContinuousIntegrationServer::handle);
        server.start();
    }
}
