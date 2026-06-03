package be.mauricedeke.shinkai.data.messaging

import android.util.Log
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class LavinMQMessagePublisher @Inject constructor(
    private val hostName: String,
    private val exchange: String
) : MessagePublisher {
    private val factory = ConnectionFactory()

    override suspend fun publishMessage(message: String, userId: String) {
        withContext(Dispatchers.IO) {
            try {
                val connection = factory.newConnection(hostName)
                val channel = connection.createChannel()

                channel.basicPublish(
                    exchange,
                    userId,
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