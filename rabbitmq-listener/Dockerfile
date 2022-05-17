FROM python:3.11.0b1
COPY ./receive.py ./receive.py
RUN python3 -m pip install pika
CMD ["python3", "receive.py"]