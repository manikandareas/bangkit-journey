import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.channels.Channel

fun main() = runBlocking {
    // Concurrency dan Parallelism
    println("== Concurrency dan Parallelism ==")
    val time = measureTimeMillis {
        val deferred1 = async { doWork("Task 1", 1000L) }
        val deferred2 = async { doWork("Task 2", 2000L) }
        println("Result 1: ${deferred1.await()}, Result 2: ${deferred2.await()}")
    }
    println("Completed in $time ms\n")

    // Coroutine Builders: launch, async, runBlocking
    println("== Coroutine Builders ==")
    val job: Job = launch {
        println("Coroutine dengan launch dimulai")
        delay(1000L)
        println("Coroutine dengan launch selesai")
    }
    job.join()  // Menunggu job selesai

    val deferred = async {
        println("Coroutine dengan async dimulai")
        delay(1000L)
        "Hasil Coroutine dengan async"
    }
    println("Result dari async: ${deferred.await()}")  // Mendapatkan hasil dari deferred

    // Dispatcher Usage
    println("\n== Penggunaan Dispatcher ==")
    launch(Dispatchers.Default) {
        println("Running on Default Dispatcher: ${Thread.currentThread().name}")
    }
    launch(Dispatchers.IO) {
        println("Running on IO Dispatcher: ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) {
        println("Running on Unconfined Dispatcher: ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyThread")) {
        println("Running on new single thread: ${Thread.currentThread().name}")
    }

    // Menangani masalah Deadlocks, Livelocks, Starvation, dan Race Conditions
    println("\n== Menangani masalah concurrency ==")
    val sharedResource = Resource()
    val job1 = launch { sharedResource.increment() }
    val job2 = launch { sharedResource.increment() }
    joinAll(job1, job2)
    println("Final counter value: ${sharedResource.counter}")

    // Menggunakan Channel untuk komunikasi antar-coroutine
    println("\n== Penggunaan Channel ==")
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) {
            println("Mengirim $x")
            channel.send(x * x) // mengirim nilai kuadrat
        }
        channel.close() // menutup channel saat selesai
    }

    for (y in channel) { // menerima dari channel
        println("Menerima $y")
    }



    println("\nSemua tugas selesai.")
}

// Fungsi untuk simulasi pekerjaan
suspend fun doWork(taskName: String, delayTime: Long): String {
    delay(delayTime)
    return "$taskName selesai"
}

// Kelas untuk menangani masalah race condition
class Resource {
    var counter = 0

    // Fungsi synchronized untuk menghindari race condition
    @Synchronized
    suspend fun increment() {
        delay(100) // Simulasi kerja
        counter++
        println("Counter incremented to: $counter")
    }
}
