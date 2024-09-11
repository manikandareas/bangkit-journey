import kotlin.random.Random

enum class Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}


fun controlFlow() {
    // 1. If Expression
    // If digunakan untuk mengevaluasi kondisi dan memilih blok kode yang akan dijalankan.
    val number = 10

    // If expression sederhana
    if (number > 0) {
        println("Number is positive")
    } else {
        println("Number is non-positive")
    }

    // If expression sebagai expression yang mengembalikan nilai
    val max = if (number > 0) number else 0
    println("Max: $max")

    // 2. When Expression
    // When mirip dengan switch di bahasa lain, tetapi lebih fleksibel. Dapat digunakan dengan atau tanpa argumen.
    val day = 3



    val today = Day.WEDNESDAY


    // When sebagai pengganti switch
    when (today) {
        Day.MONDAY -> println("Monday")
        Day.TUESDAY -> println("Tuesday")
        Day.WEDNESDAY -> println("Wednesday")
        Day.THURSDAY -> println("Thursday")
        Day.FRIDAY -> println("Friday")
        Day.SATURDAY -> println("Saturday")
        Day.SUNDAY -> println("Sunday")
        else -> println("Invalid day")
    }

    // When sebagai expression yang mengembalikan nilai
    val dayType = when (day) {
        6, 7 -> "Weekend"
        in 1..5 -> "Weekday"
        else -> "Invalid day"
    }
    println("Day type: $dayType")

    val value =  27
    val ranges = 10..50
    val range2 = 10.rangeTo(50) step 3

    when(value){
        in ranges -> println("value is in the range")
        !in ranges -> println("value is outside the range")
    }

    val anyType: Any = 100L
    when (anyType) {
        is Long -> println("the value has a Long type")
        is Int -> println("the value has a Int type")
        is Double -> println("the value has a Double type")
        else -> println("undefined")
    }

    // 3. For Loop
    // For digunakan untuk mengulang koleksi atau rentang nilai.

    // For loop melalui range
    for (i in 1..5) {
        println("Count: $i")
    }

    // For loop melalui array
    val fruits = arrayOf("Apple", "Banana", "Cherry")
    for (fruit in fruits) {
        println("Fruit: $fruit")
    }

    // For loop dengan index
    for (index in fruits.indices) {
        println("Fruit at index $index is ${fruits[index]}")
    }
    // For loop index with value
    for ((index, v) in ranges.withIndex()) {
        println("value $v with index $index")
    }

    // 4. While Loop
    // While digunakan untuk mengulang blok kode selama kondisinya terpenuhi.

    var count = 5
    while (count > 0) {
        println("Countdown: $count")
        count--
    }

    // 5. Do-While Loop
    // Do-while mirip dengan while tetapi blok kode dijalankan setidaknya sekali.

    var counter = 0
    do {
        println("Counter: $counter")
        counter++
    } while (counter < 3)

    // 6. Break and Continue
    // Break digunakan untuk menghentikan loop, sementara continue digunakan untuk melewatkan iterasi saat ini dan melanjutkan ke iterasi berikutnya.

    for (i in 1..5) {
        if (i == 3) break // Hentikan loop saat i == 3
        println("Loop with break: $i")
    }

    for (i in 1..5) {
        if (i == 3) continue // Lewati iterasi ini saat i == 3
        println("Loop with continue: $i")
    }

    // 7. Return
    // Return digunakan untuk mengembalikan nilai dari fungsi atau keluar dari fungsi.

    val result = sum(5, 10) // Memanggil fungsi sum dengan return
    println("Sum: $result")

    // 8. Try-Catch Block
    // Digunakan untuk menangani exception yang mungkin terjadi dalam program.

    try {
        val result = 10 / 0 // Ini akan menghasilkan ArithmeticException
        println("Result: $result")
    } catch (e: ArithmeticException) {
        println("Exception caught: Division by zero is not allowed.")
    } finally {
        println("This block always executes.")
    }

    // 9. Return with Label
    // Menggunakan label untuk mengontrol alur keluar dari loop bersarang atau fungsi.

    outer@ for (i in 1..3) {
        for (j in 1..3) {
            if (i == 2 && j == 2) break@outer // Menghentikan loop luar
            println("i = $i, j = $j")
        }
    }

    // 10. Throw Exception
    // Throw digunakan untuk melemparkan exception secara manual.

    try {
        throwException()
    } catch (e: IllegalArgumentException) {
        println("Exception caught: ${e.message}")
    }

    // 11. Try-with-resources
    // Kotlin tidak memiliki try-with-resources secara eksplisit seperti Java, tetapi menggunakan 'use' untuk otomatis menutup resource.

    // Contoh penggunaan 'use' dengan file
    try {
        val fileReader = java.io.File("example.txt").bufferedReader().use { reader ->
            println(reader.readLine())
        }
    } catch (e: java.io.IOException) {
        println("IOException caught: ${e.message}")
    }

    // 12. Inline Functions and Lambda
    // Inline function mengizinkan pemanggilan fungsi lambda yang efisien.
    // Contoh penggunaan inline function

    inlineFun {
        println("This is an inline function with lambda")
    }
}

// Contoh fungsi dengan return label
fun outerFunction() {
    outer@ for (i in 1..3) {
        for (j in 1..3) {
            if (i == 2 && j == 2) return // Menghentikan loop luar
            println("i = $i, j = $j")
        }
    }
}

// Contoh fungsi untuk melemparkan exception
fun throwException() {
    throw IllegalArgumentException("This is a manual exception")
}

// Contoh fungsi inline
inline fun inlineFun(block: () -> Unit) {
    block() // Eksekusi lambda block
}

// Contoh fungsi dengan return
fun sum(a: Int, b: Int): Int {
    return a + b
}


fun getRegisterNumber() = Random.nextInt(100)