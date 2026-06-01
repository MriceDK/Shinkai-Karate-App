package be.mauricedeke.shinkai.data.fake

import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.BeltProgram
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.model.LexiconEntry
import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.domain.model.ProgramSection
import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.model.Techniek
import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.domain.model.UserProfile
import java.time.LocalDate

object FakeDataSource {

    val events = listOf(
        Event("1", "Stage Naigairyu", "10:00", "16:00", "14/03", "Kapellestraat 79", "Evergem",
            "Naigairyu Bujutsu Kai — School for traditional Japanese martial arts."),
        Event("2", "Stage Naigairyu", "10:00", "16:00", "14/03", "Kapellestraat 79", "Evergem",
            "Naigairyu Bujutsu Kai — School for traditional Japanese martial arts."),
    )

    val inboxEvents = listOf(
        Event("3", "Stage test1234", "9:00", "12:00", "16 maart 2026", "", "Limburg",
            "", isInbox = true),
    )

    val nextTraining = Training(
        id = "1", type = "Technieken",
        startTime = "20:00", endTime = "22:00",
        date = LocalDate.of(2025, 3, 18),
        injuries = "Geen", sensei = "Geen"
    )

    val trainings = listOf(
        Training("1", "Technieken", "20:00", "22:00", LocalDate.of(2025, 8, 14), "Geen", "Geen"),
        Training("2", "Technieken", "20:00", "22:00", LocalDate.of(2025, 8, 14), "Geen", "Geen"),
        Training("3", "Kata", "19:00", "21:00", LocalDate.of(2025, 8, 7), "Geen", "Sensei Ludo"),
    )

    val belts = listOf(
        Belt(
            name = "Geel",
            colorHex = "#FFD700",
            pogramma = BeltProgram(sections = listOf(
                ProgramSection("Vereisten om te mogen deelnemen"),
                ProgramSection("Standen / Dachi waza"),
                ProgramSection("Slag/Stoot technieken"),
                ProgramSection("Afweertechnieken / Uke waza"),
                ProgramSection("Traptechnieken / Geri waza"),
                ProgramSection("Kata"),
                ProgramSection("Conditie"),
                ProgramSection("Algemene kennis en geschiedenis"),
            )),
            technieken = listOf(
                Techniek("Gekruis vastnemen 1 hand", "Geel", ""),
                Techniek("Parallel vastnemen 1 hand", "Geel", ""),
                Techniek("Vastnemen 2 handen op 1", "Geel", ""),
                Techniek("Wurging met 1 hand en hoekstoot", "Geel",
                    "Kin naar beneden doen en met de rechter hand de linkse hoekstoot blokkeren " +
                    "en met de linkerhand controle uitoefenen op de rechtse hand van de aanvaller."),
            )
        ),
        Belt(
            name = "Oranje",
            colorHex = "#FF8C00",
            pogramma = BeltProgram(sections = listOf(
                ProgramSection("Vereisten om te mogen deelnemen"),
                ProgramSection("Standen / Dachi waza"),
                ProgramSection("Slag/Stoot technieken"),
                ProgramSection("Afweertechnieken / Uke waza"),
                ProgramSection("Traptechnieken / Geri waza"),
                ProgramSection("Kata"),
                ProgramSection("Conditie"),
                ProgramSection("Algemene kennis en geschiedenis"),
            )),
            technieken = listOf(
                Techniek("Techniek 1", "Oranje", ""),
                Techniek("Techniek 2", "Oranje", ""),
            )
        ),
        Belt(
            name = "Rood",
            colorHex = "#CC0000",
            pogramma = BeltProgram(sections = listOf(
                ProgramSection("Vereisten om te mogen deelnemen"),
                ProgramSection("Standen / Dachi waza"),
                ProgramSection("Slag/Stoot technieken"),
                ProgramSection("Traptechnieken / Geri waza"),
                ProgramSection("Kata"),
                ProgramSection("Conditie"),
                ProgramSection("Algemene kennis en geschiedenis"),
            )),
        ),
        Belt(
            name = "Groen",
            colorHex = "#1B5E20",
            pogramma = BeltProgram(sections = listOf(
                ProgramSection("Vereisten om te mogen deelnemen"),
                ProgramSection("Standen / Dachi waza"),
                ProgramSection("Slag/Stoot technieken"),
                ProgramSection("Traptechnieken / Geri waza"),
                ProgramSection("Kata"),
                ProgramSection("Conditie"),
                ProgramSection("Algemene kennis en geschiedenis"),
            )),
        ),
        Belt(
            name = "Blauw",
            colorHex = "#1565C0",
            pogramma = BeltProgram(sections = listOf(
                ProgramSection("Vereisten om te mogen deelnemen"),
                ProgramSection("Afweertechnieken / Uke waza"),
                ProgramSection("Traptechnieken / Geri waza"),
                ProgramSection("Kata"),
                ProgramSection("Conditie"),
                ProgramSection("Algemene kennis en geschiedenis"),
            )),
        ),
    )

    val lexiconEntries = listOf(
        LexiconEntry("Rei", "Buiging / Groet"),
        LexiconEntry("Dojo", "Trainingsplaats"),
        LexiconEntry("Sensei", "Leraar / Meester"),
        LexiconEntry("Karate", "Lege hand"),
        LexiconEntry("Kiai", "Strijdkreet"),
        LexiconEntry("Kata", "Patroon / Vorm"),
        LexiconEntry("Kumite", "Gevecht / Sparring"),
        LexiconEntry("Mawashi", "Cirkelbeweging"),
        LexiconEntry("Tsuki", "Stoot"),
        LexiconEntry("Geri", "Trap"),
        LexiconEntry("Uke", "Afweer / Blok"),
        LexiconEntry("Mae", "Voor / Voorwaarts"),
        LexiconEntry("Yoko", "Zijwaarts"),
        LexiconEntry("Ushiro", "Achterwaarts"),
    )

    val userProfile = UserProfile(
        name = "Maurice De Kegel",
        email = "maurice.de.kegel@student.howest.be",
        belt = "Yellow belt"
    )

    val strengthResults = listOf(
        StrengthResult("Punching Strength", 659, "#1B5E20"),
        StrengthResult("Kiai Strength", 250, "#FF8C00"),
    )

    val defaultNotificationSettings = NotificationSettings()
    val defaultLocationSettings = LocationSettings()
}
