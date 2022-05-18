# import pika
# import logging

# logging.basicConfig()

# connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))

# channel = connection.channel()

# channel.queue_declare(queue='rabbit_queue')

# def callback(ch, method, properties, body):
# 	print(" [x] Received %r" % body)

# channel.basic_consume(queue='rabbit_queue', on_message_callback=callback, auto_ack=True)

# print(' [*] Waiting for messages. To exit press CTRL+C')
# channel.start_consuming()

# connection.close()

import pika, sys, os

host = os.environ.get('HOST')
queue = os.environ.get('QUEUE_NAME')

def on_message(ch, method, properties, body):
    message = body.decode('UTF-8')
    # print(message)
    print(" [x] Received %r" % message)

def main():
    connection_params = pika.ConnectionParameters(host="rabbitmq-broker")
    connection = pika.BlockingConnection(connection_params)
    channel = connection.channel()

    channel.queue_declare(queue=queue)
    channel.basic_consume(queue=queue, on_message_callback=on_message, auto_ack=True)

    print('Subscribed to ' + queue + ', waiting for messages...')
    channel.start_consuming()

if __name__ == '__main__':
    main()