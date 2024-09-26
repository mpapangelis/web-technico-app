package com.example.webtechnico.resources;

import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.models.TypeOfRepairEnum;
import com.example.webtechnico.services.PropertyRepairService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("propertyRepair")
public class PropertyRepairResource {
    
    @Inject
    @Named("PropertyRepairService")
    private PropertyRepairService propertyRepairService;
    
    @PermitAll
    @POST
    @Path("/initiate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initiateRepair(PropertyRepair propertyRepair) {
        try {
            propertyRepairService.initiateRepair(
                    propertyRepair.getProperty().getId(),
                    propertyRepair.getTypeOfRepair(),
                    propertyRepair.getShortDescription(),
                    propertyRepair.getDescription());
            return Response.ok(propertyRepair).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Failed to initiate repair: " + e.getMessage())
                    .build();
        }
    }
    
    @PermitAll
    @DELETE
    @Path("/softDelete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response softDeletePropertyRepair(@PathParam("id") Long repairId) {
        try {
            propertyRepairService.softDelete(repairId);
            return Response.ok("PropertyRepair with ID " + repairId + " has been soft deleted.").build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Cannot soft delete PropertyRepair with ID " + repairId + ": " + e.getMessage())
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to soft delete the PropertyRepair with ID " + repairId + ".")
                           .build();
        }
    }
    
}
