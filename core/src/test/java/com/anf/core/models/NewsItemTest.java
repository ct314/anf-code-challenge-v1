package com.anf.core.models;

import com.anf.core.constants.CodeChallengeConstants;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(AemContextExtension.class)
public class NewsItemTest {

    private final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() throws Exception {
        ctx.addModelsForClasses(NewsItem.class);
        ctx.load().json("/com/anf/core/models/NewsItemTest.json", "/component");
    }

    @Test
    void testSetGetTitle() {
        final String expected = "John Doe";

        ctx.currentResource("/component/newsitem");

        NewsItem ni = new NewsItem();
        ni.setTitle(expected);
        String actual = ni.getTitle();

        assertEquals(expected, actual);
    }

    @Test
    void testSetGetAuthor() {
        final String expected = "John Doe";

        ctx.currentResource("/component/newsitem");
        NewsItem ni = new NewsItem();
        ni.setAuthor(expected);
        String actual = ni.getAuthor();

        assertEquals(expected, actual);
    }

    @Test
    void testSetGetDescription() {
        final String expected = "This is a news description";

        ctx.currentResource("/component/newsitem");
        NewsItem ni = new NewsItem();
        ni.setDescription(expected);

        String actual = ni.getDescription();

        assertEquals(expected, actual);
    }

    @Test
    void testSetGetImage() {
        final String expected = "http//www.url.com/to-some/image";

        ctx.currentResource("/component/newsitem");

        NewsItem ni = new NewsItem();
        ni.setImage(expected);

        String actual = ni.getImage();

        assertEquals(expected, actual);
    }

    @Test
    void testSetGetDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CodeChallengeConstants.DATE_PATTERN);
        final String expected = simpleDateFormat.format(new Date());

        ctx.currentResource("/component/newsitem");

        NewsItem ni = new NewsItem();
        ni.setDate(expected);

        String actual = ni.getDate();

        assertEquals(expected, actual);
    }
}
