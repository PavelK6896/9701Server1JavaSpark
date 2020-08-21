package app.web.pavelk.server1;

import com.google.gson.Gson;
import spark.ModelAndView;
import spark.ResponseTransformer;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Starter {
    private static Gson gson = new Gson();

    public static String render(Map<String, Object> model, String templatePath) {
        return new MustacheTemplateEngine().render(new ModelAndView(model, templatePath));
    }


    public static void main(String[] args) {
        port(8080);
        staticFiles.location("/static");
        Hello.hello();

        get("/", (req, res) -> "Hello World//");

        post("/:name", (req, res) -> {
            String name = req.params("name");

            MoodHolder mood = gson.fromJson(req.body(), MoodHolder.class); // body gson

            HashMap<String, String> map = new HashMap<>();

            map.put("name", name);
            map.put("device", req.queryParams("device"));
            map.put("user-age", req.queryMap().get("user").get("age").value());
            map.put("user-gender", req.queryMap().get("user").get("gender").value());
            map.put("mood", mood.getMood());
            //halt(403, "this ");

            return map;
        }, new JsonTransformer());

        after((request, response) -> {
            response.header("Content-Encoding", "gzip");//шифрование
        });

        notFound((req, res) -> {
            res.type("application/json");
            return gson.toJson(Collections.singletonMap("error", "not is"));
        });


    }
}


class MoodHolder { // gson
    private String mood;

    public MoodHolder() {
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}


class JsonTransformer implements ResponseTransformer { //сериализатор
    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
