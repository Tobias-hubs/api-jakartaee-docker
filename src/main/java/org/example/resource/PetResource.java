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
    public Collection<PetDTO> list(
            @QueryParam("species") String species,
            @QueryParam("sortBy") @DefaultValue("id") String sortBy,
            @QueryParam("order") @DefaultValue("asc") String order,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit) {

        // Get all pets
        List<PetDTO> all = new ArrayList<>(service.getAll());

        // Filtering
        if (species != null && !species.isBlank()) {
            all = all.stream()
                    .filter(pet -> pet.getSpecies().equalsIgnoreCase(species)) //
                    .toList();
        }

        // Sorting
        Comparator<PetDTO> comparator = switch (sortBy.toLowerCase()) {
            case "happinesLevel" -> Comparator.comparing(PetDTO::getHappinesLevel);
            case "hungerLevel" -> Comparator.comparing(PetDTO::getHungerLevel);
            case "name" -> Comparator.comparing(PetDTO::getName);
            default -> Comparator.comparing(PetDTO::getName); // fallback
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        all = all.stream().sorted(comparator).toList();

        // Pagination
        int from = Math.min(offset, all.size());
        int to = Math.min(from + limit, all.size());

        return all.subList(from, to);
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        PetDTO pet = service.getById(id);
        if (pet == null) {
            throw new NotFoundException("Pet with id " + id + " not found");
        }
        return Response.ok(pet).build();
    }

    @PUT
    @Path("/{id}/feed")
    public Response feed(@PathParam("id") Long id) {
        PetDTO pet = service.getById(id);
        if (pet == null) {
            throw new NotFoundException("Pet with id " + id + " not found");
        }
        pet.setHungerLevel(Math.max(0, pet.getHungerLevel() - 10));
        return Response.ok(pet).build();
    }

    @PUT
    @Path("/{id}/play")
    public Response play(@PathParam("id") Long id) {
        PetDTO pet = service.getById(id);
        if (pet == null) {
            throw new NotFoundException("Pet with id " + id + " not found");
        }
        pet.setHappinesLevel(Math.min(100, pet.getHappinesLevel() + 10));
        return Response.ok(pet).build();
    }

    @DELETE
    @Path("/{id}")
    public Response release(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
