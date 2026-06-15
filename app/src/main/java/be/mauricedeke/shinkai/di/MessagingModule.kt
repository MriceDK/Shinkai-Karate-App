package be.mauricedeke.shinkai.di

import be.mauricedeke.shinkai.BuildConfig
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

    @Provides
    @Singleton
    fun provideConnectionFactory(): ConnectionFactory {
        val hostPort = BuildConfig.AMQP_URL
        val hostParts = hostPort.split(":", limit = 2)
        val host = hostParts.firstOrNull().orEmpty()
        val port = hostParts.getOrNull(1)?.toIntOrNull() ?: ConnectionFactory.DEFAULT_AMQP_OVER_SSL_PORT

        return ConnectionFactory().apply {
            username = BuildConfig.AMQP_USERNAME
            password = BuildConfig.AMQP_PASSWORD
            this.host = host
            this.port = port
            virtualHost = BuildConfig.AMQP_VHOST
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
            exchange = BuildConfig.AMQP_EXCHANGE,
            routingKeyPrefix = BuildConfig.AMQP_PUBLISH_ROUTING_KEY,
            factory = factory,
            adapter = adapter
        )

    @Provides
    @Singleton
    fun provideMessageConsumer(factory: ConnectionFactory): MessageConsumer =
        LavinMQMessageConsumer(
            exchange = BuildConfig.AMQP_EXCHANGE,
            routingKeyPrefix = BuildConfig.AMQP_SUBSCRIBE_ROUTING_KEY,
            factory = factory
        )
}
