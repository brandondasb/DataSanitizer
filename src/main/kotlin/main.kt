import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames
import java.io.File
import java.io.Serial

val categories = mapOf(
    "Sports & Fitness" to "fitness",
    "Clothing" to "fashion",
    "Haircare" to "hair",
    "Hair Salon" to "hair",
    "Education" to "education",
    "Accounting" to "accounting",
    "Arts & Entertainment" to "art",
    "Beauty & Spa" to "beauty",
    "Skincare" to "beauty",
    "Men's Grooming" to "beauty",
    "Property & Estate Agents" to "property",
    "Services" to "service",
    "service" to "service",
    "Travel" to "travel",
    "Hotels" to "travel",
    "Coffee Shop" to "food & drinks",
    "Bakery" to "food & drinks",
    "Food & Restaurants" to "food & drinks",
    "Baby & Child" to "baby & child",
    "Gifts" to "gift",
    "Home & Garden" to "gift",
    "Grocery Store" to "groceries",
    "Health & Medical" to "health",
)

private val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

fun main() {
    val data = File("/Users/Brandon.Milambo/firestorepush/csvjson.json").readText()

    val test = json.decodeFromString<List<Input>>(data)
    val deduplicated = test.distinctBy { it.name }
    val hasImage = deduplicated.filter { it.image.isNotBlank() }

    val outputs = hasImage.map {
        Output(
            name = it.name,
            tagline = it.tagline.takeUnless { it.isNullOrBlank() },
            phoneNumber = it.phoneNumber,
            image = it.image,
            category = it.category.map { categories[it]!! },
            website = it.website,
            address = it.address.takeUnless { it.isNullOrBlank() },
            city = null,
            location = it.addressLongLat?.let {
                val (_, longLat) = it.split("=")
                val (long, lat) = longLat.split(",")
                Output.Location(long, lat)
            }
        )
    }

    println(json.encodeToString(outputs))

//    println(deduplicated.filter { it.category[0] == "Home & Garden" })
//
//        test
//            .groupBy { it.name }
//            .filter { it.value.size > 1 }
//            .forEach { (key, value) ->
//                    println("We have duplicates for '$key'")
//                value.forEach {
//                    println("    $value")
//                }
//            }

}

@Serializable
data class Output(
    val name: String,
    val tagline: String?,
    val phoneNumber: String?,
    val image: String,
    val category: List<String>,
    val website: String?,
    val address: String?,
    val city: String?,
    val location: Location?
) {
    @Serializable
    data class Location(val longitude: String, val latitude: String)
}

@Serializable
data class Input(
    val name: String,
    val tagline: String?,
    val phoneNumber: String? = null,
    @JsonNames("galleryImages-src")
    val image: String,
    val category: List<String>,
    val website: String?,
    val address: String?,
    @JsonNames("addresLongLat-href")
    val addressLongLat: String? = null
)

//    "web-scraper-order": "1630624327-228",
//    "web-scraper-start-url": "https://blackpoundday.uk/",
//    "directory-link": "Directory",
//    "directory-link-href": "https://blackpoundday.uk/#",
//    "category-id": "Skincare",
//    "category-id-href": "https://blackpoundday.uk/listing-category/skincare/",
//    "product-link": "Yoni Yoni",
//    "product-link-href": "https://blackpoundday.uk/listing/yoni-yoni/",
//    "name": "Yoni Yoni",
//    "tagline": "Yoni Care Is Self Care",
//    "about": "",
//    "phoneNumber": "+447742435589",
//    "galleryImages-src": "",
//    "category": ["Beauty & Spa"],
//    "website": "http://www.yoniyoni.shop",
//    "openingHours": "Monday09:00 AM - 05:00 PMTuesday09:00 AM - 05:00 PMWednesday09:00 AM - 05:00 PMThursday09:00 AM - 05:00 PMFriday09:00 AM - 05:00 PM",
//    "address": "London, UK",
//    "addresLongLat": "Get Directions",
//    "addresLongLat-href": "https://www.google.com/maps?daddr=51.5073509,-0.1277583"


//current categories in apps. they
