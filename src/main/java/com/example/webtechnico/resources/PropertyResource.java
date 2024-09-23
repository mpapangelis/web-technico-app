package com.example.webtechnico.resources;

import com.example.webtechnico.models.Property;
import com.example.webtechnico.services.PropertyOwnerService;
import com.example.webtechnico.services.PropertyService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("property")
public class PropertyResource {
    
//    @Inject
//    @Named("PropertyOwnerService")
//    private PropertyOwnerService propertyOwnerService;
    
    @Inject
    @Named("PropertyService")
    private PropertyService propertyService;
    
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
    
}
