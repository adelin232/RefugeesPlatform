FROM python:3.11.0b1-alpine
COPY ./receive.py ./receive.py
RUN python3 -m pip install pika
RUN sleep 5
CMD ["python", "receive.py"]