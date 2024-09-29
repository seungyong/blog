import threading

from consumer import Consumer


def initial_kafka():
    consumer = Consumer()
    thread = threading.Thread(target=consumer.run)
    thread.start()
    thread.join()


if __name__ == '__main__':
    initial_kafka()
