import pika
import logging

logging.basicConfig()

connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))

channel = connection.channel()

channel.queue_declare(queue='rabbit_queue')

def callback(ch, method, properties, body):
	print(" [x] Received %r" % body)

channel.basic_consume(queue='rabbit_queue', on_message_callback=callback, auto_ack=True)

print(' [*] Waiting for messages. To exit press CTRL+C')
channel.start_consuming()

connection.close()
