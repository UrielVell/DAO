package mx.uv;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Hello world!
 *
 */
public class App {

    static Gson gson = new Gson();
    static HashMap<String, Usuario> usuarios = new HashMap<String, Usuario>();
    public static void main(String[] args) {
        System.out.println("Hello World!");
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        get("/", (req, res) -> "<h1>Bienvenido</h1>");
        get("/hola", (req, res) -> "<h1>Hola</h1>");
        get("/adios", (req, res) -> "<h1>Adios</h1>");
        get("/alumno", (req, res) -> "{'alumno' : 'john','matricula' : 's0001','carrera' : 'tc'}");

        post("/alumno", (req, res) -> {
            System.out.println(req.contentLength());
            System.out.println(req.contentType());
            System.out.println(req.contextPath());
            return "hi " + req.queryParams("nombre");
        });

        get("/tipo-usuario", (req, res) -> {
            JsonObject oRes = new JsonObject();
            oRes.addProperty("tipo-usuario", "profesor");
            oRes.addProperty("nombre", req.queryParams("nombre"));
            oRes.addProperty("paterno", req.queryParams("apellidoP"));
            oRes.addProperty("materno", req.queryParams("apellidoM"));
            res.type("application/json");

            return oRes;

        });

        get("/usuario", (req,res) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("nombre", "uriel");
            jsonObject.addProperty("password", "1234");

            return jsonObject;
        });

        post("/json", (request, response) ->{
            String peticion = request.body();
            System.out.println(peticion);

            JsonParser parser = new JsonParser();
            JsonElement arbol = parser.parse(peticion);
            return arbol.getAsJsonObject().get("username").getAsString();
        });

        get("/usuarios", (request, response)->{
            response.type("application/json");
            //return gson.toJson(usuarios.values());
            return gson.toJson(DAO.getUsuarios());
        });
        post("/usuarios", (request, response)->{
            String payload = request.body();
            Usuario user = gson.fromJson(payload, Usuario.class);
            System.out.println("payload: "+ payload);
            System.out.println("nombre: "+ user.getNombre());
            System.out.println("correo: "+ user.getCorreo());
            String id = UUID.randomUUID().toString();
            user.setId(id);
            //usuarios.put(id, user);
            DAO.crearUsuario(user);
            return "Usuario Agregado";
        });

        post("/eliminarUsuario", (request, response)->{
            String payload = request.body();
            Usuario usuario = gson.fromJson(payload, Usuario.class);
            //System.out.println(id);
            DAO.eliminarUsuario(usuario.getId());
            return "Usuario Eliminado";
        });

        post("/editarUsuario", (request, response)->{
            String payload = request.body();
            Usuario usuario = gson.fromJson(payload, Usuario.class);
            System.out.println(payload);
            DAO.editarUsuario(usuario);
            return "Usuario Editado";
        });

    }
}
