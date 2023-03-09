/***Begin Code - Robert Krulich***/
package com.anf.core.listeners;

import com.day.cq.wcm.api.NameConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Map;

import static com.anf.core.constants.CodeChallengeConstants.RESOURCE_RESOLVER_SITES_SERVICE_PARAMS;
import static com.anf.core.constants.CodeChallengeConstants.SITES_SERVICE_ACCOUNT_IDENTIFIER;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;

@Component(immediate = true, service = EventListener.class)
public class PageCreatedListener implements EventListener {
    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    SlingRepository repository;

    @Reference protected ResourceResolverFactory resourceResolverFactory;

    @Activate
    public void activate(ComponentContext context) throws Exception {
        log.info("Activating ANF Code Challenge Page Created Listener");
        try {
            Session adminSession = repository.loginService(SITES_SERVICE_ACCOUNT_IDENTIFIER, null);
            adminSession.getWorkspace().getObservationManager().addEventListener(
                    this, //handler
                    Event.NODE_ADDED,
                    "/content/anf-code-challenge/us/en", //path
                    true, //is Deep?
                    null, //uuids filter
                    new String[]{NameConstants.NT_PAGE}, //nodetypes filter - only want to catch moves on pages
                    false);
        } catch (RepositoryException e) {
            log.error("Unable to register session for PageMoveListener, listener will likely never be invoked", e);
        }
    }

    @Override
    public void onEvent(EventIterator eventIterator) {
        log.debug("Hello from onEvent method for listener");
        try {
            while(eventIterator.hasNext()) {
                String eventPath = eventIterator.nextEvent().getPath();
                log.debug("eventPath={}",eventPath);
                if(null != eventPath && eventPath.endsWith(JCR_CONTENT)) {
                    ResourceResolver resourceResolver = resourceResolverFactory.getResourceResolver(RESOURCE_RESOLVER_SITES_SERVICE_PARAMS);
                    Session session = resourceResolver.adaptTo(Session.class);
                    Resource resource = resourceResolver.getResource(eventPath);
                    if(null != resource) {
                        log.debug("Setting pageCreated property");
                        Node jcrNode = resource.adaptTo(Node.class);
                        jcrNode.setProperty("pageCreated", true);
                        session.save();
                    }
                }
            }
        } catch (RepositoryException e) {
            log.error("Repository Exception inside onEvent: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Exception occured inside onEvent: {}", e.getMessage(), e);
        }
    }

    /**
     * Method to get resource resolver
     * @return
     */
    private ResourceResolver getResourceResolver() {

        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, SITES_SERVICE_ACCOUNT_IDENTIFIER);

        ResourceResolver resourceResolver = null;
        try
        {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
            return resourceResolver;
        }
        catch (LoginException e)
        {
            log.error("Error getting resource resolver",e);
        }
        return null;
    }
}
/***End Code***/
