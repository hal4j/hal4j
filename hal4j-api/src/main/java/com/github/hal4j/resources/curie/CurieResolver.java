package com.github.hal4j.resources.curie;

import com.github.hal4j.uritemplate.URITemplate;

/**
 * Represents a function that maps HAL link namespaces to URI templates. When constructing a resource
 * the resource builder uses an instance implementing this interface to include CURIE links for each HAL link
 * with a namespace.
 *
 * Example:
 * <pre>
 * {
 *      "_links" : {
 *          "curies" : [
 *              {
 *                  "href" : "https://my.example.com/apidoc/v1/{rel}",
 *                  "name" : "my",
 *                  "template" : "true"
 *              },
 *              {
 *                  "href" : "https://docs.another.com/api/{rel}",
 *                  "name" : "another",
 *                  "template" : "true"
 *              }
*           ],
 *          "my:link" { "href": "https://my.example.com/api/v1/something" },
 *          "another:endpoint" { "href": "https://api.another.com/endpoint" },
 *          "self": { "href" : "https://api.example.com/object/123" }
 *      }
 * }
 * </pre>
 *  In this example namespace <code>my</code> wasresolved
 *  to URI template <code>https://my.example.com/apidoc/v1/{rel}</code>
 *  and namespace <code>another</code> to </code><code>https://docs.another.com/api/{rel}</code>
 *
 * @see com.github.hal4j.resources.ResourceSupport
 * @see com.github.hal4j.resources.HALLink
 */
public interface CurieResolver {

    /**
     * Name of the relation containing CURIE links
     */
    String REL_CURIES = "curies";

    /**
     * Returns CURIE link for given namespace
     * @param namespace the namespace to look up the link
     * @return CURIE link as URI template
     */
    URITemplate resolve(String namespace);

}
