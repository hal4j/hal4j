package com.github.hal4j.resources;

import java.util.List;
import java.util.Map;

/**
 * Resource containing only navigation links and attachments, but no internal model.
 */
public final class NavigationResource extends ResourceSupport {

    NavigationResource(Map<String, List<HALLink>> _links,
                              Map<String, List<Object>> _embedded) {
        super(_links, _embedded, null);
    }

    public NavigationResource(Map<String, List<HALLink>> _links,
                              Map<String, List<Object>> _embedded,
                              BindingContext context) {
        super(_links, _embedded, context);
    }

}
