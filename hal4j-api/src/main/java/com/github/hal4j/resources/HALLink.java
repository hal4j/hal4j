package com.github.hal4j.resources;

import com.github.hal4j.uritemplate.URIFactory;
import com.github.hal4j.uritemplate.URITemplate;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class HALLink implements Serializable {

    public static final String REL_SELF = "self";
    public static final String REL_NEXT = "next";
    public static final String REL_PREV = "prev";
    public static final String REL_PAGE = "page";
    public static final String REL_ITEMS = "items";

    public static final String PARAM_EXPAND = "expand";

    /**
     * An optional convention for href values indicating that provided link is pointing to the same
     * resource (i.e. "self" link of the resource can be used).
     * Example 1: multiple links referencing to self
     * <pre>
     *     {
     *          "_links" : {
     *              "self" : { "href": "https://api.example.com/object/1" },
     *              "crud:update" : { "href" : "." },
     *              "crud:delete" : { "href" : "." }
     *          }
     *     }
     * </pre>
     * Example 2: multiple self links defining operations on the object
     * <pre>
     *     {
     *          "_links" : {
     *              "self" : [
     *                  { "href" : "https://api.example.com/object/1" },
     *                  { "href" : ".", "name" : "http:DELETE" },
     *                  { "href" : ".", "name" : "http:PUT" }
 *                  ]
     *          }
     *     }
     * </pre>     */
    public static final String HREF_SAME_RESOURCE = ".";

    public static final Predicate<HALLink> SAME_RESOURCE = link -> link.href.equals(HREF_SAME_RESOURCE);

    public final String name;

    public final String href;

    public final String hreflang;

    public final boolean templated;

    public final String title;

    public final String type;

    public final URI profile;

    public final URI deprecation;

    /**
     * Creates new instance of HALLink object
     * @param href required, must either conform to specification of URI or be a valid URI template
     * @param templated when <code>true</code> indicates that this link should be treated as an URI template
     * @param title the title of this link which can be displayed to end users
     * @param name the semantic name of this link which can be used to determine its purpose programmatically
     * @param type the optional type of this link
     * @param hreflang the optional language of the target resource as ISO code
     * @param profile the optional URI of the specification of this link
     * @param deprecation the optional URI of the resource describing the reasons for deprecation of this link
     * @see URITemplate
     * @see URI
     */
    public HALLink(String href,
                   boolean templated,
                   String title,
                   URI name,
                   String type,
                   String hreflang,
                   URI profile,
                   URI deprecation) {
        if (!templated) {
            try {
                new URI(href);
            } catch (URISyntaxException x) {
                String msg = "href value must be a valid URI, if not a template";
                throw new IllegalArgumentException(msg, x);
            }
        }
        this.href = href;
        this.templated = templated;
        this.title = title;
        this.name = name != null ? name.toString() : null;
        this.type = type;
        this.hreflang = hreflang;
        this.profile = profile;
        this.deprecation = deprecation;
    }

    private HALLink(String href,
                   boolean templated,
                   String title,
                   String name,
                   String type,
                   String hreflang,
                   URI profile,
                   URI deprecation) {
        this.href = href;
        this.templated = templated;
        this.title = title;
        this.name = name;
        this.type = type;
        this.hreflang = hreflang;
        this.profile = profile;
        this.deprecation = deprecation;
    }

    /**
     * Checks if this link shall be treated as templated
     * @return <code>true</code> if this link is templated, <code>false</code> otherwise
     */
    public boolean isTemplated() {
        return templated;
    }

    /**
     * Returns the semantic name of this link which can be used to determine
     * its purpose programmatically or <code>null</code> if not set
     * @return this.name
     * @see HALLink#name
     */
    public String name() {
        return this.name;
    }

    /**
     * Returns the title of this link which can be displayed to end users
     * or <code>null</code> if not set
     * @return this.title
     * @see HALLink#title
     */
    public String title() {
        return this.title;
    }

    /**
     * Returns the non-null value of this link
     * @return this.href
     * @see HALLink#href
     */
    public String href() {
        return this.href;
    }

    /**
     * Returns the type of this link which can be used to
     * or <code>null</code> if not set
     * @return this.type
     * @see HALLink#type
     */
    public String type() {
        return this.type;
    }

    /**
     * Returns this link as URI object
     * @return this.href as URI
     * @see HALLink#href
     */
    public URI uri() {
        return URI.create(this.href);
    }

    /**
     * Returns the language of the target resource as ISO code or <code>null</code> if not set
     * @return this.hreflang
     * @see HALLink#hreflang
     */
    public String language() {
        return this.hreflang;
    }

    /**
     * Returns the URI of the specification of this link or <code>null</code> if not set
     * @return this.profile
     * @see HALLink#profile
     */
    public URI profile() {
        return this.profile;
    }

    /**
     * Returns the URI of the resource describing the reasons for deprecation of this link
     * or <code>null</code> if not set
     * @return this.deprecation
     * @see HALLink#deprecation
     */
    public URI deprecation() {
        return this.deprecation;
    }

    /**
     * this link as an instance of {@link URITemplate}
     * @return new URITemplate object constructed from this.href
     */
    public URITemplate template() {
        return URIFactory.templateUri(href);
    }

    /**
     * Expands this link as URI template with given parameters and returns the resulting URI
     * @param substitutions the template parameters to substitute
     * @return new <code>URI</code> object
     * @see URITemplate#expand(Map)
     */
    public URI expand(Map<String, ?> substitutions) {
        return this.template().expand(substitutions).toURI();
    }

    /**
     * Expands this link as URI template with given parameters and returns the resulting URI
     * @param substitutions the template parameters to substitute in given order
     * @return new <code>URI</code> object
     * @see URITemplate#expand(Object...)
     */
    public URI expand(Object... substitutions) {
        return this.template().expand(substitutions).toURI();
    }

    /**
     * Create new HAL link from given URI
     * @param href the link to be wrapped
     * @return new HALLink object with given URI as href
     */
    public static HALLink create(URI href) {
        return new HALLink(href.toString(), false, null, (URI) null, null, null,
                null, null);
    }

    HALLink resolve(HALLink src) {
        return new HALLink(src.href, src.templated, title, name, type, hreflang, profile, deprecation);
    }

}
