package be.mauricedeke.shinkai.di

import be.mauricedeke.shinkai.BuildConfig
import be.mauricedeke.shinkai.data.messaging.LavinMQMessageConsumer
import be.mauricedeke.shinkai.data.messaging.LavinMQMessagePublisher
import be.mauricedeke.shinkai.data.messaging.MessageConsumer
import be.mauricedeke.shinkai.data.messaging.MessagePublisher
import com.rabbitmq.client.ConnectionFactory
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
    fun provideMessagePublisher(factory: ConnectionFactory): MessagePublisher =
        LavinMQMessagePublisher(BuildConfig.AMQP_EXCHANGE, factory)

    @Provides
    @Singleton
    fun provideMessageConsumer(factory: ConnectionFactory): MessageConsumer =
        LavinMQMessageConsumer(BuildConfig.AMQP_EXCHANGE, factory)
}
