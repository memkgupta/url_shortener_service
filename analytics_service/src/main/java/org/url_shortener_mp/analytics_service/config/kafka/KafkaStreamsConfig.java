package org.url_shortener_mp.analytics_service.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.url_shortener_mp.analytics_service.dtos.URLClickEventDTO;
import org.url_shortener_mp.analytics_service.dtos.WindowedAnalytic;
import org.url_shortener_mp.analytics_service.services.LogService;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean
    public KStream<String,URLClickEventDTO> kStream(StreamsBuilder builder, LogService logService,KafkaProducer producer) {
        KStream<String, URLClickEventDTO> kStream = builder.stream("url-click-event", Consumed.with(Serdes.String(),new URLClickEventSerde()));
      KTable<Windowed<String>, WindowedAnalytic> clickCounts = kStream.groupByKey().
                windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10)))
                .aggregate(WindowedAnalytic::new,
                        (shortUrl,event,aggregate)->{

                    aggregate.setShortUrl(shortUrl);

                            aggregate.setTotalClicks(aggregate.getTotalClicks() + 1);

                            // Referrer counts
                            if(event.getReferrer()!=null){
                                aggregate.getReferrerCounts().put(event.getReferrer(),aggregate.getReferrerCounts().getOrDefault(event.getReferrer(),0L)+1);

                            }
if(event.getAgent()!=null){
    aggregate.getUserAgentCounts().put(event.getAgent().replace(".","_"),aggregate.getUserAgentCounts().getOrDefault(event.getAgent(),0L)+1);

}

                    return aggregate;
                        }
                        ,Materialized.with(Serdes.String(), new Serde<WindowedAnalytic>() {
                    @Override
                    public Serializer<WindowedAnalytic> serializer() {
                        return new Serializer<WindowedAnalytic>() {
                            ObjectMapper objectMapper = new ObjectMapper();
                            @Override
                            public byte[] serialize(String s, WindowedAnalytic windowedAnalytic) {
                                try {
return objectMapper.writeValueAsBytes(windowedAnalytic);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                    }

                    @Override
                    public Deserializer<WindowedAnalytic> deserializer() {
                        return new KafkaDeserializer<WindowedAnalytic>(WindowedAnalytic.class);
                    }

                    ObjectMapper mapper = new ObjectMapper();

                }))
              .toStream()
              .peek((k,v)->{
                  v.setWindowStart(k.window().startTime());
                  v.setWindowEnd(k.window().endTime());

              }).toTable();

       clickCounts.toStream().foreach((k,c)->{
           String key = k.key();
           System.out.println(c);
           producer.send(c);
            logService.writeLog(c);


       });

        return kStream;
    }
}
