package app.web.pavelk.server1;

import static spark.Spark.get;
import static spark.Spark.post;

public class Hello {
    public static void hello() {

        get("/hello", (req, res) -> "Hello World");
        post("/hello", (req, res) -> "Hello World");

        get("/say/*/to/*", (request, response) -> {
            return "Number of splat parameters: " + request.splat().length;
        });

    }

}
