package be.mauricedeke.shinkai.data.messaging

import android.util.Log
import be.mauricedeke.shinkai.domain.model.NotificationMessage
import com.rabbitmq.client.ConnectionFactory
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

class LavinMQMessagePublisher(
    private val exchange: String,
    private val routingKeyPrefix: String,
    private val factory: ConnectionFactory,
    private val adapter: JsonAdapter<NotificationMessage>
) : MessagePublisher {

    override suspend fun publish(message: NotificationMessage, userId: String) {
        withContext(Dispatchers.IO) {
            try {
                val payload = adapter.toJson(message)
                val connection = factory.newConnection()
                val channel = connection.createChannel()

                channel.exchangeDeclare(exchange, "direct", true)
                channel.basicPublish(
                    exchange,
                    "$routingKeyPrefix-$userId",
                    null,
                    payload.toByteArray(StandardCharsets.UTF_8)
                )
                Log.i("Messagebroker", "Message sent to broker: $payload")

                channel.close()
                connection.close()

            } catch (e: Exception) {
                Log.e("Messagebroker", "Error publishing to Messagebroker! - ${e.message}")
            }
        }
    }
}
