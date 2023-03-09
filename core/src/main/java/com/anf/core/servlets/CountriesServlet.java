/***Begin Code - Robert Krulich***/
package com.anf.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.json.JSONArray;
import org.json.JSONException;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static com.anf.core.constants.CodeChallengeConstants.COUNTRY_LIST;
import static com.anf.core.constants.CodeChallengeConstants.COUNTRY_LIST_PATH;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_ID + "=" + "Countries Servlet",
                SLING_SERVLET_RESOURCE_TYPES + "=" + "/apps/anf-code-challenge/components/form/country/list"
        }
)
public class CountriesServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final String TAG = CountriesServlet.class.getSimpleName();
    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        try {
            // Getting resource resolver from the current request
            ResourceResolver resourceResolver = req.getResourceResolver();

            Resource jsonResource = resourceResolver.getResource(COUNTRY_LIST_PATH);

            Asset asset = DamUtil.resolveToAsset(jsonResource); // Converting this json resource to an Asset

            Rendition originalAsset = Objects.requireNonNull(asset).getOriginal();  // Get the original rendition

            InputStream content = Objects.requireNonNull(originalAsset).adaptTo(InputStream.class);

            StringBuilder jsonContent = new StringBuilder(); // Read all the data in the json file as a string
            BufferedReader jsonReader = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(content), StandardCharsets.UTF_8));

            String line;
            while ((line = jsonReader.readLine()) != null) { // Loop through each line
                jsonContent.append(line);
                log.debug("line is: "+line);
            }
            // Remove {, }, and " from the string
            String[] keyValArr = jsonContent.toString().replaceAll("[\\{\\\"}]", "").split(",");

            JSONArray jsonArray = new JSONArray(keyValArr); //(jsonContent.toString());
            Map<String, String> data = new TreeMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] listItem = jsonArray.get(i).toString().split(":");
                data.putIfAbsent(listItem[0],listItem[1]); // Add country object to data map
            }
            // Creating the data source object
            @SuppressWarnings({"unchecked", "rawtypes"})
            DataSource ds =
                    new SimpleDataSource(
                            new TransformIterator<>(
                                    data.keySet().iterator(),
                                    (Transformer) item -> {
                String itemValue = (String) item;
                log.debug("------------itemValue={}",itemValue);
                log.debug("------------value for data={}",data.get(itemValue));
                ValueMap vm = new ValueMapDecorator(new HashMap<>());
                vm.put("text", itemValue);
                vm.put("value", data.get(itemValue));
                return new ValueMapResource(resourceResolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, vm);
            }));
            req.setAttribute(DataSource.class.getName(), ds);
        } catch (IOException | JSONException e) {
            log.error("{}: exception occurred: {}", TAG, e.getMessage());
        }
    }

    private Resource getJsonResource(ResourceResolver resourceResolver, String dropdownSelector) {
        Resource jsonResource;
        if (dropdownSelector.equals(COUNTRY_LIST)) {
            jsonResource = resourceResolver.getResource(COUNTRY_LIST_PATH);
        }
        else {
            jsonResource = resourceResolver.getResource(COUNTRY_LIST_PATH);
        }

        return jsonResource;
    }
}
/***End Code***/
