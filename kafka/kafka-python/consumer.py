import concurrent.futures
import json
import os

from kafka import KafkaConsumer

from log import Logger
from producer import Producer
from dotenv import load_dotenv

load_dotenv()

consumer_topic = os.environ.get("consumer-topic")
group_id = os.environ.get("group-id")
bootstrap_servers = os.environ.get("bootstrap-servers")


class Consumer:
    broker = ""
    topic = ""
    group_id = ""
    logger = None
    consumer = None
    producer = None

    def __init__(self):
        self.logger = Logger(name='consumer').logger
        self.broker = bootstrap_servers
        self.topic = consumer_topic
        self.group_id = group_id
        self.consumer = KafkaConsumer(
            bootstrap_servers=self.broker,
            group_id=self.group_id,
            auto_offset_reset='latest',
            enable_auto_commit=True,
            value_deserializer=lambda m: json.loads(m.decode('utf-8')),
        )
        self.producer = Producer()
        self.consumer.subscribe([self.topic])
        self.executor = concurrent.futures.ThreadPoolExecutor(max_workers=5)

    def run(self):
        self.logger.info('Starting Consumer...')

        try:
            for message in self.consumer:
                self.executor.submit(self.process_message, message)
        except Exception as e:
            self.logger.error(f'Failed process message : {e}')
        finally:
            self.executor.shutdown(wait=True)

    def process_message(self, message):
        self.logger.info('Receive Message')

        key = str(message.key, 'utf-8') if message.key else 'default_key'
        is_json = isinstance(message.value, dict)

        if not is_json:
            self.logger.error('Not Json type')
            return

        self.logger.info(f"Receving {key} message id : {message.value['id']} value : {message.value['name']}")

        self.producer.send_message('python_key', {
            'id': message.value['id'],
            'message': f"Hello, {message.value['name']}"
        })