package it.jump3.util.qute;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import it.jump3.controller.model.ItemDto;

@CheckedTemplate
public class QuteTemplates {

    public static native TemplateInstance hello();

    public static native TemplateInstance item(ItemDto item);
}
