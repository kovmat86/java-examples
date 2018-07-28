import java.time.Duration;
import java.util.List;

public class CrawlingResult {
    List<String> urls;
    Duration executionTime;
    int threadCount;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public CrawlingResult(List<String> urls, Duration executionTime, int threadCount) {
        this.urls = urls;
        this.executionTime = executionTime;
        this.threadCount = threadCount;
    }

    public CrawlingResult() {
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Duration executionTime) {
        this.executionTime = executionTime;
    }
}
