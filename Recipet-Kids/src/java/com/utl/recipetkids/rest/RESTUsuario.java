package com.utl.recipetkids.rest;

import com.google.gson.Gson;
import com.utl.recipetkids.controller.ControllerUsuario;
import com.utl.recipetkids.model.Usuario;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("usuario")
public class RESTUsuario
{
@POST
@Path("login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response login(Usuario datos)
{
    ControllerUsuario cu = new ControllerUsuario();
    Usuario u;
    String out;

    try
    {
        u = cu.validate(datos.getNombreUsuario(), datos.getContrasenia());

        if (u == null)
        {
            out = """
                  {"error":"Nombre de usuario o contraseña incorrectos."}
                  """;
        }
        else
        {
            out = new Gson().toJson(u);
        }
    }
    catch (Exception e)
    {
        e.printStackTrace();
        out = """
              {"exception":"Error en el servidor"}
              """;
    }

    return Response.ok(out).build();
}
}