package com.anf.core.models.impl;

import com.anf.core.models.HelloWorldModel;
import com.anf.core.models.NewsItem;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AemContextExtension.class)
public class NewsFeedImplTest {

    private final AemContext ctx = new AemContext();

    private NewsItem newsitem;

    //private NewsFeed unitTest = new NewsFeed();

    @Mock
    private HelloWorldModel hwm;

    @Mock
    private ModelFactory modelFactory;

    private Page page;
    private Resource resource;

    @BeforeEach
    void setUp() throws Exception {
        //page = ctx.create().page("/var/newsitem");
        //resource = ctx.create().resource(page, "hello",
        //        "author", "John Doe");
        ctx.addModelsForClasses(NewsItem.class);
        ctx.load().json("/com/anf/core/models/NewsItemTest.json", "/component");
        //ctx.currentResource("/content")/
        /*lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()), any(Resource.class), eq(HelloWorldModel.class)))
                .thenReturn(hwm);

        ctx.registerService(ModelFactory.class, modelFactory, org.osgi.framework.Constants.SERVICE_RANKING,
                Integer.MAX_VALUE);*/
    }

  /*  @PostConstruct
    private void init() {
        image = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Image.class);
    } */

/*    @Test
    void testGetDescription() {
        fail("Not yet implemented");
    }*/

/*    @Test
    void testGetAuthor() {
        fail("Not yet implemented");
    } */

    @Test
    void testGetNewsFeed() {
        //ctx.request().setAttribute();
        final String expected = "John Doe";

        ctx.currentResource("/component/newsitem");
        NewsFeedImpl newsfeed = ctx.request().adaptTo(NewsFeedImpl.class);
        //List<NewsItem> ni = newsfeed.getNewsFeed();

        //ni.get(0).setAuthor(ctx.currentResource().getValueMap().get("author"));

        // create resource
       ///// ctx.create().resource("/component/newsFeed", ImmutableMap.<String, Object>builder()
        //        .put("prop1", "value1")
       ///         .put("prop2", "value2")
      //          .build());

        //List<NewsItem> ni = newsfeed.getNewsFeed();
        //String actual = ni.get(0).getAuthor();
        //String actual = ni.get(0).getAuthor();

        //assertEquals(expected, actual);
    }

    @Test
    void testGetDescription() {
        //ctx.request().setAttribute();
        final String expected = "This is a news description";

        ctx.currentResource("/component/newsitem");
        NewsItem ni = ctx.request().adaptTo(NewsItem.class);

        //String actual = ni.getDescription();
        //System.out.println("actual"+actual);

        //List<NewsItem> ni = newsfeed.getNewsFeed();
        //String actual = ni.get(0).getAuthor();
        //String actual = ni.getAuthor();

        //assertEquals(expected, actual);
    }

/*    @Test
    void testIsEmpty() {
        fail("Not yet implemented");
    } */
}
