package io.conduktor.demos.kafka.wikimedia.kafka.wikimedia.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoWithShutdown {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Consumer!");

        String groupId = "my-java-application";
        String topic = "demo_java";

        // create Consumer Properties
        Properties properties = new Properties();

        // connect to local host
//        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        // connect to Conduktor Playground
        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"4sgMZTMIeVoq7zFam4vIhN\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI0c2dNWlRNSWVWb3E3ekZhbTR2SWhOIiwib3JnYW5pemF0aW9uSWQiOjcwMzkwLCJ1c2VySWQiOjgxNDQwLCJmb3JFeHBpcmF0aW9uQ2hlY2siOiI5NWZiNGE0Mi0yZGQ2LTRkNGYtYmJmZi1kZmQxNWQxZTg1OWEifX0.qwZdZrEvfI1wLxeg3Rjp5e0bYlTAz75b-FLJKh5LNQU\";");
        properties.setProperty("sasl.mechanism", "PLAIN");

        // set Consumer Properties
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        // create the Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // get a reference to main thread
        final Thread mainThread = Thread.currentThread();

        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
           public void run() {
               log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
               consumer.wakeup();

               try {
                   mainThread.join();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });

        try {

            // subscribe to topic
            consumer.subscribe(Arrays.asList(topic));

            // poll for data
            while (true) {

//                log.info("Polling...");

                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record: records) {
                    log.info("Key: " + record.key() + ", Value: " + record.value());
                    log.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                }
            }
        } catch (WakeupException e) {
            log.info("Wake up exception!");
        } catch (Exception e) {
            log.info("Unexpected exception", e);
        } finally {
            consumer.close();
            log.info("The consumer is now gracefully shut down");
        }

    }
}
