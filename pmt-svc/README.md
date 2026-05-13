# Ensure No Message Loss
The service will listen to messages in order-placed topics and process.
In case of failures (simulated in this case), it'll retry those messages for specified number of times, post which it'll just move these messages to a dead letter topic. The listener
for dead letter topic just logs the message.