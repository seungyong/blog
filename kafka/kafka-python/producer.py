import json
import os
from dotenv import load_dotenv
from kafka import KafkaProducer

from log import Logger

load_dotenv()

producer_topic = os.environ.get("producer-topic")
bootstrap_servers = os.environ.get("bootstrap-servers").split(",")


class Producer:
    broker = ""
    topic = ""
    producer = None
    logger = None

    def __init__(self):
        self.logger = Logger(name='producer').logger
        self.broker = bootstrap_servers
        self.topic = producer_topic
        self.producer = KafkaProducer(
            bootstrap_servers=self.broker,
            key_serializer=lambda x: bytes(x, encoding='utf-8'),
            value_serializer=lambda x: json.dumps(x).encode('utf-8'),
            retries=3
        )

    def send_message(self, key, message):
        try:
            self.producer.send(
                topic=self.topic,
                key=key,
                value=message
            )

            self.producer.flush()
        except Exception as e:
            self.logger.error(f"Error sending message to {self.topic}: {message} cause {e.__str__()}")
