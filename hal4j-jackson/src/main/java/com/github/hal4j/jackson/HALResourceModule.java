package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.hal4j.resources.*;

public class HALResourceModule extends SimpleModule {

    private static final String MODULE_NAME = "HALResourceModule";

    public HALResourceModule() {
        super(MODULE_NAME, versionOf(HALResourceModule.class));
        setMixInAnnotation(GenericResource.class, GenericResourceMixin.class);
        setMixInAnnotation(NavigationResource.class, NavigationResourceMixin.class);
        setMixInAnnotation(Resource.class, ResourceMixin.class);
        setMixInAnnotation(Resources.class, ResourcesMixin.class);
        setMixInAnnotation(HALLink.class, HALLinkMixin.class);
    }

    @Override
    public void setupModule(SetupContext context) {
        ObjectMapper mapper = context.getOwner();
        mapper.setHandlerInstantiator(new ResourceHandlerInstantiator(null));
        super.setupModule(context);
    }

    private static Version versionOf(Class<HALResourceModule> moduleClass) {
        int major = 1;
        int minor = 0;
        int patch = 0;
        String snapshot = null;
        String groupId ="localhost";
        String artifactId = "hal4j-jackson";
        Package pckg = moduleClass.getPackage();
        if (pckg != null) {
            String version = pckg.getImplementationVersion();
            if (version != null) {
                int idx = version.indexOf('-');
                if (idx > 0) {
                    snapshot = version.substring(idx + 1);
                    version = version.substring(0, idx);
                }
                String[] items = version.split("\\.");
                major = parse(items, 0);
                minor = parse(items, 1);
                patch = parse(items, 2);
            }
        }
        return new Version(major, minor, patch, snapshot, groupId, artifactId);
    }

    private static int parse(String[] items, int i) {
        if (i >= items.length) return 0;
        try {
            return Integer.parseInt(items[i]);
        } catch (NullPointerException | NumberFormatException e) {
            return 0;
        }
    }

}
