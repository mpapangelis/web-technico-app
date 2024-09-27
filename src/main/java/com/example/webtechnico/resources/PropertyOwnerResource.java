package com.example.webtechnico.resources;

import com.example.webtechnico.exceptions.DuplicateEntryException;
import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.models.PropertyOwner;
import com.example.webtechnico.services.PropertyOwnerService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("propertyOwner")
public class PropertyOwnerResource {
    
    @Inject
    @Named("PropertyOwnerService")
    private PropertyOwnerService propertyOwnerService;
    
    /**
     * Creates a new property owner.
     *
     * @param propertyOwner The PropertyOwner object to create.
     * @return Response containing the created PropertyOwner.
     */
    @PermitAll
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPropertyOwner(PropertyOwner propertyOwner) {
        propertyOwnerService.create(
            propertyOwner.getFirstName(),
            propertyOwner.getLastName(),
            propertyOwner.getEmail(),
            propertyOwner.getUserName(),
            propertyOwner.getPhoneNumber().toString(),
            propertyOwner.getAddress(),
            propertyOwner.getVat().toString(),
            propertyOwner.getPassword()
        );
        return Response.ok(propertyOwner).build();
    }
    
    /**
     * Retrieves a property owner by their ID.
     *
     * @param id The ID of the property owner.
     * @return Response containing the PropertyOwner if found, otherwise an error message.
     */
    @PermitAll
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPropertyOwnerById(@PathParam("id") Long id) {
        try {
            PropertyOwner propertyOwner = propertyOwnerService.get(id);
            return Response.ok(propertyOwner).build();
        } catch (OwnerNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                            .entity("Owner with ID: " + id + " not found.")
                            .build();
        }
    }
    
    /**
     * Soft deletes a property owner by setting their isActive status to false.
     *
     * @param id The ID of the property owner to soft delete.
     * @return Response indicating the result of the operation.
     */
    @PermitAll
    @DELETE
    @Path("/softDelete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response softDeletePropertyOwner(@PathParam("id") Long id) {
        try {
            propertyOwnerService.softDelete(id);
            return Response.ok("PropertyOwner with ID " + id + " has been soft deleted.").build();   
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                       .entity("Owner with ID " + id + " not found.")
                       .build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("Failed to soft delete the PropertyOwner with ID " + id + ".")
                       .build();
        }
    }
    
    /**
     * Retrieves all property owners.
     *
     * @return Response containing a list of all PropertyOwners.
     */
    @PermitAll
    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllPropertyOwners() {
        try {
            List<PropertyOwner> propertyOwners = propertyOwnerService.findAll();
            return Response.ok(propertyOwners).build();
        } catch (Exception e) {
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to retrieve property owners.")
                           .build();
        }
    }
    
    /**
     * Hard deletes a property owner by their ID.
     *
     * @param id The ID of the property owner to delete.
     * @return Response indicating the result of the operation.
     */
    @PermitAll
    @DELETE
    @Path("/hardDelete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePropertyOwner(@PathParam("id") Long id) {
        try {
            propertyOwnerService.delete(id);
            return Response.ok("PropertyOwner with ID " + id + " has been deleted.").build();
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Owner with ID " + id + " not found.")
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to delete the PropertyOwner with ID " + id + ".")
                           .build();
        }
    }
    
    /**
     * Updates a property owner's details.
     *
     * @param id            The ID of the property owner to update.
     * @param propertyOwner The PropertyOwner object containing updated details.
     * @return Response containing the updated PropertyOwner.
     */
    @PermitAll
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePropertyOwner(@PathParam("id") Long id, PropertyOwner propertyOwner) {
        try {
            PropertyOwner updatedOwner = propertyOwnerService.update(
                id,
                propertyOwner.getFirstName(),
                propertyOwner.getLastName(),
                propertyOwner.getEmail(),
                propertyOwner.getUserName(),
                propertyOwner.getPhoneNumber().toString(),
                propertyOwner.getAddress(),
                propertyOwner.getVat()
            );
            return Response.ok(updatedOwner).build();
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Owner with ID " + id + " not found: " + e.getMessage())
                           .build();
        } catch (DuplicateEntryException e) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("Conflict: " + e.getMessage())
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to update the PropertyOwner with ID " + id + ": " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Retrieves a property owner by their email.
     *
     * @param email The email of the property owner.
     * @return Response containing the PropertyOwner if found, otherwise an error message.
     */
    @PermitAll
    @GET
    @Path("/findByEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPropertyOwnerByEmail(@PathParam("email") String email) {
        try {
            PropertyOwner propertyOwner = propertyOwnerService.findByEmail(email);
            return Response.ok(propertyOwner).build();
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Owner with email: " + email + " not found.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving property owner: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Retrieves a property owner by their VAT number.
     *
     * @param vat The VAT number of the property owner.
     * @return Response containing the PropertyOwner if found, otherwise an error message.
     */
    @PermitAll
    @GET
    @Path("/findByVat/{vat}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPropertyOwnerByVat(@PathParam("vat") Long vat) {
        try {
            PropertyOwner propertyOwner = propertyOwnerService.findByVat(vat);
            return Response.ok(propertyOwner).build();
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Owner with VAT: " + vat + " not found.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving property owner: " + e.getMessage())
                    .build();
        }
    }
}
