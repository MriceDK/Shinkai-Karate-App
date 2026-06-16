package be.mauricedeke.shinkai.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import be.mauricedeke.shinkai.data.messaging.LavinMQMessageConsumer
import be.mauricedeke.shinkai.data.messaging.LavinMQMessagePublisher
import be.mauricedeke.shinkai.data.messaging.MessageConsumer
import be.mauricedeke.shinkai.data.messaging.MessagePublisher
import be.mauricedeke.shinkai.domain.model.NotificationMessage
import com.rabbitmq.client.ConnectionFactory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MessagingModule {

    private const val PUBLISH_ROUTING_KEY_PREFIX = "user"
    private const val SUBSCRIBE_ROUTING_KEY_PREFIX = "user"

    @Provides
    @Singleton
    fun provideConnectionFactory(): ConnectionFactory {
        val rc = Firebase.remoteConfig
        val hostPort = rc.getString("amqp_url")
        val hostParts = hostPort.split(":", limit = 2)
        val host = hostParts.firstOrNull().orEmpty()
        val port = hostParts.getOrNull(1)?.toIntOrNull() ?: ConnectionFactory.DEFAULT_AMQP_OVER_SSL_PORT

        return ConnectionFactory().apply {
            username = rc.getString("amqp_username")
            password = rc.getString("amqp_password")
            this.host = host
            this.port = port
            virtualHost = rc.getString("amqp_vhost")
            useSslProtocol()
        }
    }

    @Provides
    @Singleton
    fun provideNotificationMessageAdapter(moshi: Moshi): JsonAdapter<NotificationMessage> =
        moshi.adapter(NotificationMessage::class.java)

    @Provides
    @Singleton
    fun provideMessagePublisher(
        factory: ConnectionFactory,
        adapter: JsonAdapter<NotificationMessage>
    ): MessagePublisher =
        LavinMQMessagePublisher(
            exchange = Firebase.remoteConfig.getString("amqp_exchange"),
            routingKeyPrefix = PUBLISH_ROUTING_KEY_PREFIX,
            factory = factory,
            adapter = adapter
        )

    @Provides
    @Singleton
    fun provideMessageConsumer(factory: ConnectionFactory): MessageConsumer =
        LavinMQMessageConsumer(
            exchange = Firebase.remoteConfig.getString("amqp_exchange"),
            routingKeyPrefix = SUBSCRIBE_ROUTING_KEY_PREFIX,
            factory = factory
        )
}
