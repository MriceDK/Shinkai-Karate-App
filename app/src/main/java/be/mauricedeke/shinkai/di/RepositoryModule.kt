package be.mauricedeke.shinkai.di

import be.mauricedeke.shinkai.data.repository.AccelerometerRepositoryImpl
import be.mauricedeke.shinkai.data.repository.RouteRepositoryImpl
import be.mauricedeke.shinkai.data.repository.EventRepositoryImpl
import be.mauricedeke.shinkai.data.repository.LexiconRepositoryImpl
import be.mauricedeke.shinkai.data.repository.LocationRepositoryImpl
import be.mauricedeke.shinkai.data.repository.MicrophoneRepositoryImpl
import be.mauricedeke.shinkai.data.repository.NoteRepositoryImpl
import be.mauricedeke.shinkai.data.repository.SettingsRepositoryImpl
import be.mauricedeke.shinkai.data.repository.TechniekRepositoryImpl
import be.mauricedeke.shinkai.data.repository.ThemeRepositoryImpl
import be.mauricedeke.shinkai.data.repository.TrainingNoteRepositoryImpl
import be.mauricedeke.shinkai.data.repository.TrainingRepositoryImpl
import be.mauricedeke.shinkai.data.repository.UserRepositoryImpl
import be.mauricedeke.shinkai.domain.repository.AccelerometerRepository
import be.mauricedeke.shinkai.domain.repository.RouteRepository
import be.mauricedeke.shinkai.domain.repository.EventRepository
import be.mauricedeke.shinkai.domain.repository.LexiconRepository
import be.mauricedeke.shinkai.domain.repository.LocationRepository
import be.mauricedeke.shinkai.domain.repository.MicrophoneRepository
import be.mauricedeke.shinkai.domain.repository.NoteRepository
import be.mauricedeke.shinkai.domain.repository.SettingsRepository
import be.mauricedeke.shinkai.domain.repository.TechniekRepository
import be.mauricedeke.shinkai.domain.repository.ThemeRepository
import be.mauricedeke.shinkai.domain.repository.TrainingNoteRepository
import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import be.mauricedeke.shinkai.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @Binds
    abstract fun bindTechniekRepository(impl: TechniekRepositoryImpl): TechniekRepository

    @Binds
    abstract fun bindLexiconRepository(impl: LexiconRepositoryImpl): LexiconRepository

    @Binds
    abstract fun bindTrainingRepository(impl: TrainingRepositoryImpl): TrainingRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindThemeRepository(impl: ThemeRepositoryImpl): ThemeRepository

    @Binds
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Binds
    abstract fun bindTrainingNoteRepository(impl: TrainingNoteRepositoryImpl): TrainingNoteRepository

    @Binds
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindAccelerometerRepository(impl: AccelerometerRepositoryImpl): AccelerometerRepository

    @Binds
    abstract fun bindMicrophoneRepository(impl: MicrophoneRepositoryImpl): MicrophoneRepository

    @Binds
    abstract fun bindRouteRepository(impl: RouteRepositoryImpl): RouteRepository
}
