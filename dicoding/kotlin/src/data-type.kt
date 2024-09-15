/*
* Kotlin Data Types and Special Types
*/

typealias Arithmetic = ((Int, Int) -> Int)?
typealias NamePerson = String

// Char: Menyimpan karakter tunggal
val myChar: Char = 'A'

// String: Menyimpan urutan karakter atau teks
val myString: String = "Manik"

// Int: Menyimpan bilangan bulat 32-bit
val myInt: Int = 0

// Long: Menyimpan bilangan bulat 64-bit
val myLong: Long = 0L

// Short: Menyimpan bilangan bulat 16-bit
val myShort: Short = 0

// Byte: Menyimpan bilangan bulat 8-bit
val myByte: Byte = 0

// Float: Menyimpan angka floating-point 32-bit
val myFloat: Float = 0f

// Double: Menyimpan angka floating-point 64-bit
val myDouble: Double = 0.0

// Boolean: Menyimpan nilai true atau false
val myBool: Boolean = false

// Null Safety: Menyimpan nilai null dengan tipe data nullable
val myNull: String? = null

// Null Safety Call Operator: Memeriksa panjang string hanya jika tidak null
val length = myNull?.length

// Null Safety Elvis Operator: Memberikan nilai default jika null
val length1 = myNull?.length ?: "No Name"

// Array: Koleksi elemen yang mutable dan memiliki ukuran tetap
val myArray = arrayOf(1, 2, 3, 4, 5)

// List: Koleksi elemen yang immutable dan memiliki ukuran tetap
val myList = listOf(1, 2, 3, 4, 5)

// MutableList: Koleksi elemen yang mutable dan memiliki ukuran tetap
val myMutableList = mutableListOf(1, 2, 3, 4, 5)

// Set: Koleksi elemen unik yang immutable dan memiliki ukuran tetap
val mySet = setOf(1, 2, 3, 4, 5)

// MutableSet: Koleksi elemen unik yang mutable dan memiliki ukuran tetap
val myMutableSet = mutableSetOf(1, 2, 3, 4, 5)

// Map: Koleksi pasangan key-value yang immutable
val myMap = mapOf(1 to "One", 2 to "Two", 3 to "Three")

// MutableMap: Koleksi pasangan key-value yang mutable
val myMutableMap = mutableMapOf(1 to "One", 2 to "Two", 3 to "Three")

// Pair: Tipe data untuk menyimpan dua nilai berpasangan
val myPair: Pair<String, Int> = Pair("One", 1)

// Triple: Tipe data untuk menyimpan tiga nilai berpasangan
val myTriple: Triple<String, Int, Boolean> = Triple("One", 1, true)

// Unit: Sama dengan 'void' di bahasa lain, fungsi ini tidak mengembalikan nilai apapun
fun printMessage(): Unit {
    println("This is a message!")
}

// Nothing: Digunakan untuk menunjukkan bahwa sebuah fungsi tidak akan pernah mengembalikan nilai
fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}

// Any: Supertype dari semua tipe di Kotlin, bisa digunakan untuk menampung tipe data apapun
val anything: Any = "Can be any type"

// Enum Class: Kelas yang digunakan untuk mendefinisikan kumpulan nilai konstan
enum class DayOfWeek {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}

// Sealed Class: Digunakan untuk membatasi pewarisan kelas
// sealed class Result {
//    class Success(val data: String) : Result()
//    class Error(val error: String) : Result()
// }

// Data Class: Kelas yang digunakan untuk menyimpan data, otomatis menghasilkan fungsi seperti toString(), equals(), hashCode(), dan copy()
// data class User(val name: String, val age: Int)

// Function Types and Lambdas: Tipe data fungsi juga bisa disimpan dalam variabel
val sum: (Int, Int) -> Int = { a, b -> a + b }

// Lateinit: Inisialisasi variabel nanti, hanya dapat digunakan dengan var dan tipe non-nullable
lateinit var lateInitVar: String

// Delegated Properties: Menggunakan kata kunci 'by' untuk memberikan kontrol khusus
// val lazyValue: String by lazy {
//    "Computed only once!"
// }

// Unsigned Integer Types: Tipe bilangan bulat yang hanya bisa positif
val uInt: UInt = 10u
val uLong: ULong = 100UL

// readable number
val oneMillion = 1_000_000

// max size of integer
val maxInt = Int.MAX_VALUE

// not null assertion
val name: String? = "Manik"
val nameLength = name!!.length

// default argument
fun greet(name: String = "World") {
    println("Hello, $name!")
}

// vararg
fun greetAll(vararg names: String) {
    names.forEach { name ->
        println("Hello, $name!")
    }
}

// spread operator *
fun printNumbers(vararg numbers: Int) {
    val arrNumbers = arrayOf<Int>(1, 2, 3, 4, 5)
    val combinedArr = arrayOf(*numbers.toTypedArray(), *arrNumbers)
}




fun printAllDataType(): Unit {
    println("Char: $myChar")
    println("String: $myString")
    println("Int: $myInt")
    println("Long: $myLong")
    println("Short: $myShort")
    println("Byte: $myByte")
    println("Float: $myFloat")
    println("Double: $myDouble")
    println("Boolean: $myBool")
    println("Array: ${myArray.joinToString()}")
    println("Null Safety: $myNull")
    println("Null Safety Call Operator: $length")
    println("Null Safety Elvis Operator: $length1")
    println("List: ${myList.joinToString()}")
    println("Mutable List: ${myMutableList.joinToString()}")
    println("Set: ${mySet.joinToString()}")
    println("Mutable Set: ${myMutableSet.joinToString()}")
    println("Map: ${myMap.entries}")
    println("Mutable Map: ${myMutableMap.entries}")
    println("Pair: ${myPair.first}, ${myPair.second}")
    println("Triple: ${myTriple.first}, ${myTriple.second}, ${myTriple.third}")
    println("Lazy Value: $lazyValue")
    println("UInt: $uInt")
    println("ULong: $uLong")

    // Lib function and Lambda
    myList.fold(0) { total, next -> total + next }

    // Inline if else
    val a = 10
    val b = 20
    val max = if (sum(1,2) == 3) a else b

    val inArr = if (3 in myArray) "3 found in the array" else "oow 3 is not int in the array"

    lateInitVar = "Lateinit variable"

    val user = User("Manik", 25)
    println("User: $user")
    println("Max value: $max")
}
