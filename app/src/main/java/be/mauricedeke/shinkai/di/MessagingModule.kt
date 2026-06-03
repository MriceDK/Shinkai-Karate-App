package be.mauricedeke.shinkai.di

import be.mauricedeke.shinkai.BuildConfig
import be.mauricedeke.shinkai.data.messaging.LavinMQMessageConsumer
import be.mauricedeke.shinkai.data.messaging.LavinMQMessagePublisher
import be.mauricedeke.shinkai.data.messaging.MessageConsumer
import be.mauricedeke.shinkai.data.messaging.MessagePublisher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MessagingModule {
    private val lavinMQHost = "amqp://${BuildConfig.AMQP_USERNAME}:${BuildConfig.AMQP_PASSWORD}@${BuildConfig.AMQP_URL}/${BuildConfig.AMQP_VHOST}"

    @Provides
    @Singleton
    fun providePublishService(): MessagePublisher = LavinMQMessagePublisher(lavinMQHost, BuildConfig.AMQP_EXCHANGE)

    @Provides
    fun provideConsumeService(): MessageConsumer = LavinMQMessageConsumer(lavinMQHost, BuildConfig.AMQP_EXCHANGE)
}
