/***Begin Code - Robert Krulich***/
package com.anf.core.models.impl;

import com.adobe.aem.formsndocuments.util.FMConstants;
import com.anf.core.models.NewsFeed;
import com.anf.core.models.NewsItem;
import com.anf.core.constants.CodeChallengeConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.*;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {NewsFeed.class},
        resourceType = {NewsFeedImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class NewsFeedImpl implements NewsFeed {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected static final String RESOURCE_TYPE = "anf-code-challenge/components/newsfeed";
    protected static final String NEWS_TITLE = "title";
    protected static final String NEWS_AUTHOR ="author";
    protected static final String NEWS_DESCRIPTION ="description";
    protected static final String NEWS_IMAGE ="urlImage";
    protected static final String NEWS_SEARCH_DEFAULT_PATH ="/var/commerce/products/anf-code-challenge/newsData";

    @Inject
    private Resource resource;

    @Inject
    private TagManager tagManager;

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue
    @Default(values = NEWS_SEARCH_DEFAULT_PATH)
    protected String newsFeedPath;

    protected List<NewsItem> newsResult;

    protected int newsCount = 0;

    protected int newsResultLimit = 50;

    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CodeChallengeConstants.DATE_PATTERN);

    @PostConstruct
    protected void init() {
        log.debug("Hello from News Feed Component");
        if (StringUtils.isBlank(newsFeedPath)) {
            newsFeedPath = NEWS_SEARCH_DEFAULT_PATH;
        }

        newsResult = new ArrayList<>();
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Map<String, String> queryMap = getNewsFeedQueryMap();
        SearchResult searchResult = runQuery(resourceResolver, queryMap);
        searchResult.getHits().forEach(hit -> {
            try {
                Resource resultResource = hit.getResource();
                Node node = resultResource.adaptTo(Node.class);
                NewsItem newsItem = createNewsModel(node);
                newsResult.add(newsItem);
            } catch (RepositoryException e) {
                log.error("Repository Exception: ", e);
            }
        });
        newsCount = Math.toIntExact(searchResult.getTotalMatches());
        log.debug("News count: {}",newsCount);

    }

    public List<NewsItem> getNewsFeed() {
        return newsResult;
    }

    private NewsItem createNewsModel(Node node) {
        NewsItem newsItem = new NewsItem();

        try {
            String title = node.getProperty(NEWS_TITLE).getString();
            newsItem.setTitle(title);

            String image = node.getProperty(NEWS_IMAGE).getString();
            newsItem.setImage(image);

            String author = node.getProperty(NEWS_AUTHOR).getString();
            newsItem.setAuthor(author);

            String date = simpleDateFormat.format(new Date());
            newsItem.setDate(date);

            String description = node.getProperty(NEWS_DESCRIPTION).getString();
            newsItem.setDescription(description);
        }
        catch (Exception e) {
            log.error("Error building News Feed Model");
            e.printStackTrace();
        }

        return newsItem;
    }

    private SearchResult runQuery(ResourceResolver resourceResolver, Map<String, String> queryMap) {
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);

        Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
        return query.getResult();
    }

    private Map<String, String> getNewsFeedQueryMap() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", newsFeedPath);
        queryMap.put("type", FMConstants.NT_UNSTRUCTURED_NODETYPE);
        queryMap.put("p.limit", String.valueOf(-1));
        queryMap.put("orderby", NEWS_TITLE);
        //queryMap.put("orderby.sort", "desc";
        return queryMap;
    }
    public boolean isEmpty() {
        return (StringUtils.isBlank(newsFeedPath) || newsCount == 0);
    }
}
/***End Code***/
