package be.mauricedeke.shinkai.data.fake

import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.domain.model.BeltProgram
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.model.LexiconEntry
import be.mauricedeke.shinkai.domain.model.ProgramSection
import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.model.Techniek
import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.domain.model.UserProfile
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

object FakeDataSource {

    // Shared RSVP state across all screens, seeded from initial event values
    val rsvpMap: MutableMap<String, Boolean?> = mutableMapOf(
        "1" to true,
        "3" to false,
    )

    val events = listOf(
        Event(
            "1", "Stage Naigairyu", "10:00", "16:00",
            LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Kapellestraat 79", "Evergem",
            "Naigairyu Bujutsu Kai — School for traditional Japanese martial arts.",
            localDate = LocalDate.now().plusDays(7),
            rsvp = true
        ),
        Event(
            "2", "Kata Training", "19:00", "21:00",
            LocalDate.now().plusDays(14).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Sporthal De Kuip", "Gent",
            "Gezamenlijke kata training voor alle graden.",
            localDate = LocalDate.now().plusDays(14),
            rsvp = null
        ),
        Event(
            "4", "Kumite Stage", "09:00", "17:00",
            LocalDate.now().plusDays(21).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Martial Arts Center", "Brugge",
            "Intensieve kumite stage voor gevorderde leerlingen.",
            localDate = LocalDate.now().plusDays(21),
            rsvp = null
        ),
        Event(
            "5", "Grading Examen", "10:00", "14:00",
            LocalDate.now().plusDays(30).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Hoofddojo", "Antwerpen",
            "Officieel grading examen voor de volgende bandkleur.",
            localDate = LocalDate.now().plusDays(30),
            rsvp = null
        ),
        Event(
            "6", "Grading Examen 2", "10:00", "14:00",
            LocalDate.now().plusDays(30).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Hoofddojo", "Antwerpen",
            "Officieel grading examen voor de volgende bandkleur.",
            localDate = LocalDate.now().plusDays(30),
            rsvp = null
        ),
        Event(
            "7", "Voorbij examen", "10:00", "14:00",
            LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Hoofddojo", "Antwerpen",
            "Officieel voorbij examen voor de volgende bandkleur.",
            localDate = LocalDate.now().minusDays(30),
            rsvp = null
        ),
    )

    val inboxEvents = listOf(
        Event(
            "3", "Uitnodiging Vriendenkamp", "9:00", "17:00",
            LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("dd/MM")),
            "", "Limburg",
            "Speciaal vriendenkamp georganiseerd door de regionale federatie.",
            isInbox = true, localDate = LocalDate.now().plusDays(10),
            rsvp = false
        ),
        Event(
            "6", "Demonstratie Openingsdag", "13:00", "15:00",
            LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Gemeenteplein", "Aalst",
            "Publieke demonstratie ter gelegenheid van de openingsdag van het sportseizoen.",
            isInbox = true, localDate = LocalDate.now().plusDays(5),
            rsvp = null
        ),
    )

    val nextTraining = Training(
        id = "t1", type = "Technieken",
        startTime = "20:00", endTime = "22:00",
        date = LocalDate.now().plusDays(2),
        injuries = "Geen", sensei = "Sensei Ludo"
    )

    val trainings: MutableList<Training> = mutableListOf(
        // June 2026
        Training("t1",  "Technieken",  "20:00", "22:00", LocalDate.now().plusDays(2),   "Geen",              "Sensei Ludo"),
        Training("t22",  "Technieken",  "20:00", "22:00", LocalDate.now().plusDays(2),   "Geen",              "Sensei Ludo"),
        Training("t2",  "Kumite",      "19:00", "21:00", LocalDate.now().minusDays(2),  "Geen",              "Sensei Ludo",  note = "Goede sessie vandaag. Gyaku-zuki combinaties zijn verbeterd."),
        Training("t3",  "Kata",        "20:00", "22:00", LocalDate.now().minusDays(4),  "Geen",              "Sensei Yuki"),
        Training("t4",  "Conditie",    "19:30", "21:00", LocalDate.now().minusDays(7),  "Geen",              "Sensei Ludo"),
        Training("t5",  "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(9),  "Pols — licht",      "Sensei Yuki",  note = "Pols voelt nog wat stijf. Minder kracht gezet op stoot."),
        Training("t21",  "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(9),  "Pols — licht",      "Sensei Yuki",  note = "Pols voelt nog wat stijf. Minder kracht gezet op stoot."),
        // May 2026
        Training("t6",  "Kumite",      "19:00", "21:00", LocalDate.now().minusDays(12), "Geen",              "Sensei Ludo"),
        Training("t7",  "Kata",        "20:00", "22:00", LocalDate.now().minusDays(14), "Geen",              "Sensei Yuki"),
        Training("t8",  "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(16), "Schouder — licht",  "Sensei Ludo",  note = "Schouder blessure tijdens uke-waza. Volgende training voorzichtig zijn."),
        Training("t9",  "Conditie",    "19:30", "21:00", LocalDate.now().minusDays(19), "Geen",              "Sensei Ludo"),
        Training("t10", "Kata",        "19:00", "21:00", LocalDate.now().minusDays(21), "Geen",              "Sensei Yuki"),
        Training("t11", "Kumite",      "20:00", "22:00", LocalDate.now().minusDays(23), "Geen",              "Sensei Ludo"),
        Training("t12", "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(26), "Geen",              "Sensei Yuki"),
        Training("t13", "Conditie",    "19:30", "21:00", LocalDate.now().minusDays(28), "Knie — licht",      "Sensei Ludo",  note = "Kniepijn bij laag gedeelte kata. Vraag naar aanpassingen."),
        // April 2026
        Training("t14", "Kata",        "19:00", "21:00", LocalDate.now().minusDays(33), "Geen",              "Sensei Yuki"),
        Training("t15", "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(35), "Geen",              "Sensei Ludo"),
        Training("t16", "Kumite",      "19:00", "21:00", LocalDate.now().minusDays(38), "Geen",              "Sensei Yuki"),
        Training("t17", "Conditie",    "19:30", "21:00", LocalDate.now().minusDays(40), "Geen",              "Sensei Ludo"),
        Training("t18", "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(42), "Rug — licht",       "Sensei Yuki",  note = "Rug pijn na conditietraining. Ibuprofen genomen."),
        Training("t19", "Kata",        "19:00", "21:00", LocalDate.now().minusDays(47), "Geen",              "Sensei Ludo"),
        Training("t20", "Kumite",      "20:00", "22:00", LocalDate.now().minusDays(49), "Geen",              "Sensei Yuki"),
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
        userId = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890"),
        name = "Maurice De Kegel",
        email = "maurice.de.kegel@student.howest.be",
        belt = "Yellow belt"
    )

    val strengthResults = listOf(
        StrengthResult("Punching Strength", 659),
        StrengthResult("Kiai Strength", 0),
    )

    // Lege lijsten voor testing van schermen zonder data
//
//    val events: List<Event>? = null
//    val inboxEvents: List<Event>? = null
//    val trainings: List<Training>? = null
//    val nextTraining: Training? = null
//    val belts: List<Belt>? = null
//    val lexiconEntries: List<LexiconEntry>? = null
//    val strengthResults: List<StrengthResult>? = null
//    val userProfile = null
}
