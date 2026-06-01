package be.mauricedeke.shinkai.data.fake

import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.BeltColor
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
        Event(
            "1", "Stage Naigairyu", "10:00", "16:00", "14/03", "Kapellestraat 79", "Evergem",
            "Naigairyu Bujutsu Kai — School for traditional Japanese martial arts.",
            localDate = LocalDate.of(2026, 3, 14)
        ),
        Event(
            "2", "Stage Naigairyu", "10:00", "16:00", "14/03", "Kapellestraat 79", "Evergem",
            "Naigairyu Bujutsu Kai — School for traditional Japanese martial arts.",
            localDate = LocalDate.of(2026, 3, 14)
        ),
    )

    val inboxEvents = listOf(
        Event(
            "3", "Stage test1234", "9:00", "12:00", "16 maart 2026", "", "Limburg",
            "", isInbox = true, localDate = LocalDate.of(2026, 3, 16)
        ),
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
            beltColor = BeltColor.YELLOW,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection(
                        title = "10 bevrijdingstechnieken",
                        items = listOf(
                            "Tegen gekruist vastnemen hand",
                            "Tegen parallel vastnemen hand",
                            "Tegen vastnemen aan beide handen",
                            "Tegen vastnemen twee handen op één",
                            "Tegen wurging met één hand en hoekstoot"
                        )
                    ),
                    ProgramSection(title = "2 Technieken vrije keuze self defense"),
                    ProgramSection(
                        title = "Traptechnieken / Geri waza",
                        items = listOf(
                            "Mae Geri (voorwaartse trap)",
                            "Mawashi Geri (cirkelvormige trap)",
                            "Yoko Geri (zijwaartse trap)",
                            "Ushiro Geri (achterwaartse trap)"
                        )
                    ),
                    ProgramSection(
                        title = "Kata",
                        items = listOf("Shodan Kata", "Blokkingset 1")
                    ),
                )
            ),
            technieken = listOf(
                Techniek("Gekruis vastnemen 1 hand", "Geel", "Testing123"),
                Techniek("Parallel vastnemen 1 hand", "Geel", ""),
                Techniek("Vastnemen 2 handen op 1", "Geel", ""),
                Techniek(
                    "Wurging met 1 hand en hoekstoot", "Geel",
                    "Kin naar beneden doen en met de rechter hand de linkse hoekstoot blokkeren " +
                            "en met de linkerhand controle uitoefenen op de rechtse hand van de aanvaller."
                ),
            )
        ),
        Belt(
            name = "Oranje",
            beltColor = BeltColor.ORANGE,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Standen / Dachi waza"),
                    ProgramSection("Slag/Stoot technieken"),
                    ProgramSection("Afweertechnieken / Uke waza"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
            technieken = listOf(
                Techniek("Techniek 1", "Oranje", ""),
                Techniek("Techniek 2", "Oranje", ""),
            )
        ),
        Belt(
            name = "Rood",
            beltColor = BeltColor.RED,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Standen / Dachi waza"),
                    ProgramSection("Slag/Stoot technieken"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
        ),
        Belt(
            name = "Groen",
            beltColor = BeltColor.GREEN,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Standen / Dachi waza"),
                    ProgramSection("Slag/Stoot technieken"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
        ),
        Belt(
            name = "Blauw",
            beltColor = BeltColor.BLUE,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Afweertechnieken / Uke waza"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
        ),
        Belt(
            name = "Paars",
            beltColor = BeltColor.PURPLE,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Afweertechnieken / Uke waza"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
        ),
        Belt(
            name = "Bruin - I",
            beltColor = BeltColor.BROWN_I,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Afweertechnieken / Uke waza"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
        ),
        Belt(
            name = "Bruin - II",
            beltColor = BeltColor.BROWN_II,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Afweertechnieken / Uke waza"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
        ),
        Belt(
            name = "Bruin - III",
            beltColor = BeltColor.BROWN_III,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Afweertechnieken / Uke waza"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
        ),
        Belt(
            name = "Zwart",
            beltColor = BeltColor.BLACK,
            pogramma = BeltProgram(
                sections = listOf(
                    ProgramSection("Vereisten om te mogen deelnemen"),
                    ProgramSection("Afweertechnieken / Uke waza"),
                    ProgramSection("Traptechnieken / Geri waza"),
                    ProgramSection("Kata"),
                    ProgramSection("Conditie"),
                    ProgramSection("Algemene kennis en geschiedenis"),
                )
            ),
        ),
    )

    val lexiconEntries = listOf(
        LexiconEntry("Rei", "Buiging / Groet", "Rei is de formele buiging die respect uitdrukt tegenover de sensei, de dojo en de trainingspartner."),
        LexiconEntry("Dojo", "Trainingsplaats", "De dojo is de ruimte waar karate beoefend wordt. Het woord betekent letterlijk 'plaats van de weg'."),
        LexiconEntry("Sensei", "Leraar / Meester", "Sensei betekent letterlijk 'degene die voor is gegaan'. Het is de titel voor een karate-instructeur."),
        LexiconEntry("Karate", "Lege hand", "Karate is een Japanse vechtkunst waarbij gevochten wordt zonder wapens, enkel met de lege hand."),
        LexiconEntry("Kiai", "Strijdkreet"),
        LexiconEntry("Kata", "Patroon / Vorm", "Een kata is een vaste reeks van technieken die solo uitgevoerd wordt en een gesimuleerde gevechtsscenario voorstelt."),
        LexiconEntry("Kumite", "Gevecht / Sparring", "Kumite is het vrije of vaste sparren met een partner, waarbij aanvals- en afweertechnieken worden gecombineerd."),
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
