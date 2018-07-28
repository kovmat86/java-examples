import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Crawler {
    Set<String> alreadyVisited = new HashSet<>();

    CrawlingResult crawl(String url) throws IOException, InterruptedException;

    static List<String> extractUrls(String html) {
        List<String> result = new LinkedList<>();
        Pattern pattern = Pattern.compile("<a href=\"(.*?)\"");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            result.add(matcher.group(1)/*.replaceAll("/$", "")*/);
        }
        return result.stream().distinct().collect(Collectors.toList());
    }

    static String getTargetUrl(String baseUrl, String url) {
        Pattern pattern = Pattern.compile("http");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? url : baseUrl + "/" + url;
    }
}
