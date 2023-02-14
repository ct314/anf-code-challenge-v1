/***Begin Code - Robert Krulich***/
package com.anf.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

public interface ContentService {
	void commitUserDetails(SlingHttpServletRequest request, String lastName, String firstName, String sAge, String country);
}
/***End Code***/