package it.jump3.controller;

import it.jump3.controller.model.GenericResponse;
import it.jump3.file.DownloadResponse;
import it.jump3.file.FileUploadForm;
import it.jump3.file.FileUtil;
import it.jump3.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/file")
@Slf4j
public class FileController {

    @Inject
    FileService fileService;

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@MultipartForm @Valid FileUploadForm form,
                               @Context UriInfo uriInfo) throws IOException {

        log.info("**** START -> upload ****");
        String filename = fileService.uploadOnFs(form);
        UriBuilder builder = uriInfo.getBaseUriBuilder().path("file/download/" .concat(filename));
        log.info("**** END -> upload ****");

        return Response.created(builder.build()).build();

    }

    @POST
    @Path("/upload-multi")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(MultipartFormDataInput input,
                               @Context UriInfo uriInfo) throws IOException {

        log.info("**** START -> upload-multi ****");
        Set<String> filenames = fileService.uploadOnFs(input);
        UriBuilder builder = uriInfo.getBaseUriBuilder().path("file/download/");
        filenames = filenames.stream()
                .map(s -> builder.build().toString().concat(s))
                .collect(Collectors.toSet());
        log.info("**** END -> upload-multi ****");

        return Response.ok(new GenericResponse(filenames)).build();
    }

    @GET
    @Path("/download/{fileName:.+}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFileInTextFormat(@PathParam("fileName") String fileName) {

        log.info("**** START -> download ****");
        File file = fileService.loadFile(fileName);
        log.info("**** START -> download ****");

        return FileUtil.download(new DownloadResponse(file, fileName));
    }
}
