package com.example.webtechnico.resources;

import com.example.webtechnico.exceptions.InvalidInputException;
import com.example.webtechnico.exceptions.PropertyNotFoundException;
import com.example.webtechnico.exceptions.ResourceNotFoundException;
import com.example.webtechnico.models.Property;
import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.services.AdminService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("admin")
public class AdminResource {
    
    @Inject
    @Named("AdminService")
    private AdminService adminService;
    
    /**
     * Retrieves all active property repairs.
     * 
     * @return Response containing a list of active property repairs.
     */
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
    
    /**
     * Retrieves all inactive property repairs.
     *
     * @return Response containing a list of inactive property repairs.
     */
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
    
    /**
     * Retrieves all pending property repairs.
     *
     * @return Response containing a list of pending property repairs.
     */
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
    
    /**
     * Retrieves all properties.
     *
     * @return Response containing a list of all properties.
     */
    @PermitAll
    @GET
    @Path("/properties")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProperties() {
        try {
            List<Property> properties = adminService.getAllProperties();
            return Response.ok(properties).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to retrieve properties: " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Retrieves all property repairs.
     *
     * @return Response containing a list of all property repairs.
     */
    @GET
    @Path("/repairs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRepairs() {
        try {
            List<PropertyRepair> repairs = adminService.getAllRepairs();
            return Response.ok(repairs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving repairs: " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Updates the repair proposition with the given details.
     *
     * @param repairId        ID of the repair to update.
     * @param propertyRepair  PropertyRepair object containing the updated details.
     * @return Response containing the updated PropertyRepair.
     */
    @PermitAll
    @POST
    @Path("/repairProposition/{repairId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRepairProposition(@PathParam("repairId") Long repairId, PropertyRepair propertyRepair) {
        try {
            PropertyRepair updatedRepair = adminService.repairProposition(
                repairId,
                propertyRepair.getStatus().name(),
                propertyRepair.getProposedStartDate(),
                propertyRepair.getProposedEndDate(),
                propertyRepair.getProposedCost()
            );
            return Response.ok(updatedRepair).build();
        } catch (PropertyNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Repair with ID " + repairId + " not found.")
                           .build();
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid input: " + e.getMessage())
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to update repair: " + e.getMessage())
                           .build();
        }
    }
}
