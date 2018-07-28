import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class SingleThreadedCrawler implements Crawler {

    final int depth = 5;

    @Override
    public CrawlingResult crawl(String url) throws IOException {
        Instant startTime = Instant.now();
        List<String> resultList = crawlSite(url, url, depth);
        Instant endTime = Instant.now();
        return new CrawlingResult(resultList, Duration.between(startTime, endTime), 1);
    }

    List<String> crawlSite(String baseUrl, String url, int depth) throws IOException {
        String targetUrl = Crawler.getTargetUrl(baseUrl, url);

        if (depth <= 0 || alreadyVisited.contains(targetUrl)) {
            return Arrays.asList(targetUrl);
        }
        depth--;
        alreadyVisited.add(targetUrl);
        System.out.println("crawling site: " + targetUrl);

        try {
            String html = Jsoup.connect(targetUrl).get().html();
            List<String> urls = Crawler.extractUrls(html);
            List<String> resultList = new LinkedList<>();
            for (String iterateUrl : urls) {
                resultList.addAll(crawlSite(baseUrl, iterateUrl, depth));
            }
            return resultList;
        }
        catch (Exception e) {
            return Arrays.asList();
        }
    }
}
