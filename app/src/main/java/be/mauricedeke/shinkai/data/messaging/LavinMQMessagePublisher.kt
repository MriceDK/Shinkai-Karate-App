package be.mauricedeke.shinkai.data.messaging

import android.util.Log
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

class LavinMQMessagePublisher(
    private val exchange: String,
    private val factory: ConnectionFactory
) : MessagePublisher {

    override suspend fun publishMessage(message: String, userId: String) {
        withContext(Dispatchers.IO) {
            try {
                val connection = factory.newConnection()
                val channel = connection.createChannel()

                channel.exchangeDeclare(exchange, "direct", true)
                channel.basicPublish(
                    exchange,
                    "user-${userId}",
                    null,
                    message.toByteArray(StandardCharsets.UTF_8)
                )
                Log.i("Messagebroker", "Message sent to broker: $message")

                channel.close()
                connection.close()

            } catch (e: Exception) {
                Log.e("Messagebroker", "Error publishing to Messagebroker! - ${e.message}")
            }
        }
    }
}