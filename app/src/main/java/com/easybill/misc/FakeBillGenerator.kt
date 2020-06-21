package com.easybill.misc

import com.easybill.data.model.Bill
import com.easybill.data.model.BillHeader
import com.easybill.data.model.BillItem
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.random.Random

var RandomNumberGenerator = Random(42)

fun generateFakeBills(count: Int): MutableList<Bill> {
    val list = mutableListOf<Bill>()
    for (i in 1..count)
        list.add(generateFakeBill())
    return list
}

fun generateFakeBill(): Bill {
    val bill = Bill()
    bill.items = generateFakeItems(RandomNumberGenerator.nextInt(1, 25))
    bill.header = generateFakeHeader()
    return bill
}

fun generateFakeHeader(): BillHeader {
    val header = BillHeader()
    header.address = addresses.random()
    header.companyName = companyNames.random()
    header.dateTime = LocalDateTime.ofEpochSecond(RandomNumberGenerator
        .nextLong(1498002828, 1592697228), 0, ZoneOffset.UTC)
    val bla = header.getDateTimeAsString()
    return header
}

// from http://www.foxpoint.de/randomAdresses/index.php
private val addresses = listOf(
    "Zeppelinstrasse 117, 91522 Ansbach",
    "Höhenweg 286, 99718 Greußen",
    "Burgweg 47, 37284 Waldkappel",
    "Breslauer Strasse 159a, 06642 Nebra (Unstrut)",
    "Waldweg 254a, 46325 Borken",
    "Friedensstrasse 86, 88069 Tettnang",
    "Tannenstrasse 300, 35619 Braunfels",
    "Lessingstrasse 119, 19395 Plau am See",
    "Lindenweg 235, 96450 Coburg",
    "Danziger Strasse 171, 94086 Bad Griesbach i.Rottal",
    "Schwarzer Weg 117, 76703 Kraichtal",
    "Akazienweg 181, 52156 Monschau",
    "Höhenweg 171, 69434 Hirschhorn (Neckar)",
    "Lessingstrasse 230, 76530 Baden-Baden",
    "Mühlenweg 245, 33142 Büren",
    "Eichendorffstrasse 166, 59320 Ennigerloh",
    "Bergweg 153, 22926 Ahrensburg",
    "Leipziger Strasse 252b, 72379 Hechingen",
    "Schulstrasse 163, 31832 Springe",
    "Richard-Wagner-Strasse 130, 39218 Schönebeck (Elbe)",
    "Fichtenweg 12, 04874 Belgern-Schildau",
    "Schubertstrasse 151, 71540 Murrhardt",
    "Alte Dorfstrasse 221, 14612 Falkensee",
    "Schulweg 148, 07937 Zeulenroda-Triebes",
    "Weinbergstrasse 62c, 66482 Zweibrücken",
    "Wilhelmstrasse 241, 95671 Bärnau",
    "Flurstrasse 44, 52531 Übach-Palenberg",
    "Siedlung 121, 29614 Soltau",
    "Gutenbergstrasse 260, 37696 Marienmünster",
    "Fasanenweg 174, 04539 Groitzsch",
    "Talstrasse 276, 58239 Schwerte an der Ruhr",
    "Gartenstrasse 163, 56751 Polch",
    "Rosenstrasse 92, 91567 Herrieden",
    "Teichstrasse 220, 35285 Gemünden (Wohra)",
    "Mittelweg 215, 91217 Hersbruck",
    "Erlenstrasse 192, 41539 Dormagen",
    "Gerhart-Hauptmann-Strasse 206, 30159 Hannover",
    "Kirchplatz 291, 07407 Remda-Teichel",
    "Kurze Strasse 160a, 08485 Lengenfeld",
    "Feldstrasse 31, 86368 Gersthofen",
    "Ahornstrasse 120, 53909 Zülpich",
    "Kiefernweg 271, 56338 Braubach",
    "Erlenweg 18, 34393 Grebenstein",
    "Am Bahnhof 86, 67752 Wolfstein",
    "Wacholderweg 22, 96047 Bamberg",
    "Klosterstrasse 195, 06647 Bad Bibra",
    "Nordstrasse 92, 37574 Einbeck",
    "Brückenstrasse 263, 91275 Auerbach i.d.OPf.",
    "Fasanenweg 296, 83278 Traunstein",
    "Bergweg 256, 07349 Lehesten",
    "Grüner Weg 211a, 79664 Wehr",
    "Asternweg 83, 17153 Stavenhagen, Reuterstadt",
    "Asternweg 51, 95659 Arzberg",
    "Schulstrasse 181d, 59368 Werne",
    "Lindenweg 244, 63179 Obertshausen",
    "Sudetenstrasse 15a, 94486 Osterhofen",
    "Richard-Wagner-Strasse 148a, 09599 Freiberg",
    "Kiefernweg 15, 49661 Cloppenburg",
    "Birkenstrasse 158, 72574 Bad Urach",
    "Im Winkel 267, 56269 Dierdorf",
    "Weiherstrasse 44, 39646 Oebisfelde-Weferlingen",
    "Finkenweg 252, 96047 Bamberg",
    "Weststrasse 246, 97769 Bad Brückenau",
    "Sandweg 13, 06502 Thale",
    "Teichstrasse 48, 99947 Bad Langensalza",
    "Fliederweg 44, 35260 Stadtallendorf",
    "Brunnenweg 213, 92249 Vilseck",
    "Brunnenstrasse 237, 96358 Teuschnitz",
    "Nordstrasse 48, 52441 Linnich",
    "Brunnenstrasse 193, 24937 Flensburg",
    "Ulmenweg 178b, 02681 Wilthen",
    "Kantstrasse 7, 06406 Bernburg (Saale)",
    "Kantstrasse 163, 91710 Gunzenhausen",
    "Daimlerstrasse 274, 25421 Pinneberg",
    "Goethestrasse 269b, 72393 Burladingen",
    "Friedensstrasse 212, 25899 Niebüll",
    "Marienstrasse 286, 32361 Preußisch Oldendorf",
    "Bismarckstrasse 202, 01816 Bad Gottleuba-Berggießhübel",
    "Blumenweg 35, 07619 Schkölen",
    "Mühlenweg 209, 79761 Waldshut-Tiengen",
    "Danziger Strasse 15, 71686 Remseck am Neckar",
    "Amselweg 299, 95679 Waldershof",
    "Bergweg 202, 52349 Düren",
    "Kurze Strasse 196, 66538 Neunkirchen",
    "Albert-Schweitzer-Strasse 34, 65239 Hochheim am Main",
    "Eschenweg 118, 59269 Beckum",
    "Alte Dorfstrasse 275, 01855 Sebnitz",
    "Römerstrasse 87, 89597 Munderkingen",
    "Nordstrasse 94, 71332 Waiblingen",
    "Stettiner Strasse 225, 89312 Günzburg",
    "Breslauer Strasse 140, 99423 Weimar",
    "Birkenstrasse 136, 91781 Weißenburg i.Bay.",
    "Römerstrasse 135, 98708 Gehren",
    "Talstrasse 36c, 02929 Rothenburg/O.L.",
    "Lilienweg 156, 97645 Ostheim v.d.Rhön",
    "Brunnenweg 138c, 91550 Dinkelsbühl",
    "Königsberger Strasse 243, 53111 Bonn",
    "Teichstrasse 43, 35279 Neustadt (Hessen)",
    "Buchenstrasse 238, 96145 Seßlach",
    "Grabenstrasse 209, 79837 St. Blasien"
)

/* 1. go to https://de.wikipedia.org/wiki/Kategorie:Einzelhandelsunternehmen_(Deutschland)
 * 2. open your browsers developer-console
 * 3. enter Array.from(document.getElementsByTagName('li')).map(e => e.innerText)
 * 4. enjoy the time-save
 */
private val companyNames = listOf(
    "Ahg Autohandelsgesellschaft", 
    "Akzenta",
    "Aldi",
    "AllerWeltHaus",
    "Allkauf",
    "Alnatura",
    "Amper Einkaufs Zentrum",
    "ANWR GROUP",
    "Ara AG",
    "Arcandor",
    "Arko ",
    "ARO Heimtextilien",
    "Astroh",
    "Auto-Teile-Unger",
    "B1 Discount Baumarkt",
    "Bartels-Langness",
    "Basic AG",
    "Bauking",
    "Baumarkt Direkt",
    "Bayerische Lagerversorgung",
    "Bayer-Kaufhaus",
    "Ludwig Beck am Rathauseck",
    "Becker + Flöge",
    "Bio Company",
    "Blume 2000",
    "Blumen Risse",
    "Blumen-Hanisch",
    "Boesner-Unternehmensgruppe",
    "Bofrost",
    "Bolle ",
    "Bonus-Markt",
    "Bora Computer Gruppe",
    "Braun Möbel-Center",
    "Breuninger",
    "Brück & Sohn",
    "Budnikowsky",
    "Bünting-Gruppe",
    "Butlers ",
    "C&A",
    "CAP ",
    "Carsch & Co",
    "Coma ",
    "Combi ",
    "Coop eG",
    "Cramer & Meermann",
    "DeFaKa",
    "Deichmann SE",
    "Dennree",
    "Depot ",
    "Dirk Rossmann GmbH",
    "Distributa",
    "Dm-drogerie markt",
    "Domäne Einrichtungsmärkte",
    "Douglas Holding",
    "Drogerie Linke",
    "Dursty Getränkemärkte",
    "D&W",
    "EBay",
    "Ebl-naturkost",
    "Edeka",
    "Edeka Nord",
    "Edeka Südwest",
    "Edgar Schmidt",
    "Dresden-Budapest",
    "Einrichtungspartnerring VME",
    "Eismann Tiefkühl-Heimservice",
    "Engelhorn KGaA",
    "Ernsting’s family",
    "Etsy",
    "Eurobaustoff Handelsgesellschaft",
    "Euromaster",
    "Extra ",
    "Famila",
    "Fantastic Shop",
    "Feinkost Böhm",
    "Feiyr",
    "Feneberg Lebensmittel",
    "Fielmann",
    "Finke ",
    "Fischer Automobile",
    "Fotohaus Preim",
    "Frischeparadies",
    "Fristo",
    "Modehaus Garhammer",
    "Gebrüder Götz",
    "Getränke Ahlers",
    "Getränkewelt",
    "G. H. Rehfeld & Sohn",
    "Glitterhouse",
    "Globetrotter Ausrüstung",
    "Globus Holding",
    "Ludwig Görtz",
    "Gottlieb-Handelsgesellschaft",
    "Gummi-Mayer"
)

fun generateFakeItems(count: Int): MutableList<BillItem> {
    val items = mutableListOf<BillItem>()
    for (i in 1..count)
        items.add(generateFakeItem())
    return items
}

fun generateFakeItem(): BillItem {
    val item = BillItem()

    val tmp = RandomNumberGenerator.nextInt(0, 1000)
    item.price = when {
        tmp < 1 // 0.1% chance for price in [2000.0, 10,000.0)
        -> RandomNumberGenerator.nextDouble(2000.0, 5000.0)
        tmp < 10 // 0.9% chance for price in [1000.0, 2000.0)
        -> RandomNumberGenerator.nextDouble(1000.0, 2000.0)
        tmp < 50 // 4% chance for price in [500.0, 1000.0)
        -> RandomNumberGenerator.nextDouble(500.0, 1000.0)
        tmp < 100 // 5% chance for price in [200.0, 500.0)
        -> RandomNumberGenerator.nextDouble(200.0, 500.0)
        tmp < 200 // 10% chance for price in [100.0, 200.0)
        -> RandomNumberGenerator.nextDouble(100.0, 200.0)
        tmp < 400 // 20% chance for price in [30.0, 100.0)
        -> RandomNumberGenerator.nextDouble(30.0, 100.0)
        tmp < 600 // 20% chance for price in [10.0, 30.0)
        -> RandomNumberGenerator.nextDouble(10.0, 30.0)
        tmp < 900 // 30% chance for price in [1.0, 10.0)
        -> RandomNumberGenerator.nextDouble(1.0, 10.0)
        // 10% chance for price in [0.05, 1.0)
        else -> RandomNumberGenerator.nextDouble(0.05, 200.0)
    }
    item.amount = RandomNumberGenerator.nextInt(1, 10).toDouble()
    item.name = itemNames.random()
    return item
}

/* 1. go to https://www.idealo.de/preisvergleich/MainSearchProductCategory/100oE0oJ4.html
 * 2. open your browsers developer-console
 * 3. enter Array.from(document.getElementsByClassName('offerList-item-description-title')).map(e => e.innerText)
 * 4. enjoy the time-save
 */
private val itemNames = listOf(
    "Astra HD+ Verlängerung für 12 Monate", 
    "Braun IRT 6520 ThermoScan 7",
    "Samsung 860 Evo 1TB 2.5",
    "Bode Sterillium Lösung (500 ml)",
    "Nike Woven Hooded Tracksuit (BV3025) black/white/black",
    "OnePlus 7 Pro 128GB 6GB Mirror Grey",
    "Microsoft Office 2019 Home & Student (Multi) (ESD)",
    "Huawei Watch GT 2 46mm Classic",
    "De'Longhi Pinguino PAC EL98 ECO Realfeel",
    "HP Nr. 302XL schwarz (F6U68AE)",
    "3M Medica Atemschutzmaske FFP3 mit Cool-Flow Ventil 9332",
    "Philips 55OLED804",
    "HP 250 G7 (6HM83ES)",
    "Amazon Echo (3. Generation) Anthrazit Stoff",
    "Klarstein IceTower",
    "Samsung HW-Q70R",
    "Amazon Fire TV Cube",
    "Xiaomi Mi Smart Compact Projector",
    "Huawei P40 Pro Black",
    "VidaXL Fahrradanhänger",
    "El Fuego Portland XL (AY 3172)",
    "Reebok Club C 85 intense white/green",
    "Apple iPhone X 64GB space grau",
    "Sony KD-65XF9005",
    "Comfee' MPPH-09CRN7",
    "Singer Heavy Duty 4423",
    "Bode Sterillium Classic Pure Lösung (1000 ml)",
    "Logitech B525 HD",
    "3M Medica Atemschutzmaske 8822 Klassik Schutzstufe FFP2",
    "Greenworks GDC40",
    "Tefal Jamie Oliver Pfanne 28 cm (E85606)",
    "Asus ROG Strix B550-E Gaming",
    "Hisense RS670N4BC2",
    "Nikon Nikkor Z 20mm f1.8 S",
    "Saeco SM5471/10 PicoBaristo",
    "Shelly 1 WiFi",
    "LEGO Creator Expert - Fiat 500 (10271)",
    "Samsung GQ65Q70TGT",
    "Xiaomi Mi 10 128GB Twilight Grey",
    "Sony KD-55XF9005",
    "Samsung Galaxy Watch Active 2 44mm Edelstahl LTE schwarz",
    "Samsung Galaxy Tab S5e 128GB WiFi silber",
    "Samsung Galaxy Tab A 10.1 64GB WiFi gold (2019)",
    "Sony PlayStation 4 (PS4) Slim 1TB + 2 Controller",
    "blink XT2-2",
    "Asus ZenBook 14 (UX434FAC-A5164T)",
    "iRobot Roomba 981",
    "Samsung GQ75Q80TGT",
    "Tolino Vision 4 HD",
    "Apple iPhone 7 128GB silber",
    "SanDisk Ultra A1 microSDXC 512GB (SDSQUAR-512G)",
    "Vans Old Skool Primary Check Black/White ( VA38G1P0S)",
    "Playmobil Back to the Future DeLorean (70317)",
    "Kingston SSDNow A2000 1TB",
    "FIFA 20 (Xbox One)",
    "Soundcore Life P2",
    "Klarstein Empire State Tower",
    "Doppler Grillmeister 250 x 200 cm anthrazit",
    "Beurer FT 85",
    "Hamax Outback grey"
)

