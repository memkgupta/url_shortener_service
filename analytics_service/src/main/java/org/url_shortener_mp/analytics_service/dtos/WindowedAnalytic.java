package org.url_shortener_mp.analytics_service.dtos;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class WindowedAnalytic {
    private String shortUrl;
    private Instant windowStart;
    private Instant windowEnd;
    private long totalClicks;
    private Map<String, Long> referrerCounts;
    private Map<String, Long> userAgentCounts;

    @Override
    public String toString() {
        return "WindowedAnalytic{" +
                "shortUrl='" + shortUrl + '\'' +
                ", windowStart=" + windowStart +
                ", windowEnd=" + windowEnd +
                ", totalClicks=" + totalClicks +
                ", referrerCounts=" + referrerCounts +
                ", userAgentCounts=" + userAgentCounts +
                '}';
    }

    public WindowedAnalytic() {
        referrerCounts = new HashMap<>();
        userAgentCounts = new HashMap<>();
    }




    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Instant getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(Instant windowStart) {
        this.windowStart = windowStart;
    }

    public Instant getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Instant windowEnd) {
        this.windowEnd = windowEnd;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public Map<String, Long> getReferrerCounts() {
        return referrerCounts;
    }

    public void setReferrerCounts(Map<String, Long> referrerCounts) {
        this.referrerCounts = referrerCounts;
    }

    public Map<String, Long> getUserAgentCounts() {
        return userAgentCounts;
    }

    public void setUserAgentCounts(Map<String, Long> userAgentCounts) {
        this.userAgentCounts = userAgentCounts;
    }
}
