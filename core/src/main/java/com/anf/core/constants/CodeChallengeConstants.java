package com.anf.core.constants;

import org.apache.sling.api.resource.ResourceResolverFactory;

import java.util.Collections;
import java.util.Map;

/***Begin Code - Robert Krulich***/

public class CodeChallengeConstants {
    public static final String USER_DETAILS_ROOT_PATH = "/var";
    public static final String USER_DETAILS_PATH = "/var/anf-code-challenge";
    public static final String USER_DETAILS_NODE = "anf-code-challenge";
    public static final String AGE_PATH = "/etc/age";
    public static final String AGE_MIN_PROP = "minAge";
    public static final String AGE_MAX_PROP = "maxAge";

    // Dynamic country datasource
    public static final String DATASOURCE = "datasource";
    public static final String DROPDOWN_SELECTOR = "dropdownSelector";
    public static final String COUNTRY_LIST = "countryList";
    public static final String COUNTRY_LIST_PATH = "/content/dam/anf-code-challenge/exercise-1/countries.json";

    public static final String DATE_PATTERN = "dd.MM.yyyy";

    // needed for allowing a resource resolver admin user who has permissions
    // for things that a standard anonymous user might not have access to
    //public static final String SITES_SERVICE_ACCOUNT_IDENTIFIER = "anf-cc-sys-user";
    public static final String SITES_SERVICE_ACCOUNT_IDENTIFIER = "anf-cc-ReadWriteService";
    public static final Map<String, Object> RESOURCE_RESOLVER_SITES_SERVICE_PARAMS = Collections
            .singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) SITES_SERVICE_ACCOUNT_IDENTIFIER);
}

/***End Code***/
