package com.products;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Configuration
public class ApplicationConfig {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendInstant(3).toFormatter();

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
         return builder -> {
             builder.serializers(new ZonedDateTimeSerializer(formatter));
         };
     }
}
