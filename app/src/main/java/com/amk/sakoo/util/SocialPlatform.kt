package com.amk.sakoo.util

import androidx.annotation.DrawableRes
import com.amk.sakoo.R

sealed class SocialPlatform(
    val id: String,
    val displayName: String,
    @DrawableRes val iconRes: Int,
    val serviceIds: List<String>
) {
    object Instagram : SocialPlatform(
        id = "instagram",
        displayName = "اینستاگرام",
        iconRes = R.drawable.ic_instagram,
        serviceIds = listOf(
            "21", "60", "124", "175", "178", "208", "406", "492", "493", "554",
            "694", "697", "713", "719", "721", "723", "815", "825", "849", "850",
            "888", "1182", "1218", "1273", "1298", "1306", "1316", "1318", "1343",
            "1428", "1439", "1650", "1703", "1706", "1708", "1709", "1710", "1771",
            "1775", "1776", "1894", "1944", "1951", "1961", "1981", "1988", "1992",
            "2004", "2063", "2085", "2093", "2095", "2098", "2129", "2131", "2136",
            "2158", "2159", "2175", "2176", "2177", "2178", "2180", "2188", "2191",
            "2208", "2209", "2241", "2268", "2354", "2368", "2408", "2420", "2531",
            "2535", "2539", "2541", "2582", "2626", "2633", "2720", "2778", "2788",
            "2789", "2790", "2800", "2802", "2804", "2805", "2806", "2807", "2809",
            "2810"
        )
    )

    object Telegram : SocialPlatform(
        id = "telegram",
        displayName = "تلگرام",
        iconRes = R.drawable.ic_telegram,
        serviceIds = listOf(
            "17", "38", "45", "51", "223", "347", "595", "599", "615", "637", "638",
            "735", "790", "804", "861", "949", "950", "1040", "1050", "1117", "1119",
            "1151", "1152", "1153", "1154", "1155", "1159", "1160", "1220", "1232",
            "1243", "1244", "1245", "1249", "1250", "1252", "1253", "1276", "1277",
            "1294", "1311", "1399", "1405", "1407", "1409", "1410", "1411", "1413",
            "1414", "1456", "1457", "1458", "1461", "1462", "1464", "1465",
            "1467", "1472", "1544", "1576", "1646", "1647", "1913", "1914", "1915",
            "1953", "1954", "1969", "1973", "1985", "1990", "2013", "2046", "2066",
            "2076", "2077", "2087", "2096", "2097", "2161", "2203", "2223", "2264",
            "2357", "2366", "2367", "2380", "2390", "2396", "2536", "2586", "2587",
            "2588", "2589", "2591", "2599", "2601", "2603", "2604", "2605", "2606",
            "2607", "2624", "2629", "2630", "2649", "2682", "2719", "2725", "2726",
            "2727", "2728", "2732", "2766", "2768", "2769", "2770", "2771", "2772",
            "2773", "2774", "2780"
        )
    )

    object Rubika : SocialPlatform(
        id = "rubika",
        displayName = "روبیکا",
        iconRes = R.drawable.ic_rubika,
        serviceIds = listOf(
            "1305", "1312", "1339", "1419", "1442", "1443", "1901", "1902", "2047",
            "2048", "2049", "2137", "2147", "2410", "2614", "2615", "2616", "2617",
            "2618", "2619", "2620", "2621", "2622", "2632", "2641", "2811", "2817",
            "2819", "2820", "2821", "2822", "2823", "2824"
        )
    )

    object Aparat : SocialPlatform(
        id = "aparat",
        displayName = "آپارات",
        iconRes = R.drawable.ic_aparat,
        serviceIds = listOf(
            "337", "1469", "1491", "1692", "1695", "1696", "1893", "2371", "2781"
        )
    )

    object Twitter : SocialPlatform(
        id = "twitter",
        displayName = "توییتر (X)",
        iconRes = R.drawable.ic_x,
        serviceIds = listOf(
            "597", "851", "1200", "1257", "1258", "1415", "1481", "1719", "1720",
            "1721", "1722", "1870", "1919", "1920", "1921", "1982", "2041", "2042",
            "2546"
        )
    )

    object YouTube : SocialPlatform(
        id = "youtube",
        displayName = "یوتیوب",
        iconRes = R.drawable.ic_youtube,
        serviceIds = listOf(
            "97", "105", "577", "1205", "1216", "1506", "1668", "1670", "1679",
            "1683", "1686", "1687", "1688", "1922", "1923", "1962", "1976", "1984",
            "2019", "2089", "2090", "2091", "2206", "2213", "2570", "2782", "2796"
        )
    )

    object TikTok : SocialPlatform(
        id = "tiktok",
        displayName = "تیک تاک",
        iconRes = R.drawable.ic_tiktok,
        serviceIds = listOf(
            "424", "855", "893", "894", "1231", "1468", "1730", "1731", "1733",
            "1734", "1735", "1851", "1852", "1886", "1887", "1888", "1924", "1925",
            "2259", "2260", "2261", "2353", "2567"
        )
    )

    object Facebook : SocialPlatform(
        id = "facebook",
        displayName = "فیسبوک",
        iconRes = R.drawable.ic_facebook,
        serviceIds = listOf(
            "27", "161", "162", "1260", "1396", "1738", "1741", "1742", "1743",
            "1744", "1745", "1746", "1747", "1748", "2124", "2125", "2127", "2192",
            "2193", "2267", "2338", "2595", "2797"
        )
    )

    object Eitaa : SocialPlatform(
        id = "eitaa",
        displayName = "ایتا",
        iconRes = R.drawable.ic_eitaa,
        serviceIds = listOf(
            "1652", "2051", "2052", "2086", "2155", "2784", "2786"
        )
    )

    object WhatsApp : SocialPlatform(
        id = "whatsapp",
        displayName = "واتساپ",
        iconRes = R.drawable.ic_whatsapp,
        serviceIds = listOf(
            "1938", "1979", "1997", "1998", "1999", "2000", "2001", "2002",
            "2003", "2528"
        )
    )

    object Threads : SocialPlatform(
        id = "threads",
        displayName = "تردز",
        iconRes = R.drawable.ic_threads,
        serviceIds = listOf(
            "1493", "1494", "1561", "1644", "1645"
        )
    )

    object Discord : SocialPlatform(
        id = "discord",
        displayName = "دیسکورد",
        iconRes = R.drawable.ic_add_shopping_cart,
        serviceIds = listOf(
            "2549", "2551"
        )
    )

    object Spotify : SocialPlatform(
        id = "spotify",
        displayName = "اسپاتیفای",
        iconRes = R.drawable.ic_spotify,
        serviceIds = listOf(
            "791", "793", "951", "1398", "1452", "1453", "1455"
        )
    )

    object SoundCloud : SocialPlatform(
        id = "soundcloud",
        displayName = "ساندکلاد",
        iconRes = R.drawable.ic_add_shopping_cart,
        serviceIds = listOf(
            "31", "42", "44"
        )
    )

    object Twitch : SocialPlatform(
        id = "twitch",
        displayName = "توییچ",
        iconRes = R.drawable.ic_add_shopping_cart,
        serviceIds = listOf(
            "1539", "1540", "1541", "1542", "2123"
        )
    )

    object Likee : SocialPlatform(
        id = "likee",
        displayName = "لایکی",
        iconRes = R.drawable.ic_likee,
        serviceIds = listOf(
            "49", "52", "87", "171", "1445", "1446"
        )
    )

    object Clubhouse : SocialPlatform(
        id = "clubhouse",
        displayName = "کلاب‌ هاوس",
        iconRes = R.drawable.ic_clubhouse,
        serviceIds = listOf(
            "617"
        )
    )

    object Pinterest : SocialPlatform(
        id = "pinterest",
        displayName = "پینترست",
        iconRes = R.drawable.ic_pinterest,
        serviceIds = listOf(
            "2092"
        )
    )

    object Bale : SocialPlatform(
        id = "bale",
        displayName = "بله",
        iconRes = R.drawable.ic_bale,
        serviceIds = listOf(
            "2579"
        )
    )
}

val allPlatforms = listOf(
    SocialPlatform.Instagram,
    SocialPlatform.Telegram,
    SocialPlatform.Rubika,
    SocialPlatform.Aparat,
    SocialPlatform.Twitter,
    SocialPlatform.YouTube,
    SocialPlatform.TikTok,
    SocialPlatform.Facebook,
    SocialPlatform.Eitaa,
    SocialPlatform.WhatsApp,
    SocialPlatform.Threads,
//    SocialPlatform.Discord,
    SocialPlatform.Spotify,
//    SocialPlatform.SoundCloud,
//    SocialPlatform.Twitch,
    SocialPlatform.Likee,
    SocialPlatform.Clubhouse,
    SocialPlatform.Pinterest,
    SocialPlatform.Bale
)