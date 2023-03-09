package com.anf.core.components.models.impl;

import com.anf.core.components.models.NewsFeed;
import com.anf.core.components.models.NewsItem;
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
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
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

    //protected static final String RESOURCE_TYPE = "anf-code-challenge/components/content/newsfeed";
    protected static final String RESOURCE_TYPE = "anf-code-challenge/components/newsfeed";
    protected static final String NEWS_TITLE = "title";
    protected static final String NEWS_AUTHOR ="author";
    protected static final String NEWS_DESCRIPTION ="description";
    protected static final String NEWS_IMAGE ="urlImage";
    protected static final String NEWS_SEARCH_DEFAULT_PATH ="/var/commerce/products/anf-code-challenge/newsData";
   /* protected static final String PUBLISH_DATE ="publishDate";
    protected static final String DEFAULT_STRING = "";
    protected static final String DEFAULT_IDEA= "No Tag Configured";
    protected static final String DEFAULT_SUMMARY = "Summary";
    protected static final String DEFAULT_PUBLISH_DATE = "Insert Publish Date";*/

    @Inject
    private Resource resource;

    @Inject
    private TagManager tagManager;

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue
    @Default(values = NEWS_SEARCH_DEFAULT_PATH)
    protected String newsFeedPath;

 /*   @ValueMapValue
    @Default(values = "")
    protected int newsResultLimit;*/

//    @Self
//    protected SlingHttpServletRequest request;

 //   @SlingObject
 //   private Resource resource;

//    @Inject
//    private SlingHttpServletRequest slingRequest;

    //@OSGiService
    //QueryService queryService;

    protected List<NewsItem> newsResult;
 //   protected int pageCount = 0;
 //   protected int start = 1;
    protected int newsCount = 0;
/*    protected int startIndex = 0;
    protected String startParam;
    protected String yearParam;
    protected String startYear;
    protected String endYear;*/

/*    private String title;
    private String author;
    private String description;
    private String image; */
    //private String publishDate;
    //private Boolean isNewsArticle = false;
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CodeChallengeConstants.DATE_PATTERN);
    //protected SimpleDateFormat simpleDateFormatOne = new SimpleDateFormat(TrexConstants.DATE_PATTERN_1);

    @PostConstruct
    protected void init() {
        log.debug("Hello from News Feed Component");
        if (StringUtils.isBlank(newsFeedPath)) {
            newsFeedPath = NEWS_SEARCH_DEFAULT_PATH;
        }

        //HttpSession session = request.getSession();
        //startParam = slingRequest.getParameter(REQUEST_START_PARAM_STRING);
        //yearParam = slingRequest.getParameter(REQUEST_YEAR_PARAM_STRING);

       /* if((StringUtils.isNotEmpty(startParam))) {
            start = Integer.parseInt(startParam);
        } else {
            startParam = "1";
            start = 1;
        }

        if((StringUtils.isNotEmpty(yearParam))) {
            startYear = yearParam;
            endYear = yearParam;
        } else {
            yearParam = String.valueOf(LocalDateTime.now().getYear());
            startYear = String.valueOf(LocalDateTime.now().getYear() - 1);
            endYear = String.valueOf(LocalDateTime.now().getYear());
        }*/

        //startIndex = newsResultLimit * (start - 1);
        newsResult = new ArrayList<>();
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Map<String, String> queryMap = getNewsFeedQueryMap();
        SearchResult searchResult = runQuery(resourceResolver, queryMap);
        searchResult.getHits().forEach(hit -> {
            try {
                Resource resultResource = hit.getResource();
                //Page page = resultResource.adaptTo(Page.class);
                Node node = resultResource.adaptTo(Node.class);
                NewsItem newsItem = createNewsModel(node);
                newsResult.add(newsItem);
            } catch (RepositoryException e) {
                log.error("Repository Exception: ", e);
            }
        });
        newsCount = Math.toIntExact(searchResult.getTotalMatches());
        log.debug("News count: {}",newsCount);
        //setPageCount(newsCount);

      /*  if (session != null && session.getAttribute(PUBLISH_YEARS) != null) {
            publishYears = (List<String>) session.getAttribute(PUBLISH_YEARS);
        } else if (session != null) {
            session.setAttribute(PUBLISH_YEARS, setPublishYears());
        }*/
    }

/*    public Boolean isNewsArticle() {
        return isNewsArticle;
    }*/

    public List<NewsItem> getNewsFeed() {
        return newsResult;
    }

 /*   public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getDescription() {
        return description;
    }

    public String getIdea() {
        return image;
    }

    @Override
    public String getPath() {
        return resource.getPath();
    }*/

    private NewsItem createNewsModel(Node node) {
        NewsItem newsItem = new NewsItem();
        //newsItem.setTitle(this.getPageTitleWithFallback(page));
        //newsItem.setTitle(page.getPath().concat(TrexConstants.HTML_STRING));

        //String[] datetime = page.getProperties().get(PUBLISH_DATE,"").split("T");
        try {
            String title = node.getProperty(NEWS_TITLE).getString();
            newsItem.setTitle(title);

            String image = node.getProperty(NEWS_IMAGE).getString();
            newsItem.setImage(image);

            String author = node.getProperty(NEWS_AUTHOR).getString();
            newsItem.setAuthor(author);

            ///Date dt = new Date();
            String date = simpleDateFormat.format(new Date());
            //date = node.getProperties(NEWS_IMAGE);
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
 /*   public String getTextDescription() {
        EditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        try {
            Reader reader = new StringReader(description);
            kit.read(reader, doc, 0);
            return doc.getText(0, doc.getLength()).replaceAll("\u00a0"," ");
        } catch (Exception e) {
            return "";
        }
    }*/

 /*   @Override
    public boolean isEmpty() {
        return false;
    }*/

    private Map<String, String> getNewsFeedQueryMap() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", newsFeedPath);
        //queryMap.put("daterange.property", JCR_PUBLISH_DATE);
        //queryMap.put("daterange.lowerBound", startYear + "-01-01");
        //queryMap.put("daterange.lowerOperation", ">=");
        //queryMap.put("daterange.upperBound", endYear + "-12-31");
        //queryMap.put("daterange.upperOperation", "<=");
        //queryMap.put("type", NameConstants.NT_.NT_PAGE);
        //queryMap.put("p.offset", String.valueOf(startIndex));
        //queryMap.put("p.limit", String.valueOf(newsResultLimit));
        //queryMap.put("orderby", ORDER_BY_PUBLISH_DATE);
        //queryMap.put("orderby.sort", TrexConstants.ORDER_DESCENDING);
        return queryMap;
    }
    public boolean isEmpty() {
        return (StringUtils.isBlank(newsFeedPath) || newsCount == 0);
    }
}
