import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // constants
    static final String url="https://www.atingo.be";
    static final int numberOfCrawlers = 5;
    static final int crawlingDepth = 1;
    static final String crawlStorageFolder = System.getProperty("user.dir")+"\\crawl";

    public static List<Task> collectURL(String url, String crawlStorageFolder, int numberOfCrawlers, int crawlingDepth) throws Exception {

        // Crawler configuration
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(crawlingDepth);

        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
        controller.addSeed(url);

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<AccessCrawler> factory = () -> new AccessCrawler(url);

        // Start the crawl. This is a blocking operation, meaning that your code
        // will reach the line after this only when crawling is finished.
        controller.start(factory, numberOfCrawlers);

        return AccessCrawler.list;
    }

    // serial version
    public static List<Task> testAllPages(List<Task> list) {
        for(Task task:list) {
            logger.info("Checking "+task.url);
            try {
                task.performChecks();
            } catch (IOException e) {
                logger.info("Ignoring URL "+task.url);
                // ignore errors, will not be taken into account in score
            }
        }
        return list;
    }

    // serial version
    public static double computeGlobalScore(List<Task> list) {
        int n=0;
        double score=0;
        for(Task task:list) {
            if (task.success) {
                score+=task.score;
                n++;
            }
        }
        if (n==0) return 0; // nothing
        return score/n;     // mean
    }

    // Main
    public static void main(String[] args) throws Exception {

        // step 1 - collection
        logger.info("=============================================");
        logger.info("STEP 1 - Collecting");
        List<Task> list=collectURL(url, crawlStorageFolder, numberOfCrawlers, crawlingDepth);

        // step 2 - analysis
        logger.info("=============================================");
        logger.info("STEP 2 - Analysing");
        testAllPages(list);

        // step 3 - computing global score
        logger.info("=============================================");
        logger.info("Computing global score");
        double score=computeGlobalScore(list);

        logger.info("DONE - GLOBAL SCORE: "+Math.round(score*100)+" %");
        logger.info("=============================================");
    }

}
