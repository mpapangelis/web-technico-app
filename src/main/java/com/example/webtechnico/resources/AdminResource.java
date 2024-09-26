package com.example.webtechnico.resources;

import com.example.webtechnico.exceptions.ResourceNotFoundException;
import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.services.AdminService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("admin")
public class AdminResource {
    
    @Inject
    @Named("AdminService")
    private AdminService adminService;
    
    @PermitAll
    @GET
    @Path("/repairs/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveRepairs() {
        try {
            List<PropertyRepair> activeRepairs = adminService.getActiveRepairs();
            return Response.ok(activeRepairs).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No active repairs found.")
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to retrieve active repairs.")
                           .build();
        }
    }
    
    @PermitAll
    @GET
    @Path("/repairs/inactive")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInactiveRepairs() {
        try {
            List<PropertyRepair> inactiveRepairs = adminService.getInactiveRepairs();
            return Response.ok(inactiveRepairs).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No inactive repairs found.")
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to retrieve inactive repairs.")
                           .build();
        }
    }
    
    @PermitAll
    @GET
    @Path("/repairs/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPendingRepairs() {
        try {
            List<PropertyRepair> pendingRepairs = adminService.getAllPendingRepairs();
            return Response.ok(pendingRepairs).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No pending repairs found.")
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to retrieve pending repairs.")
                           .build();
        }
    }
}
