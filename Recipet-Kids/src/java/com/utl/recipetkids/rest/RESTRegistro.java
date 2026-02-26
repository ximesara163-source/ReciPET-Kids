package com.utl.recipetkids.rest;

import com.utl.recipetkids.controller.ControllerRegistro;
import com.utl.recipetkids.model.Maestro;
import com.google.gson.Gson;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

@Path("/registro")
public class RESTRegistro {

    @GET
    @Path("listarMaestros")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarMaestros() {
        try {
            ControllerRegistro cr = new ControllerRegistro();
            List<Maestro> lista = cr.listarMaestros();
            String out = new Gson().toJson(lista);
            return Response.ok(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.toString())
                    .build();
        }
    }

    @GET
    @Path("listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar(@QueryParam("fecha") String fecha,
                           @QueryParam("grado") String grado,
                           @QueryParam("grupo") String grupo) {
        try {
            ControllerRegistro ctrl = new ControllerRegistro();
            String json = ctrl.listar(fecha, grado, grupo);
            return Response.ok(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("{\"error\":\"" + e.toString() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("listarGrupos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarGrupos() {
        try {
            ControllerRegistro ctrl = new ControllerRegistro();
            String json = ctrl.listarGrupos();
            return Response.ok(json).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"error\":\"" + e.toString() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("estadisticas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response estadisticas() {
        try {
            ControllerRegistro ctrl = new ControllerRegistro();
            String json = ctrl.estadisticas();
            return Response.ok(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("{\"error\":\"" + e.toString() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("agregar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregar(String body) {
        try {
            ControllerRegistro ctrl = new ControllerRegistro();
            return Response.ok(ctrl.agregar(body)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"error\":\"" + e.toString() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modificar(String body) {
        try {
            ControllerRegistro ctrl = new ControllerRegistro();
            return Response.ok(ctrl.modificar(body)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"error\":\"" + e.toString() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("eliminar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(String body) {
        try {
            ControllerRegistro ctrl = new ControllerRegistro();
            return Response.ok(ctrl.eliminar(body)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"error\":\"" + e.toString() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("agregarGrupo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregarGrupo(String body) {
        try {
            ControllerRegistro ctrl = new ControllerRegistro();
            return Response.ok(ctrl.agregarGrupo(body)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"error\":\"" + e.toString() + "\"}")
                    .build();
        }
    }
}