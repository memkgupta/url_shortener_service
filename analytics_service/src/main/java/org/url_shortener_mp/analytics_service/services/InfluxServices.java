package org.url_shortener_mp.analytics_service.services;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.stereotype.Service;
import org.url_shortener_mp.analytics_service.dtos.RangeAnalyticData;
import org.url_shortener_mp.analytics_service.dtos.WindowedAnalytic;
import org.url_shortener_mp.analytics_service.utils.FluxQueryBuilder;

import java.sql.Timestamp;
import java.util.*;

@Service
public class InfluxServices {
    private final InfluxDBClient influxDBClient;

    public InfluxServices(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    public void writeLog(WindowedAnalytic analytic){


        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        Point point = Point.measurement("clicks")
                .addTag("url_id", analytic.getShortUrl())
                .addField("clicks", analytic.getTotalClicks());
        StringBuilder agentBreakdown = new StringBuilder();
        StringBuilder referrerBreakdown = new StringBuilder();
        for (Map.Entry<String, Long> entry : analytic.getUserAgentCounts().entrySet()) {

            if (!agentBreakdown.isEmpty()) {
                agentBreakdown.append(",");
            }
            agentBreakdown.append(entry.getKey()).append(":").append(entry.getValue());
                    point.addField("agent_"+entry.getKey(),entry.getValue()); // make it a TAG!

        }
        for (Map.Entry<String, Long> entry : analytic.getReferrerCounts().entrySet()) {
            if (!referrerBreakdown.isEmpty()) {
               referrerBreakdown.append(",");
            }
            referrerBreakdown.append(entry.getKey()).append(":").append(entry.getValue());
                    point.addField("referrer_"+entry.getKey(), entry.getValue());// TAG for filtering

        }
        point.addField("agent_clicks",agentBreakdown.toString());
        point.addField("referrer_clicks",referrerBreakdown.toString());
        writeApi.writePoint(point);
    }
    public List<RangeAnalyticData> readDataOfRange(Map<String,String> params, String id){
        try{
            FluxQueryBuilder builder = new FluxQueryBuilder();
            Map<String,String> aggregateParams = new HashMap<>();
            builder.fromBucket("url_shortener");
            builder.filter("url_id",id);
            builder.filter("_measurement","clicks");
            for(var e :params.entrySet())
            {
                switch(e.getKey()){

                    case "agent":
                        builder.filter("agent", e.getValue());
                        break;
                    case "referrer":
                        builder.filter("referrer", e.getValue());
                        break;
                    case "start_time":
                        builder.range(e.getValue(),"now()");
                        break;

                    case "window":
                        String window[] = params.get(e.getKey()).split("-");
                        String windowType = window[1];

                        String windowFn = window[2];
                        int windowSize = Integer.parseInt(window[0]);
                        aggregateParams.put("every",windowSize+windowType);
                        aggregateParams.put("fn",windowFn);
                        builder.aggregateWindow(aggregateParams.get("every"),aggregateParams.get("fn"));

                }
            }

builder.yield("clicks_stats");
            String flux = "countStream = from(bucket: \"url\")\n" +
                    "  |> range(start:"+params.get("start_time")+" )\n" +
                    "  |> filter(fn: (r) => r._measurement == \"clicks\" and r._field == \"clicks\")\n" +
                    "  |> aggregateWindow(every: "+aggregateParams.get("every")+", fn: "+aggregateParams.get("fn")+", createEmpty: false)\n" +
                    "\n" +
                    "stringStream = from(bucket: \"url\")\n" +
                    "  |> range(start:"+params.get("start_time")+" )\n"  +
                    "  |> filter(fn: (r) => r._measurement == \"clicks\" and (r._field == \"agent_clicks\" or r._field == \"referrer_clicks\"))\n" +
                    "  |> aggregateWindow(every: "+aggregateParams.get("every")+", fn:last, createEmpty: false)\n" +
                    "\n" +
                    "union(tables: [countStream, stringStream])\n" +
                    "  |> pivot(rowKey:[\"_time\"], columnKey:[\"_field\"], valueColumn:\"_value\")";
            System.out.println(flux);
            List<FluxTable> tables = influxDBClient.getQueryApi().query(flux,"mk");
            List<RangeAnalyticData> list = new ArrayList<>();
            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    RangeAnalyticData data = new RangeAnalyticData();

                    if(record.getValueByKey("clicks") != null){

                        data.setClicks(Double.parseDouble(record.getValueByKey("clicks").toString()) );

                        }
                    Object agentMap = record.getValueByKey("agent_clicks");
                    HashMap<String,Long> mao = new HashMap<>();
                    if(agentMap!=null){
                        String converted = (String)agentMap;
                        List.of(converted.split(",")).stream()
                                .forEach((s)->{
                                    String[] split = s.split(":");
                                    System.out.println(Arrays.toString(split));
//                                    mao.put(split[0],Long.parseLong(split[1]));
                                });

                    }
                    data.setAgentMap(mao);
                    list.add(data);
                }
            }
            return list;
        }
        catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }
}
