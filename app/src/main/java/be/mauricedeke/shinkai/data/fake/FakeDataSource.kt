package be.mauricedeke.shinkai.data.fake

import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.BeltColor
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
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

    val events = mutableListOf(
        Event(
            UUID.randomUUID(), "Stage Naigairyu", "10:00", "16:00",
            LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Kapellestraat 79", "Evergem",
            "Naigairyu Bujutsu Kai — School for traditional Japanese martial arts.",
            localDate = LocalDate.now().plusDays(7),
            rsvp = true,
            lat = 51.1019, lng = 3.7177
        ),
        Event(
            UUID.randomUUID(), "Kata Training", "19:00", "21:00",
            LocalDate.now().plusDays(14).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Sporthal De Kuip", "Gent",
            "Gezamenlijke kata training voor alle graden.",
            localDate = LocalDate.now().plusDays(14),
            rsvp = null,
            lat = 51.0427, lng = 3.7228
        ),
        Event(
            UUID.randomUUID(), "Kumite Stage", "09:00", "17:00",
            LocalDate.now().plusDays(21).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Martial Arts Center", "Brugge",
            "Intensieve kumite stage voor gevorderde leerlingen.",
            localDate = LocalDate.now().plusDays(21),
            rsvp = null,
            lat = 51.2093, lng = 3.2247
        ),
        Event(
            UUID.randomUUID(), "Grading Examen", "10:00", "14:00",
            LocalDate.now().plusDays(30).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Hoofddojo", "Antwerpen",
            "Officieel grading examen voor de volgende bandkleur.",
            localDate = LocalDate.now().plusDays(30),
            rsvp = null,
            lat = 51.2194, lng = 4.4025
        ),
        Event(
            UUID.randomUUID(), "Grading Examen 2", "10:00", "14:00",
            LocalDate.now().plusDays(30).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Hoofddojo", "Antwerpen",
            "Officieel grading examen voor de volgende bandkleur.",
            localDate = LocalDate.now().plusDays(30),
            rsvp = null,
            lat = 51.2194, lng = 4.4025
        ),
        Event(
            UUID.randomUUID(), "Voorbij examen", "10:00", "14:00",
            LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Hoofddojo", "Antwerpen",
            "Officieel voorbij examen voor de volgende bandkleur.",
            localDate = LocalDate.now().minusDays(30),
            rsvp = null,
            lat = 51.2194, lng = 4.4025
        ),
    )

    val inboxEvents = mutableListOf(
        Event(
            UUID.randomUUID(), "Uitnodiging Vriendenkamp", "9:00", "17:00",
            LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("dd/MM")),
            "", "Limburg",
            "Speciaal vriendenkamp georganiseerd door de regionale federatie.",
            isInbox = true, localDate = LocalDate.now().plusDays(10),
            rsvp = false
        ),
        Event(
            UUID.randomUUID(), "Demonstratie Openingsdag", "13:00", "15:00",
            LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM")),
            "Gemeenteplein", "Aalst",
            "Publieke demonstratie ter gelegenheid van de openingsdag van het sportseizoen.",
            isInbox = true, localDate = LocalDate.now().plusDays(5),
            rsvp = null
        ),
    )

    val nextTraining = Training(
        id = UUID.randomUUID(), type = "Technieken",
        startTime = "20:00", endTime = "22:00",
        date = LocalDate.now().plusDays(2),
        injuries = "Geen", sensei = "Sensei Ludo"
    )

    val trainings: MutableList<Training> = mutableListOf(
        // June 2026
        Training(UUID.randomUUID(),  "Technieken",  "20:00", "22:00", LocalDate.now().plusDays(2),   "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(), "Technieken",  "20:00", "22:00", LocalDate.now().plusDays(2),   "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(),  "Kumite",      "19:00", "21:00", LocalDate.now().minusDays(2),  "Geen",              "Sensei Ludo",  note = "Goede sessie vandaag. Gyaku-zuki combinaties zijn verbeterd."),
        Training(UUID.randomUUID(),  "Kata",        "20:00", "22:00", LocalDate.now().minusDays(4),  "Geen",              "Sensei Yuki"),
        Training(UUID.randomUUID(),  "Conditie",    "19:30", "21:00", LocalDate.now().minusDays(7),  "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(),  "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(9),  "Pols — licht",      "Sensei Yuki",  note = "Pols voelt nog wat stijf. Minder kracht gezet op stoot."),
        Training(UUID.randomUUID(), "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(9),  "Pols — licht",      "Sensei Yuki",  note = "Pols voelt nog wat stijf. Minder kracht gezet op stoot."),
        // May 2026
        Training(UUID.randomUUID(),  "Kumite",      "19:00", "21:00", LocalDate.now().minusDays(12), "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(),  "Kata",        "20:00", "22:00", LocalDate.now().minusDays(14), "Geen",              "Sensei Yuki"),
        Training(UUID.randomUUID(),  "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(16), "Schouder — licht",  "Sensei Ludo",  note = "Schouder blessure tijdens uke-waza. Volgende training voorzichtig zijn."),
        Training(UUID.randomUUID(),  "Conditie",    "19:30", "21:00", LocalDate.now().minusDays(19), "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(), "Kata",        "19:00", "21:00", LocalDate.now().minusDays(21), "Geen",              "Sensei Yuki"),
        Training(UUID.randomUUID(), "Kumite",      "20:00", "22:00", LocalDate.now().minusDays(23), "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(), "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(26), "Geen",              "Sensei Yuki"),
        Training(UUID.randomUUID(), "Conditie",    "19:30", "21:00", LocalDate.now().minusDays(28), "Knie — licht",      "Sensei Ludo",  note = "Kniepijn bij laag gedeelte kata. Vraag naar aanpassingen."),
        // April 2026
        Training(UUID.randomUUID(), "Kata",        "19:00", "21:00", LocalDate.now().minusDays(33), "Geen",              "Sensei Yuki"),
        Training(UUID.randomUUID(), "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(35), "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(), "Kumite",      "19:00", "21:00", LocalDate.now().minusDays(38), "Geen",              "Sensei Yuki"),
        Training(UUID.randomUUID(), "Conditie",    "19:30", "21:00", LocalDate.now().minusDays(40), "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(), "Technieken",  "20:00", "22:00", LocalDate.now().minusDays(42), "Rug — licht",       "Sensei Yuki",  note = "Rug pijn na conditietraining. Ibuprofen genomen."),
        Training(UUID.randomUUID(), "Kata",        "19:00", "21:00", LocalDate.now().minusDays(47), "Geen",              "Sensei Ludo"),
        Training(UUID.randomUUID(), "Kumite",      "20:00", "22:00", LocalDate.now().minusDays(49), "Geen",              "Sensei Yuki"),
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
                Techniek(UUID.randomUUID(), "Gekruis vastnemen 1 hand", "Geel", "Testing123"),
                Techniek(UUID.randomUUID(), "Parallel vastnemen 1 hand", "Geel", ""),
                Techniek(UUID.randomUUID(), "Vastnemen 2 handen op 1", "Geel", ""),
                Techniek(
                    UUID.randomUUID(), "Wurging met 1 hand en hoekstoot", "Geel",
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
                Techniek(UUID.randomUUID(), "Techniek 1", "Oranje", ""),
                Techniek(UUID.randomUUID(), "Techniek 2", "Oranje", ""),
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
        LexiconEntry(UUID.randomUUID(), "Rei", "Buiging / Groet", "Rei is de formele buiging die respect uitdrukt tegenover de sensei, de dojo en de trainingspartner."),
        LexiconEntry(UUID.randomUUID(), "Dojo", "Trainingsplaats", "De dojo is de ruimte waar karate beoefend wordt. Het woord betekent letterlijk 'plaats van de weg'."),
        LexiconEntry(UUID.randomUUID(), "Sensei", "Leraar / Meester", "Sensei betekent letterlijk 'degene die voor is gegaan'. Het is de titel voor een karate-instructeur."),
        LexiconEntry(UUID.randomUUID(), "Karate", "Lege hand", "Karate is een Japanse vechtkunst waarbij gevochten wordt zonder wapens, enkel met de lege hand."),
        LexiconEntry(UUID.randomUUID(), "Kiai", "Strijdkreet"),
        LexiconEntry(UUID.randomUUID(), "Kata", "Patroon / Vorm", "Een kata is een vaste reeks van technieken die solo uitgevoerd wordt en een gesimuleerde gevechtsscenario voorstelt."),
        LexiconEntry(UUID.randomUUID(), "Kumite", "Gevecht / Sparring", "Kumite is het vrije of vaste sparren met een partner, waarbij aanvals- en afweertechnieken worden gecombineerd."),
        LexiconEntry(UUID.randomUUID(), "Mawashi", "Cirkelbeweging"),
        LexiconEntry(UUID.randomUUID(), "Tsuki", "Stoot"),
        LexiconEntry(UUID.randomUUID(), "Geri", "Trap"),
        LexiconEntry(UUID.randomUUID(), "Uke", "Afweer / Blok"),
        LexiconEntry(UUID.randomUUID(), "Mae", "Voor / Voorwaarts"),
        LexiconEntry(UUID.randomUUID(), "Yoko", "Zijwaarts"),
        LexiconEntry(UUID.randomUUID(), "Ushiro", "Achterwaarts"),
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

    // GeoJSON: polygons roughly covering each event city
    val dojoZones: List<Feature> = listOf(
        Feature.fromGeometry(
            Polygon.fromLngLats(listOf(listOf(
                Point.fromLngLat(3.7077, 51.0919), Point.fromLngLat(3.7277, 51.0919),
                Point.fromLngLat(3.7277, 51.1119), Point.fromLngLat(3.7077, 51.1119),
                Point.fromLngLat(3.7077, 51.0919)
            )))
        ),
        Feature.fromGeometry(
            Polygon.fromLngLats(listOf(listOf(
                Point.fromLngLat(3.7128, 51.0327), Point.fromLngLat(3.7328, 51.0327),
                Point.fromLngLat(3.7328, 51.0527), Point.fromLngLat(3.7128, 51.0527),
                Point.fromLngLat(3.7128, 51.0327)
            )))
        ),
        Feature.fromGeometry(
            Polygon.fromLngLats(listOf(listOf(
                Point.fromLngLat(3.2147, 51.1993), Point.fromLngLat(3.2347, 51.1993),
                Point.fromLngLat(3.2347, 51.2193), Point.fromLngLat(3.2147, 51.2193),
                Point.fromLngLat(3.2147, 51.1993)
            )))
        ),
        Feature.fromGeometry(
            Polygon.fromLngLats(listOf(listOf(
                Point.fromLngLat(4.3925, 51.2094), Point.fromLngLat(4.4125, 51.2094),
                Point.fromLngLat(4.4125, 51.2294), Point.fromLngLat(4.3925, 51.2294),
                Point.fromLngLat(4.3925, 51.2094)
            )))
        ),
        Feature.fromGeometry(
            Polygon.fromLngLats(listOf(listOf(
                Point.fromLngLat(3.3324235, 51.1675755),
                Point.fromLngLat(3.3327296, 51.1675733),
                Point.fromLngLat(3.3327332, 51.1677083),
                Point.fromLngLat(3.3331852, 51.1676971),
                Point.fromLngLat(3.3331763, 51.1675621),
                Point.fromLngLat(3.3331176, 51.1675554),
                Point.fromLngLat(3.3331034, 51.1672987),
                Point.fromLngLat(3.3330304, 51.1672999),
                Point.fromLngLat(3.3330339, 51.1671425),
                Point.fromLngLat(3.3322082, 51.1671503),
                Point.fromLngLat(3.3322171, 51.1672485),
                Point.fromLngLat(3.3324182, 51.1672363),
                Point.fromLngLat(3.3324235, 51.1675755)
            )))
        )
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
