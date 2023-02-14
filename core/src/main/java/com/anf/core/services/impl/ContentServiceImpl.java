package com.anf.core.services.impl;

import com.anf.core.services.ContentService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***Begin Code - Robert Krulich***/
import javax.jcr.Node;
import javax.jcr.Session;

import static com.anf.core.constants.CodeChallengeConstants.AGE_PATH;
import static com.anf.core.constants.CodeChallengeConstants.USER_DETAILS_ROOT_PATH;
import static com.anf.core.constants.CodeChallengeConstants.USER_DETAILS_PATH;
import static com.anf.core.constants.CodeChallengeConstants.USER_DETAILS_NODE;
import static com.anf.core.constants.CodeChallengeConstants.AGE_MIN_PROP;
import static com.anf.core.constants.CodeChallengeConstants.AGE_MAX_PROP;

@Component(immediate = true, service = ContentService.class)
public class ContentServiceImpl implements ContentService {

    private Session sess;

    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void commitUserDetails(SlingHttpServletRequest request, String lastName, String firstName, String sAge, String country) {
        // Add your logic. Modify method signature as per need.

        ResourceResolver resourceResolver = request.getResourceResolver();
        sess = resourceResolver.adaptTo(Session.class);

        log.debug("CommitUserDetails services");

        int iAge = Integer.parseInt(sAge);
        int minAge = 0;
        int maxAge = 0;
        try {
            String ns = sess.getUserID();

            Node ageNode = sess.getNode(AGE_PATH);

            if (ageNode!= null) {
                if (ageNode.hasProperty(AGE_MIN_PROP)) {
                    minAge = Integer.parseInt(ageNode.getProperty(AGE_MIN_PROP).getString());
                }
                if (ageNode.hasProperty(AGE_MAX_PROP)) {
                    maxAge = Integer.parseInt(ageNode.getProperty(AGE_MAX_PROP).getString());
                }
            }
            if (iAge >= minAge && iAge <= maxAge)
                saveUserDetails(lastName, firstName, sAge, country);
            else
                log.warn("Age is not valid for {} {} with age of {}", firstName, lastName, sAge);
        }
        catch(Exception e) {
            log.error("Error Occured"+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void saveUserDetails(String lastName, String firstName, String sAge, String country) {
        //add customer details node logic
        try {
            Node node = sess.getNode(USER_DETAILS_ROOT_PATH);

            //if the user details root path exists under the var dir
            if (node.hasNode(USER_DETAILS_NODE)) {
                node = sess.getNode(USER_DETAILS_PATH);
                createDetails(node.getPath(), lastName, firstName, sAge, country);
            }
            else { // user details root path does not exist, create first before creating user details
                log.debug("Node {} does not exist. Creating it now...",USER_DETAILS_PATH);
                node = sess.getNode(USER_DETAILS_ROOT_PATH);
                node = node.addNode(USER_DETAILS_NODE);
                sess.save();
                log.debug("Created Node {}",USER_DETAILS_PATH);
                createDetails(node.getPath(),lastName,firstName,sAge,country);
            }
        }
        catch (Exception e) {
            log.info("Error Occured: "+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void createDetails(String node,String lastName,String firstName,String sAge,String country) {
        try {
            Node n = sess.getNode(node);
            String name = lastName.toLowerCase() + "-"+firstName.toLowerCase() + "-"+sAge + "-"+country.toLowerCase();
            if (!n.hasNode(name)) {
                Node userDetails = n.addNode(name);
                userDetails.setProperty("firstName", firstName);
                userDetails.setProperty("lastName", lastName);
                userDetails.setProperty("age", sAge);
                userDetails.setProperty("country", country);
                sess.save();
            }
            else {
                log.warn("User Details {}/{} already exists",n.getPath(),name);
            }
        } catch (Exception e) {
            log.info("Error Occured: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

}
/***End Code***/