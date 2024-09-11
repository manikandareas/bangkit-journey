// File: Main.kt

// ==============================
// Import and Packages
// ==============================
// Import package-level function or other classes
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

// ==============================
// Class and Object
// ==============================
// Open : Digunakan untuk kelas yang dapat diwarisi
open class Person(val name: String, var age: Int) {

    // Open function yang dapat di-override
    open fun introduce() {
        println("Hello, my name is $name and I am $age years old.")
    }
}

// ==============================
// Inheritance
// ==============================
// Primary constructor dari kelas Dog
open class Animal {
    var name: Any by DelegateName()

    constructor(name: String) {
        this.name = name
    }
    // Fungsi yang akan di-override
    open fun sound() {
        println("$name makes a sound")
    }
}
                        // Inheritance
class Dog(name: String) : Animal(name) {
    // Override fungsi sound dari kelas Animal
    override fun sound() {
        println("$name barks")
    }
}

// ==============================
// Abstract Class
// ==============================
// Abstract class yang memiliki fungsi untuk di-override
abstract class Vehicle(val name: String) {
    // Abstract function yang harus di-override
    abstract fun startEngine()
}

// Concrete class yang mewarisi abstract class Vehicle
class Car(name: String) : Vehicle(name) {
    // Implementasi fungsi startEngine
    override fun startEngine() {
        println("$name engine started.")
    }
}

// ==============================
// Interface
// ==============================
// Interface yang memiliki fungsi yang harus di-implementasi
interface Drivable {
    fun drive()
}

// Concrete class yang mengimplementasi interface Drivable
class Bicycle : Drivable {
    // Implementasi fungsi drive
    override fun drive() {
        println("Riding the bicycle.")
    }
}

// ==============================
// Data Class
// ==============================
// Data class yang memiliki fungsi toString(), equals(), hashCode(), dan copy()
// data class sangat cocok untuk membuat objek yang hanya berisi properti (fields)
// dan tidak memerlukan fungsi atau logika tambahan. Misalnya, objek untuk menyimpan
// informasi pengguna, produk, atau pengaturan.
data class User(val name: String, val age: Int)

// ==============================
// Sealed Class
// ==============================
// Sealed class digunakan untuk membatasi pewarisan kelas
// Sealed class hanya dapat diwarisi oleh kelas yang berada di file yang sama
sealed class Result {
    class Success(val data: String) : Result()
    class Error(val message: String) : Result()
}

// ==============================
// Object Declaration and Object Expression
// ==============================
// Object Declaration, digunakan untuk membuat objek singleton di Kotlin
object Database {
    val name = "MainDB"
    fun connect() {
        println("Connected to $name")
    }
}

// ==============================
// Companion Object
// ==============================
// Companion object digunakan untuk membuat fungsi atau properti statis di dalam kelas
class MathUtils {
    companion object {
        fun square(x: Int) = x * x
    }
}

// ==============================
// Extensions
// ==============================
// Extension function digunakan untuk menambahkan fungsi baru ke kelas yang sudah ada
fun String.addHello(): String = "Hello $this"

// ==============================
// Overloading
// ==============================
// Overloading digunakan untuk membuat fungsi dengan nama yang sama tetapi dengan parameter yang berbeda
class Calculator {
    fun add(a: Int, b: Int): Int = a + b
    fun add(a: Double, b: Double): Double = a + b
}

// ==============================
// Nullable Receiver
// ==============================
// Nullable receiver, digunakan untuk menambahkan fungsi ke tipe data nullable
fun String?.printLength() {
    if (this != null) {
        println("Length of string: ${this.length}")
    } else {
        println("String is null")
    }
}

// ==============================
// Property Delegation
// ==============================
class DelegatedExample {
    var p: String by Delegates.observable("Initial value") { prop, old, new ->
        println("${prop.name} changed from $old to $new")
    }
}

class DelegateName {
    private var value: Any = "Default"

    operator fun getValue(classRef: Any?, property: KProperty<*>) : Any {
        println("Fungsi ini sama seperti getter untuk properti ${property.name} pada class $classRef")
        return value
    }

    operator fun setValue(classRef: Any?, property: KProperty<*>, newValue: Any){
        println("Fungsi ini sama seperti setter untuk properti ${property.name} pada class $classRef")
        println("Nilai ${property.name} dari: $value akan berubah menjadi $newValue")
        value = newValue
    }
}

// ==============================
// `lateinit` Keyword
// ==============================
class LateInitExample {
    lateinit var data: String

    fun initializeData(value: String) {
        data = value
    }

    fun printData() {
        if (::data.isInitialized) {
            println(data)
        } else {
            println("Data is not initialized")
        }
    }
}

// ==============================
// `lazy` Keyword
// ==============================
val lazyValue: String by lazy {
    println("Computed only once!")
    "Hello, Lazy!"
}

// ==============================
// Exception Handling
// ==============================
fun exceptionExample() {
    try {
        val result = 10 / 0
    } catch (e: ArithmeticException) {
        println("Caught an ArithmeticException: ${e.message}")
    } finally {
        println("Finally block always executes")
    }
}

// ==============================
// Main Function
// ==============================
fun main() {
    // Class and Object Example
    val person = Person("Alice", 30)
    person.introduce()

    // Inheritance Example
    val dog = Dog("Buddy")
    dog.sound()
    dog.n

    // Abstract Class Example
    val myCar = Car("Toyota")
    myCar.startEngine()

    // Interface Example
    val bike = Bicycle()
    bike.drive()

    // Data Class Example
    val user1 = User("Alice", 25)
    println(user1)

    // Sealed Class Example
    val result: Result = Result.Success("Data Loaded")
    when (result) {
        is Result.Success -> println("Success: ${result.data}")
        is Result.Error -> println("Error: ${result.message}")
    }

    // Object Declaration Example
    Database.connect()

    // Companion Object Example
    println(MathUtils.square(5))

    // Extensions Example
    println("World".addHello())

    // Overloading Example
    val calculator = Calculator()
    println(calculator.add(2, 3))
    println(calculator.add(2.0, 3.5))

    // Nullable Receiver Example
    val nullableString: String? = null
    nullableString.printLength()
    "Kotlin".printLength()

    // Property Delegation Example
    val delegatedExample = DelegatedExample()
    delegatedExample.p = "New value"

    // `lateinit` Keyword Example
    val lateInitExample = LateInitExample()
    lateInitExample.initializeData("Initialized!")
    lateInitExample.printData()

    // `lazy` Keyword Example
    println(lazyValue)

    // Exception Handling Example
    exceptionExample()
}
