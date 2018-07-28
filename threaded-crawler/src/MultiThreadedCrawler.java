import org.jsoup.Jsoup;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.stream.Collectors.toList;

public class MultiThreadedCrawler implements Crawler {

    final int maxNumberOfPages = 500;
    final int numberOfThreads = 8;

    Set<String> alreadyVisited = Collections.newSetFromMap(new ConcurrentHashMap<>());
    BlockingQueue<String> waitingForVisit = new LinkedBlockingQueue<>();
    int counter = 0;
    String baseUrl;

    @Override
    public CrawlingResult crawl(String url){
        Instant startTime = Instant.now();
        baseUrl = url;
        try {
            waitingForVisit.put(url);
            List<Thread> crawlerThreads = new ArrayList<>();
            for (int i = 0;i < numberOfThreads;i++) {
                Thread crawlerThread = new CrawlerThread(i);
                crawlerThread.start();
                crawlerThreads.add(crawlerThread);
            }

            for (Thread thread: crawlerThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> resultList = alreadyVisited.stream().collect(toList());
        Instant endTime = Instant.now();
        return new CrawlingResult(resultList, Duration.between(startTime, endTime), numberOfThreads);
    }

    class CrawlerThread extends Thread {
        final boolean isConsoleLog = false;
        int threadId;

        CrawlerThread(int threadId) {
            this.threadId = threadId;
        }

        void log(String message) {
            if (isConsoleLog) {
                System.out.println(message);
            }
        }

        public void run() {
            System.out.println(this.threadId + ". thread started");
            //TODO: better condition to avoid infinite loop
            while (counter < maxNumberOfPages) {
                String urlToVisit = null;
                try {
                    urlToVisit = waitingForVisit.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
                String targetUrl = Crawler.getTargetUrl(baseUrl, urlToVisit);
                if (alreadyVisited.contains(urlToVisit)) {
                    continue;
                }
                alreadyVisited.add(targetUrl);
                counter++;
                System.out.println(counter);
                log(this.threadId + ". thread crawling site: " + targetUrl);
                try {
                    String html = Jsoup.connect(targetUrl).get().html();
                    List<String> urls = Crawler.extractUrls(html);
                    for (String iterateUrl : urls) {
                        waitingForVisit.put(iterateUrl);
                    }
                } catch (Exception e) {
                   log(this.threadId + ". thread Cannot resolve: " + targetUrl);
                }
            }
            System.out.println(this.threadId + ". thread finished");
        }
    }
}
