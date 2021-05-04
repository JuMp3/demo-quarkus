package it.jump3.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.MessageBundles;
import it.jump3.controller.model.ItemDto;
import it.jump3.i18n.AppMessages;
import it.jump3.i18n.MessageResolver;
import it.jump3.util.Utility;
import it.jump3.util.qute.QuteTemplates;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Path("qute")
@Tag(name = "qute")
@Slf4j
public class QuteController {

    //@Inject
    //Template hello;

    @Inject
    MessageResolver messageResolver;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance get(@HeaderParam(HttpHeaders.ACCEPT_LANGUAGE) String lang,
                                @QueryParam("name") String name) {
        //return hello.data("name", name);
        //log.info(hello.instance().setAttribute("locale", Utility.stringToLocale(lang)).data("name", name).render());
        log.info(messageResolver.hello(Utility.stringToLocale(lang)));
        return QuteTemplates.hello().data("name", name);
    }

    @GET
    @Path("/item")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@HeaderParam(HttpHeaders.ACCEPT_LANGUAGE) String lang,
                                @BeanParam ItemDto itemDto) {
        return QuteTemplates.item(itemDto);
    }
}
