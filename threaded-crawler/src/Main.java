import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    static void printResults(CrawlingResult result) {
        System.out.println("Execution took: " + result.getExecutionTime().toString());
        for (String url: result.getUrls()) {
            System.out.println(url);
        }
    }

    static void printResultsIntoFile(CrawlingResult result, String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            writer.println("Execution took: " + result.getExecutionTime().toString());
            writer.println("Number of thread: " + result.getThreadCount());
            writer.println("Number of results: " + result.getUrls().size());
            for (String url: result.getUrls()) {
                writer.println(url);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //printResults(new SingleThreadedCrawler().crawl("http://books.toscrape.com", 1));
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = DateTimeFormatter.ofPattern("MM-dd_hh-mm-ss").format(localDateTime);
        printResultsIntoFile(new MultiThreadedCrawler().crawl("http://books.toscrape.com"), "result-" + time +".txt");
    }
}
