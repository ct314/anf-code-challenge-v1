package com.anf.core.models;

import com.day.cq.wcm.api.Page;
import org.mockito.Mock;

//import com.adobe.aem.guides.wknd.core.models.Byline;
//import com.adobe.cq.wcm.core.components.models.Image;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;

@ExtendWith(AemContextExtension.class)
public class NewsItemTest {

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


   @Test
    void testGetTitle() {
        //Not yet implemented
    }

    @Test
    void testGetAuthor() {
        //ctx.request().setAttribute();
        final String expected = "John Doe";

        ctx.currentResource("/component/newsitem");
        NewsItem ni = ctx.request().adaptTo(NewsItem.class);

        //List<NewsItem> ni = newsfeed.getNewsFeed();
        //String actual = ni.get(0).getAuthor();
        //String actual = ni.getAuthor();

        //assertEquals(expected, actual);
    }

    @Test
    void testGetDescription() {
        //ctx.request().setAttribute();
        final String expected = "This is a news description";

        ctx.currentResource("/component/newsitem");
        NewsItem ni = ctx.request().adaptTo(NewsItem.class);

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
