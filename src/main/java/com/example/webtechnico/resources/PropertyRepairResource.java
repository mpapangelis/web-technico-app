package com.example.webtechnico.resources;

import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.exceptions.ResourceNotFoundException;
import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.models.TypeOfRepairEnum;
import com.example.webtechnico.services.PropertyRepairService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("propertyRepair")
public class PropertyRepairResource {
    
    @Inject
    @Named("PropertyRepairService")
    private PropertyRepairService propertyRepairService;
    
    /**
     * Initiates a new property repair.
     *
     * @param propertyRepair The PropertyRepair object containing the details of the repair to be initiated.
     * @return Response containing the initiated PropertyRepair object.
     */
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
    
    /**
     * Soft deletes a property repair by setting its isActive status to false.
     *
     * @param repairId The ID of the property repair to be soft deleted.
     * @return Response indicating the result of the operation.
     */
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
    
    /**
     * Hard deletes a property repair.
     *
     * @param id The ID of the property repair to be hard deleted.
     * @return Response indicating whether the deletion was successful or not.
     */
    @PermitAll
    @DELETE
    @Path("/hardDelete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hardDeletePropertyRepair(@PathParam("id") Long id) {
        try {
            boolean deleted = propertyRepairService.delete(id);
            if (deleted) {
                return Response.ok("Property Repair with ID " + id + " has been deleted.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Property Repair with ID " + id + " not found.")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete the Property Repair with ID " + id + ".")
                    .build();
        }
    }
    
    /**
     * Searches for property repairs within a specified date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return Response containing a list of PropertyRepair objects within the specified date range.
     */
    @PermitAll
    @GET
    @Path("/searchByDateRange")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRepairsByDateRange(@QueryParam("startDate") Date startDate, @QueryParam("endDate") Date endDate) {
        try {
            List<PropertyRepair> repairs = propertyRepairService.searchRepairsByDateRange(startDate, endDate);
            return Response.ok(repairs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving repairs: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Searches for property repairs by submission date.
     *
     * @param submissionDate The date of submission.
     * @return Response containing a list of PropertyRepair objects submitted on the specified date.
     */
    @PermitAll
    @GET
    @Path("/searchBySubmissionDate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRepairsBySubmissionDate(@QueryParam("submissionDate") Date submissionDate) {
        try {
            List<PropertyRepair> repairs = propertyRepairService.searchRepairsBySubmissionDate(submissionDate);
            return Response.ok(repairs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving repairs: " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Searches for property repairs by owner ID.
     *
     * @param ownerId The ID of the owner.
     * @return Response containing a list of PropertyRepair objects associated with the specified owner.
     */
    @PermitAll
    @GET
    @Path("/searchByOwnerId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRepairsByOwnerId(@QueryParam("ownerId") Long ownerId) {
        try {
            List<PropertyRepair> repairs = propertyRepairService.searchRepairsByOwnerId(ownerId);
            return Response.ok(repairs).build();
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No repairs found for owner with ID: " + ownerId)
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving repairs: " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Updates a property repair with the given details.
     *
     * @param repairId The ID of the property repair to update.
     * @param propertyRepair The PropertyRepair object containing updated details.
     * @return Response containing the updated PropertyRepair object.
     */
    @PermitAll
    @PUT
    @Path("/update/{repairId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePropertyRepair(@PathParam("repairId") Long repairId, PropertyRepair propertyRepair) {
        try {
            PropertyRepair updatedRepair = propertyRepairService.updatePropertyRepair(
                    repairId,
                    propertyRepair.getProperty().getId(),
                    propertyRepair.getTypeOfRepair(),
                    propertyRepair.getShortDescription(),
                    propertyRepair.getDescription(),
                    propertyRepair.getProposedStartDate(),
                    propertyRepair.getProposedEndDate(),
                    propertyRepair.getProposedCost(),
                    propertyRepair.isOwnerAcceptance(),
                    propertyRepair.getStatus(),
                    propertyRepair.getActualStartDate(),
                    propertyRepair.getActualEndDate()
            );
            return Response.ok(updatedRepair).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("PropertyRepair not found: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update PropertyRepair: " + e.getMessage())
                    .build();
        }
    }
}
