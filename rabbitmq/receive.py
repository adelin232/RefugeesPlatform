import pika, sys, os, time

host = os.environ.get('HOST')
queue = os.environ.get('QUEUE_NAME')

def on_message(ch, method, properties, body):
    message = body.decode('UTF-8')
    print(" [x] Received %r" % message)

def main():
    connection_params = pika.ConnectionParameters(host="rabbitmq-broker")

    connected = 0
    while not connected:
        try:
            time.sleep(1)
            connection = pika.BlockingConnection(connection_params)
        except pika.exceptions.AMQPConnectionError:
            print("Rabbitmq not available")
            continue
        else:
            connected = 1
    print("Rabbitmq connection established")

    channel = connection.channel()

    channel.queue_declare(queue=queue)
    channel.basic_consume(queue=queue, on_message_callback=on_message, auto_ack=True)

    print('Subscribed to ' + queue + ', waiting for messages...')
    channel.start_consuming()

if __name__ == '__main__':
    main()