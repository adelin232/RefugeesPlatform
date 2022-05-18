import pika, sys, os, time
import smtplib, ssl

host = os.environ.get('HOST')
queue = os.environ.get('QUEUE_NAME')

def send_mail(email):
    port = 465  # For SSL
    smtp_server = "smtp.gmail.com"
    sender_email = "ukrainianrefugees123@gmail.com"  # Enter your address
    receiver_email = email  # Enter receiver address
    password = "fvpvdrfkicykdcdc"
    subject = "Account Created"
    text = "You have successfully created your account on our website!"
    message = 'Subject: {}\n\n{}'.format(subject, text)

    context = ssl.create_default_context()
    with smtplib.SMTP_SSL(smtp_server, port, context=context) as server:
        server.login(sender_email, password)
        server.sendmail(sender_email, receiver_email, message)

def on_message(ch, method, properties, body):
    message = body.decode('UTF-8')
    print(" [x] Received %r" % message)
    send_mail(message.replace('\'', ''))

def main():
    credentials = pika.PlainCredentials('admin', 'rabbitmq')
    connection_params = pika.ConnectionParameters(host="rabbitmq-broker", credentials=credentials)

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

    channel.queue_declare(queue=queue, durable=True)
    channel.basic_consume(queue=queue, on_message_callback=on_message, auto_ack=True)

    print('Subscribed to ' + queue + ', waiting for messages...')
    channel.start_consuming()

if __name__ == '__main__':
    main()