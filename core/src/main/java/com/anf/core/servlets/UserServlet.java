/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anf.core.servlets;

import com.adobe.xfa.ut.StringUtils;
import com.anf.core.services.ContentService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component(service = { Servlet.class })
@SlingServletPaths(
        value = "/bin/saveUserDetails"
)
public class UserServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    private ContentService contentService;

    /***Begin Code - Robert Krulich***/

    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {

            // Read the parameters sent from the request
            String lastName = request.getParameter("lastName");
            String firstName = request.getParameter("firstName");
            String age = request.getParameter("age");
            String country = request.getParameter("country");

            log.debug("lastName: " + lastName);
            log.debug("firstName: " + firstName);
            log.debug("age: " + age);
            log.debug("country: " + country);

            if (!StringUtils.isEmpty(lastName) && !StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(age)) {
                // Make use of ContentService to write the business logic
                contentService.commitUserDetails(request, lastName, firstName, age, country);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(Integer.toString(HttpServletResponse.SC_OK));
            }
            else {
                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                resp.getWriter().write(Integer.toString(HttpServletResponse.SC_NOT_MODIFIED));
            }
            log.debug("user details response status: " + resp.getStatus());
        }
        catch(Exception e)
        {
            log.error("Error Occured");
            log.error("SERVLET status: " + resp.getStatus());
            e.printStackTrace();
        }
    }

    private String getCountry(Resource context) {
        return Optional.ofNullable(context)
                .map(contextResource -> contextResource.getValueMap().get("country", String.class))
                .orElse(null);
    }
}

/***End Code***/
