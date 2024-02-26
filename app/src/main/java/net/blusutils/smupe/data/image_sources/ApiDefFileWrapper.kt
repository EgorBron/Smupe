package net.blusutils.smupe.data.image_sources

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.blusutils.smupe.data.image_sources.models.sources.BaseImageSource
import net.blusutils.smupe.data.image_sources.models.sources.InternetImageSource
import net.blusutils.smupe.data.image_sources.models.sources.SourceMode
import net.blusutils.smupe.data.image_sources.repository.DirectLinkRepository
import net.blusutils.smupe.data.image_sources.repository.JsonRepository
import net.blusutils.smupe.data.image_sources.repository.LocalRepository
import net.blusutils.smupe.data.image_sources.repository.PlainTextRepository
import java.io.File

object ApiDefFileWrapper {

    val Context.apiDefDirectory: File
        get() = File(filesDir, "api_def")

    fun Context.loadAllApiDefs() {
        apiDefDirectory.listFiles()?.let {
            if (it.isEmpty()) {
                Log.d("loadAllApiDefs", "No api defs found")
            } else
                it.forEach {
                    if (it.extension == "json")
                        readFile(this, it.name)?.let {
                            CurrentApiDefParams.dynamicRepos.add(it)
                        }
                }
        }
    }

    fun readFile(context: Context, filename: String): BaseImageSource? {
        context.apiDefDirectory.mkdirs()

        val file = File(context.apiDefDirectory, filename)
        val adapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        return file.run {
            if (!exists()) null
            else {
                val txt = readText()
//                if (txt.matches("^\\s*\\{".toRegex())) {
                val regexMatches = "\"mode\":\\s*\"\\w*?\"".toRegex().find(txt)
                regexMatches?.let {
                    SourceMode.Local
                    val cls = when (regexMatches.value.split(":")[1].trim()) {
                        "\"Local\"" -> LocalRepository::class.java
                        "\"Internet\"" -> InternetImageSource::class.java
                        "\"InternetDirectLink\"" -> DirectLinkRepository::class.java
                        "\"InternetPlainText\"" -> PlainTextRepository::class.java
                        else -> JsonRepository::class.java
                    }
                    adapter.adapter(cls).fromJson(txt)
                }
            }
        }
    }

    fun<T : BaseImageSource> writeFile(context: Context, filename: String, apiDef: T, cls: Class<T>) {
        context.apiDefDirectory.mkdirs()

        val file = File(context.apiDefDirectory, filename)
        val adapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(cls)
        file.writeText(adapter.toJson(apiDef))
    }
}
