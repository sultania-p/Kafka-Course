package io.conduktor.demos.kafka.wikimedia.kafka.wikimedia.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer!");

        // create Producer Properties
        Properties properties = new Properties();

        // connect to local host
//        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        // connect to Conduktor Playground
        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"4sgMZTMIeVoq7zFam4vIhN\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI0c2dNWlRNSWVWb3E3ekZhbTR2SWhOIiwib3JnYW5pemF0aW9uSWQiOjcwMzkwLCJ1c2VySWQiOjgxNDQwLCJmb3JFeHBpcmF0aW9uQ2hlY2siOiI5NWZiNGE0Mi0yZGQ2LTRkNGYtYmJmZi1kZmQxNWQxZTg1OWEifX0.qwZdZrEvfI1wLxeg3Rjp5e0bYlTAz75b-FLJKh5LNQU\";");
        properties.setProperty("sasl.mechanism", "PLAIN");

        // set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // change batch size (default 16kb)
//        properties.setProperty("batch.size", "400");

        // never do in Production set partition type
//        properties.setProperty("partition.class", RoundRobinPartitioner.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // send multiple messages
        for (int j=0; j<10; j++) {

            for (int i=0; i<30; i++) {

                // create Producer Record
                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<>("demo_java", "Message number - " + i);

                // send data
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        // executes everytime a record was successfully sent or an exception is thrown
                        if (e == null) {
                            // the record was successfully sent
                            log.info("Received new metadata \n" +
                                    "Topic: " + metadata.topic() + "\n" +
                                    "Partition: " + metadata.partition() + "\n" +
                                    "Offset: " + metadata.offset() + "\n" +
                                    "Timestamp: " + metadata.timestamp());
                        } else {
                            log.info("Error while producing", e);
                        }
                    }
                });
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        // tell the producer to send all the data and block until done - synchronous operation
        producer.flush();

        // flush and close the producer
        producer.close();
    }
}
