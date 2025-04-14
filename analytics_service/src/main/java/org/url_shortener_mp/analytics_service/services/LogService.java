package org.url_shortener_mp.analytics_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.url_shortener_mp.analytics_service.dtos.DailyClicks;
import org.url_shortener_mp.analytics_service.dtos.DashboardPayload;
import org.url_shortener_mp.analytics_service.dtos.RangeAnalyticData;
import org.url_shortener_mp.analytics_service.dtos.WindowedAnalytic;
import org.url_shortener_mp.analytics_service.entities.Entry;
import org.url_shortener_mp.analytics_service.repositories.EntryRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogService {
    private final EntryRepository entryRepository;
    private final MongoTemplate mongoTemplate;
    public LogService(EntryRepository entryRepository,MongoTemplate mongoTemplate) {
        this.entryRepository = entryRepository;
        this.mongoTemplate = mongoTemplate;
    }
public void writeLog(WindowedAnalytic window) {
    try{
        Entry entry = new Entry();
        entry.setClicks(window.getTotalClicks());
        entry.setEndTime(
               window.getWindowEnd()
        );
        entry.setStartTime(
                window.getWindowStart()
        );
        entry.setTimestamp(new Timestamp(System.currentTimeMillis()));
        entry.setUrl(window.getShortUrl());
        entry.setAgentMap(window.getUserAgentCounts());
        entry.setGeoMap(new HashMap<>());
        entry.setDeviceMap(new HashMap<>());
        System.out.println(entry.toString());
                entryRepository.save(entry);
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

public List<DailyClicks>  getAnalytics(Map<String,String> params)
{

    String url_id = params.get("url_id");

//    System.out.println(String.valueOf(Instant.parse(params.get("start_time"))));
    Timestamp startTime = Timestamp.valueOf(params.get("start_time"));

    Date endTime = Date.from(Instant.now());
if(params.get("end_time") != null){
    endTime = Timestamp.valueOf(String.valueOf(Instant.parse(params.get("end_time"))));
}
    Date timePeriodAgo = Date.from(startTime.toInstant());
    String windowUnit = params.get("window_unit");
    Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(
                    Criteria.where("url").is(url_id).andOperator(
                            Criteria.where("startTime").gte(timePeriodAgo).andOperator(
                                    Criteria.where("endTime").lte(endTime)
                            )
                    )
            ),
            Aggregation.project("clicks")
                    .andExpression("{"+
                            "$dateTrunc:{date:\"$startTime\", unit: \""+windowUnit+"\" }" +"}")
                    .as("interval"),
            Aggregation.group("interval").sum("clicks").as("totalClicks"),
            Aggregation.sort(Sort.Direction.ASC,"_id")
    );

    AggregationResults<DailyClicks> results = mongoTemplate.aggregate(
            aggregation, "entry", DailyClicks.class
    );

    return results.getMappedResults();
}
    public DashboardPayload getDashboard(String urlId){
        try{
                Aggregation aggregation = Aggregation.newAggregation(
                        Aggregation.match(
                                Criteria.where("url").is(urlId)
                        )

                );
                return new DashboardPayload();
        }
        catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Some error occured");
        }
    }
}

enum WindowUnitEnum{
    DAY,
    MINUTE,
    HOUR,
    MONTH,
    WEEK
}