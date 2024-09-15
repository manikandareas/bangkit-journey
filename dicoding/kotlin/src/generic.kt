// 1. Generic Class
class Box<T>(private val content: T) {
    fun getContent(): T = content
}

// 2. Generic Function
fun <T> printContent(item: T) {
    println("Content: $item")
}

// 3. Generic Interface
interface Storage<T> {
    fun add(item: T)
    fun getAll(): List<T>
}

class ListStorage<T> : Storage<T> {
    private val items = mutableListOf<T>()

    override fun add(item: T) {
        items.add(item)
    }

    override fun getAll(): List<T> = items
}

// 4. Type Constraints (Type Bounds)
fun <T : Comparable<T>> findMax(list: List<T>): T? {
    if (list.isEmpty()) return null
    var max = list[0]
    for (item in list) {
        if (item > max) {
            max = item
        }
    }
    return max
}

// 5. Covariance (out keyword)
class Producer<out T>(private val value: T) {
    fun get(): T = value
}

// 6. Contravariance (in keyword)
class Consumer<in T> {
    fun consume(item: T) {
        println("Consumed: $item")
    }
}

// 7. Reified Types (inline functions)
inline fun <reified T> isTypeMatch(item: Any): Boolean {
    return item is T
}

// 8. Star Projections
fun printList(list: List<*>) {
    for (item in list) {
        println(item)
    }
}

// 9. Extension Function with Generics
fun <T> List<T>.printAllItems() {
    for (item in this) {
        println(item)
    }
}

// Main function to demonstrate the use cases
fun main() {
    // 1. Generic Class
    val intBox = Box(123)
    val stringBox = Box("Hello, Generics!")
    println(intBox.getContent())
    println(stringBox.getContent())

    // 2. Generic Function
    printContent(42)
    printContent("Kotlin")

    // 3. Generic Interface
    val stringStorage: Storage<String> = ListStorage()
    stringStorage.add("Item 1")
    stringStorage.add("Item 2")
    println(stringStorage.getAll())

    // 4. Type Constraints
    val numbers = listOf(1, 3, 2, 8, 5)
    println("Max number: ${findMax(numbers)}")

    // 5. Covariance
    val producer: Producer<Number> = Producer(123)
    val intProducer: Producer<Number> = producer // Valid because of 'out'
    println(intProducer.get())

    // 6. Contravariance
    val consumer: Consumer<Number> = Consumer()
    val doubleConsumer: Consumer<Double> = consumer // Valid because of 'in'
    doubleConsumer.consume(3.14)

    // 7. Reified Types
    println("Is type match: ${isTypeMatch<String>("Kotlin")}")

    // 8. Star Projections
    printList(listOf(1, "Hello", 3.14))

    // 9. Extension Function with Generics
    val list = listOf("A", "B", "C")
    list.printAllItems()
}
