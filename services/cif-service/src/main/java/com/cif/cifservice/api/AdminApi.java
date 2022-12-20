package com.cif.cifservice.api;

import com.cif.cifservice.core.party.domain.Config;
import io.swagger.annotations.*;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;


@Path("/admin")
@Api(description = "the admin API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2022-11-01T15:10:55.604369+05:30[Asia/Calcutta]")
public interface AdminApi {


    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Create Admin", notes = "", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Admin Created", response = FetchPartyDetailByIdentifier200ResponseInner.class),
            @ApiResponse(code = 400, message = "Invalid request body", response = ErrorResponseApi.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseApi.class)})
    Response addConfigRecord(@Valid @NotNull Config config);

    @GET
    @Path("/{type}/")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Fetch Admin", notes = "", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Admin Fetch", response = FetchPartyDetailByIdentifier200ResponseInner.class),
            @ApiResponse(code = 400, message = "Invalid request body", response = ErrorResponseApi.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseApi.class)})
    Response fetchConfigByType(@PathParam("type") @ApiParam("type") String type);

    @POST
    @Path("/bulkImport")
    @Consumes({"multipart/form-data"})
    @Produces({"application/json"})
    @ApiOperation(value = "Bulk Import", notes = "", tags = {"Bulk Import"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Bulk Import Success", response = FetchPartyDetailByIdentifier200ResponseInner.class),
            @ApiResponse(code = 400, message = "Invalid request body", response = ErrorResponseApi.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseApi.class)})
    Response bulkImport(@FormDataParam("file") InputStream fileData);
}
