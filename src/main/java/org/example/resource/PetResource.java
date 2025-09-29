package org.example.resource;

import org.example.dto.PetDTO;
import org.example.service.PetService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.*;

@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetResource {
    @Inject
    PetService service;

    @POST
    public Response adopt(@Valid PetDTO pet) {
        PetDTO saved = service.addPet(pet);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @GET
    public Collection<PetDTO> list() {
        return service.getAll();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        PetDTO pet = service.getById(id);
        if (pet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(pet).build();
    }

    @DELETE
    @Path("/{id}")
    public Response release(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
