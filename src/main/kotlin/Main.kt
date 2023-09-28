import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec

fun main(args: Array<String>) {
    if (args.size != 4) {
        println("Использование: ")
        println("Для шифрования: java -jar CryptoApp.jar encrypt <входной_файл> <выходной_файл> <ключ>")
        println("Для дешифрования: java -jar CryptoApp.jar decrypt <входной_файл> <выходной_файл> <ключ>")
        return
    }

    val operation = args[0]
    val inputFile = File(args[1])
    val outputFile = File(args[2])
    val keyStr = args[3]

    try {
        val key = SecretKeySpec(keyStr.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")

        when (operation) {
            "encrypt" -> {
                cipher.init(Cipher.ENCRYPT_MODE, key)
                encryptFile(cipher, inputFile, outputFile)
                println("Файл успешно зашифрован.")
            }
            "decrypt" -> {
                cipher.init(Cipher.DECRYPT_MODE, key)
                decryptFile(cipher, inputFile, outputFile)
                println("Файл успешно дешифрован.")
            }
            else -> println("Недопустимая операция.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        println("Произошла ошибка: ${e.message}")
    }
}

private fun encryptFile(cipher: Cipher, inputFile: File, outputFile: File) {
    FileInputStream(inputFile).use { inputStream ->
        CipherOutputStream(FileOutputStream(outputFile), cipher).use { outputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } >= 0)
                outputStream.write(buffer, 0, bytesRead)
        }
    }
}

private fun decryptFile(cipher: Cipher, inputFile: File, outputFile: File) {
    CipherInputStream(FileInputStream(inputFile), cipher).use { inputStream ->
        FileOutputStream(outputFile).use { outputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } >= 0)
                outputStream.write(buffer, 0, bytesRead)
        }
    }
}