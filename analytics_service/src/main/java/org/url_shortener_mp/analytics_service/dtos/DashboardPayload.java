package org.url_shortener_mp.analytics_service.dtos;

import java.util.Map;

public class DashboardPayload {
    private long totalClicks;
    private Map<String,Long> agentMap;
}
