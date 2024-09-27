package com.example.webtechnico.resources;

import com.example.webtechnico.exceptions.DuplicateEntryException;
import com.example.webtechnico.exceptions.InvalidYearException;
import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.exceptions.ResourceNotFoundException;
import com.example.webtechnico.models.Property;
import com.example.webtechnico.services.PropertyOwnerService;
import com.example.webtechnico.services.PropertyService;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("property")
public class PropertyResource {
    
    @Inject
    @Named("PropertyOwnerService")
    private PropertyOwnerService propertyOwnerService;
    
    @Inject
    @Named("PropertyService")
    private PropertyService propertyService;
    
    /**
     * Creates a new property.
     *
     * @param property The Property object containing the details of the property to be created.
     * @return Response containing the created Property object.
     */
    @PermitAll
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProperty(Property property) {
        try {
            Property createdProperty = propertyService.createProperty(
                property.getPropertyId(),
                property.getAddress(),
                property.getYearOfConstruction(),
                property.getPropertyType(),
                property.getPropertyOwner().getId()
            );
            return Response.ok(createdProperty).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to create property: " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Soft deletes a property by setting its isActive status to false.
     *
     * @param id The ID of the property to be soft deleted.
     * @return Response containing the soft deleted Property object.
     */
    @PermitAll
    @DELETE
    @Path("/softDelete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response softDeleteProperty(@PathParam("id") Long id) {
        try {
            Property deletedProperty = propertyService.softDeleteProperty(id);
            return Response.ok(deletedProperty).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                            .entity("Property not found " + e.getMessage())
                            .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Failed to delete property: " + e.getMessage())
                            .build();
        }
    }
    
    /**
     * Finds properties by the owner's VAT number.
     *
     * @param vatNumber The VAT number of the property owner.
     * @return Response containing a list of properties associated with the specified VAT number.
     */
    @PermitAll
    @GET
    @Path("/owner/vat/{vatNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPropertiesByOwnerVatNumber(@PathParam("vatNumber") Long vatNumber) {
        try {
            List<Property> properties = propertyService.findByOwnerVatNumber(vatNumber);
            return Response.ok(properties).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No properties found for owner with VAT number: " + vatNumber)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving properties: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Finds a property by its identification number (propertyId).
     *
     * @param propertyId The identification number of the property.
     * @return Response containing the Property object, if found.
     */
    @PermitAll
    @GET
    @Path("/propertyId/{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPropertyByPropertyId(@PathParam("propertyId") Long propertyId) {
        try {
            Property property = propertyService.findByPropertyIdNumber(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
            return Response.ok(property).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Property not found: " + e.getMessage())
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving property: " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Hard deletes a property, removing it from the database.
     *
     * @param id The ID of the property to be hard deleted.
     * @return Response indicating whether the deletion was successful.
     */
    @PermitAll
    @DELETE
    @Path("/hardDelete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProperty(@PathParam("id") Long id) {
        try {
            Property deletedProperty = propertyService.deleteProperty(id);
            if (deletedProperty == null) {
                throw new ResourceNotFoundException("Property not found for id: " + id);
            }
            return Response.ok(deletedProperty).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Property not found: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting property: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Updates the details of a property.
     *
     * @param id The ID of the property to be updated.
     * @param property The Property object containing updated details.
     * @return Response containing the updated Property object.
     */
    @PermitAll
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProperty(@PathParam("id") Long id, Property property) {
        try {
        Property updatedProperty = propertyService.updateProperty(
            id,
            property.getPropertyId(),
            property.getAddress(),
            property.getYearOfConstruction(),
            property.getPropertyType(),
            property.getPropertyOwner().getId()
        );
        return Response.ok(updatedProperty).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Property not found: " + e.getMessage())
                           .build();
        } catch (DuplicateEntryException e) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("Duplicate property identification number: " + e.getMessage())
                           .build();
        } catch (InvalidYearException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid year of construction: " + e.getMessage())
                           .build();
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Owner not found: " + e.getMessage())
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error updating property: " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Finds properties by the owner's ID.
     *
     * @param ownerId The ID of the property owner.
     * @return Response containing a list of properties associated with the specified owner ID.
     */
    @PermitAll
    @GET
    @Path("/owner/id/{ownerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPropertiesByOwnerId(@PathParam("ownerId") Long ownerId) {
        try {
            List<Property> properties = propertyService.findPropertiesByOwnerId(ownerId);
            return Response.ok(properties).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No properties found for owner with ID: " + ownerId)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving properties: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Finds a property by its primary key ID.
     *
     * @param id The primary key ID of the property.
     * @return Response containing the Property object, if found.
     */
    @PermitAll
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPropertyById(@PathParam("id") Long id) {
        try {
            Property property = propertyService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
            return Response.ok(property).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Property not found: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving property: " + e.getMessage())
                    .build();
        }
    }
}
