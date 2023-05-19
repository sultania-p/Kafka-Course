package io.conduktor.demos.kafka;


import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerExample {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerExample.class.getSimpleName());

    public static void main(String[] args) {

        log.info("I am a kafka Producer!");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"4sgMZTMIeVoq7zFam4vIhN\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI0c2dNWlRNSWVWb3E3ekZhbTR2SWhOIiwib3JnYW5pemF0aW9uSWQiOjcwMzkwLCJ1c2VySWQiOjgxNDQwLCJmb3JFeHBpcmF0aW9uQ2hlY2siOiI5NWZiNGE0Mi0yZGQ2LTRkNGYtYmJmZi1kZmQxNWQxZTg1OWEifX0.qwZdZrEvfI1wLxeg3Rjp5e0bYlTAz75b-FLJKh5LNQU\";");
        properties.setProperty("sasl.mechanism", "PLAIN");


        // set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // create Producer Record
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>("demo_java", "New Message today!");

        // send data
        producer.send(producerRecord);

        // tell the producer to send all the data and block until done - synchronous operation
        producer.flush();

        // flush and close the producer
        producer.close();



    }
}
