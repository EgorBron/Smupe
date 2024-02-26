package net.blusutils.smupe.ui.image_sources

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import net.blusutils.smupe.data.image_sources.ApiDefFileWrapper
import net.blusutils.smupe.data.image_sources.CurrentApiDefParams
import net.blusutils.smupe.data.image_sources.models.sources.*
import net.blusutils.smupe.data.image_sources.repository.JsonRepository
import net.blusutils.smupe.ui.misc.CenteredRow
import net.blusutils.smupe.util.OkHttpUtils
import net.blusutils.smupe.util.checkIsLink
import net.blusutils.smupe.util.checkIsLinkAndNotBlank
import net.blusutils.smupe.util.getJsonValueFromPath

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun JsonApiDefsEditScreen(src: InternetImageSource? = null, close: () -> Unit = {}) {

    Log.d("ApiDefsEditScreen", "Appeared")

    val ctx = LocalContext.current

    val scope = rememberCoroutineScope()

    var name by rememberSaveable { mutableStateOf("") }
    var nameError by rememberSaveable { mutableStateOf(false) }

    var description by rememberSaveable { mutableStateOf("") }
    var descriptionError by rememberSaveable { mutableStateOf(false) }

    var icon by rememberSaveable { mutableStateOf("") }
    var iconError by rememberSaveable { mutableStateOf(false) }

    var link by rememberSaveable { mutableStateOf("") }
    var linkError by rememberSaveable { mutableStateOf(false) }

    var isSearch by rememberSaveable { mutableStateOf(false) }

    var searchUrl by rememberSaveable { mutableStateOf("") }
    var searchUrlError by rememberSaveable { mutableStateOf(false) }

    var searchPath by rememberSaveable { mutableStateOf("") }
    var searchPathError by rememberSaveable { mutableStateOf(false) }

    var searchIdPath by rememberSaveable { mutableStateOf("") }
    var searchPathIdError by rememberSaveable { mutableStateOf(false) }

    var authType by rememberSaveable { mutableStateOf(AuthType.None) }
    var authTypeError by rememberSaveable { mutableStateOf(false) }

    var authPayload by rememberSaveable { mutableStateOf("") }
    var authPayloadError by rememberSaveable { mutableStateOf(false) }

    var isBlurhash by rememberSaveable { mutableStateOf(false) }

    var blurhashPath by rememberSaveable { mutableStateOf("") }
    var blurhashPathError by rememberSaveable { mutableStateOf(false) }

    var isTags by rememberSaveable { mutableStateOf(false) }

    var tagsPath by rememberSaveable { mutableStateOf("") }
    var tagsPathError by rememberSaveable { mutableStateOf(false) }

    var path by rememberSaveable { mutableStateOf("") }
    var pathError by rememberSaveable { mutableStateOf(false) }

    var dataPath by rememberSaveable { mutableStateOf("") }
    var dataPathError by rememberSaveable { mutableStateOf(false) }

    var idPath by rememberSaveable { mutableStateOf("") }
    var idPathError by rememberSaveable { mutableStateOf(false) }

    var pathDelimiter by rememberSaveable { mutableStateOf(",") }
    var pathDelimiterError by rememberSaveable { mutableStateOf(false) }

    var value by rememberSaveable { mutableStateOf("") }
    var source by rememberSaveable { mutableStateOf<JsonRepository?>(null) }

    fun validate() : Boolean {
        nameError = name.isBlank()
        descriptionError = false //description.isBlank()
        iconError = if (icon.isNotBlank()) !checkIsLink(icon) else false
        // double reverse because of logic
        linkError = !checkIsLinkAndNotBlank(link)
        searchUrlError = !checkIsLinkAndNotBlank(searchUrl)
        searchPathError = searchPath.isBlank() || !searchPath.contains(',')
        searchPathIdError = searchIdPath.isBlank() || !searchIdPath.contains(',')
        // TODO authtype validation
        authPayloadError = authPayload.isBlank()
        blurhashPathError = blurhashPath.isBlank() || !blurhashPath.contains(',')
        tagsPathError = tagsPath.isBlank() || !tagsPath.contains(',')
        pathError = !checkIsLinkAndNotBlank(path)
        dataPathError = dataPath.isBlank() || !dataPath.contains(',')
        idPathError = idPath.isBlank() || !idPath.contains(',')
        pathDelimiterError = pathDelimiter.isBlank()

        return !nameError && !descriptionError && !linkError
                && !(isSearch && (searchUrlError || searchPathError || searchPathIdError))
                && !authPayloadError
                && !(isBlurhash && blurhashPathError) && !(isTags && tagsPathError)
                && !pathError && !dataPathError && !idPathError && !pathDelimiterError
    }

    val launch: () -> Unit = {
        Log.d("ApiDefsEditScreen.Launch", "Launching")
        if(validate()) {
            source = JsonRepository(
                name,
                icon,
                description,
                link,
                SearchInfo(isSearch, searchUrl, searchPath, searchIdPath),
                Authorization(
                    authType,
                    authPayload
                ),
                BlurHashInfo(
                    isBlurhash,
                    blurhashPath
                ),
                TagsInfo(
                    isTags,
                    tagsPath
                ),
                path,
                dataPath,
                idPath,
                pathDelimiter
            )
            Log.d("ApiDefsEditScreen.Launch", "Validated")

            if (CurrentApiDefParams.dynamicRepos.find { src!!.name == it.name } != null) {
                CurrentApiDefParams.dynamicRepos.remove(CurrentApiDefParams.dynamicRepos.find { src!!.name == it.name })
            }
            CurrentApiDefParams.dynamicRepos.add(source!!)
            Log.d("ApiDefsEditScreen.Launch", "Added")
            Log.d("ApiDefsEditScreen.Launch", "${CurrentApiDefParams.dynamicRepos.size}")
            scope.launch {
                value = OkHttpUtils.getAsync(link).body?.source()?.readUtf8() ?: "ERROR!"
                Log.d("ApiDefsEditScreen.Launch", "Got response")
                ApiDefFileWrapper.writeFile(ctx, "$name.json", source!!, InternetImageSource::class.java)
            }.invokeOnCompletion {
                close()
            }
        }
    }

    val closeEntire = {
        if (source != null)
            ApiDefFileWrapper.writeFile(
                ctx,
                "$name.json",
                source as InternetImageSource,
                InternetImageSource::class.java
            )
        close()
    }

    LaunchedEffect(src) {
        src?.let {
            name = it.name
            it.description?.let { description = it }
            it.icon?.let { icon = it }
            link = it.apiLink
            path = it.sourcePath
            it.dataPathSource?.let { dataPath = it }
            (it as JsonRepository).sourceIdPath?.let { idPath = it }

            it.search?.let {
                isSearch = it.supported
                searchUrl = it.url
                searchPath = it.path
                searchIdPath = it.idPath
            }

            it.authorization?.let {
                authType = it.type
                authPayload = it.payload
            }

            it.blurHash?.let {
                isBlurhash = it.supported
                blurhashPath = it.path
            }

            it.tags?.let {
                isTags = it.supported
                tagsPath = it.path
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Edit API-DEF",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = closeEntire) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        OutlinedCard(
            Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedCard {

                Text("Tip: path is a comma-separated list of keys,\nwhere numbers are array indices,\ntilda (~) means \"random index of array\".\nExample: resp,~,parts,0 stands for\n\"get key `resp` in response, then random array item, them key `parts`, then array element with index 0\"")

                TextField(
                    name,
                    { name = it },
                    placeholder = { Text("API name") },
                    isError = nameError,
                    supportingText = { if (nameError) Text("Invalid API name") }
                )

                TextField(
                    description,
                    { description = it },
                    placeholder = { Text("Description of API") }
                )

                TextField(
                    icon,
                    { icon = it },
                    placeholder = { Text("Icon URL") },
                    isError = iconError,
                    supportingText = { if (iconError) Text("Invalid icon URL") }
                )

                TextField(
                    link,
                    { link = it },
                    placeholder = { Text("Link to API") },
                    isError = linkError,
                    supportingText = { if (linkError) Text("Invalid API link") }
                )

                TextField(
                    path,
                    { path = it },
                    placeholder = { Text("API path") },
                    isError = pathError,
                    supportingText = { if (pathError) Text("Invalid API path") }
                )
                TextField(
                    dataPath,
                    { dataPath = it },
                    placeholder = { Text("Path to image") },
                    isError = dataPathError,
                    supportingText = { if (dataPathError) Text("Invalid data path") }
                )
                TextField(
                    idPath,
                    { idPath = it },
                    placeholder = { Text("Path to image ID") },
                    isError = idPathError,
                    supportingText = { if (idPathError) Text("Invalid ID path") }
                )
                TextField(
                    pathDelimiter,
                    { pathDelimiter = it },
                    placeholder = { Text("Path delimiter") },
                    isError = pathDelimiterError,
                    supportingText = { if (pathDelimiterError) Text("Invalid path delimiter") }
                )
            }

            OutlinedCard {
                TextField(
                    "",
                    { },
                    placeholder = { Text("Auth type (CHANGE TO OPTIONS)") }
                )

                TextField(
                    authPayload,
                    { authPayload = it },
                    placeholder = { Text("Auth payload") },
                    isError = authPayloadError,
                    supportingText = { if (authPayloadError) Text("Invalid auth payload") }
                )
            }
            OutlinedCard {
                CenteredRow {
                    Checkbox(isSearch, { isSearch = it })
                    Text("Is search available")
                }
                TextField(
                    searchUrl,
                    { searchUrl = it },
                    placeholder = { Text("Search endpoint in API") },
                    enabled = isSearch,
                    isError = isSearch && searchUrlError,
                    supportingText = { if (isSearch && searchUrlError) Text("Invalid search endpoint") }
                )
                TextField(
                    searchPath,
                    { searchPath = it },
                    placeholder = { Text("Path to image in search results") },
                    enabled = isSearch,
                    isError = isSearch && searchPathError,
                    supportingText = { if (isSearch && searchPathError) Text("Invalid search result path") }
                )
                TextField(
                    searchIdPath,
                    { searchIdPath = it },
                    placeholder = { Text("Path to image id in search results") },
                    enabled = isSearch,
                    isError = isSearch && searchPathIdError,
                    supportingText = { if (isSearch && searchPathIdError) Text("Invalid search result ID") }
                )
            }

            OutlinedCard {
                CenteredRow {
                    Checkbox(isBlurhash, { isBlurhash = it })
                    Text("Is Blurhash available")
                }
                TextField(
                    blurhashPath,
                    { blurhashPath = it },
                    placeholder = { Text("Path to image blurhash") },
                    enabled = isBlurhash,
                    isError = isBlurhash && blurhashPathError,
                    supportingText = { if (isBlurhash && blurhashPathError) Text("Invalid Blurhash string") }
                )
            }

            OutlinedCard {
                CenteredRow {
                    Checkbox(isTags, { isTags = it })
                    Text("Is tags available")
                }
                TextField(
                    tagsPath,
                    { tagsPath = it },
                    placeholder = { Text("Path to image tags") },
                    enabled = isTags,
                    isError = isTags && tagsPathError,
                    supportingText = { if (isTags && tagsPathError) Text("Invalid tags path") }
                )
            }

            Button(launch) {
                Text("Save")
            }

            if (value != "") Text(
                getJsonValueFromPath(
                    value,
                    dataPath
                ).toString()
            )
        }
    }
}


/*
 * Если вы читаете это... Простите, пжалста, за нытьё, но я устал.
 * Серьёзно, какой раз уже переписываю этот долбучий апидеф...
 * Хотя, не только его, но на апидефах я конкретно застрял.
 * Приложение делается уже третий месяц, а самая главная фича
 * только-только пишется. Ну ё-моё, в голове уже плавиться нечему,
 * а все извилины распрямились. Дайте выходной размером в жизнь.
 * Дошло до того, что я уже не могу понять, что я делаю, а заставить
 * себя довести всё до конца нет мотивации.
 * К слову, вон, мой знакомый, Саня (stngularity), которого я на все лады
 * благодарю, тоже делает приложение на композе, только раз в много сложнее.
 * Делает два месяца без опыта. Считайте, уже сделал. КАК?!
 * А ему, между прочим, 14 лет, он учится в гимназии (!), и времени у него
 * куда меньше. Так ещё он успевает писать спецификацию
 * криптографического алгоритма, придумывать дизайн для ОС,
 * составлять протокол хранения данных в своей БД и смотреть
 * кучу сериалов (или что там у него). :kruto_klassno:
 * Не судите строго. Просто такие дела.
 * Ах, да, почему же я делаю приложение три месяца?
 * Да потому что весь декабрь я них... пардон за мой французский,
 * ничего не делал, только к Новому году опомнился и сел за интелижу.
 * Ещё и парочка моментов в композе меня подвела.
 * Вот они, слева направо: AnchoredDraggable, DataStore, Room, Lottie,
 * работа с интернетом и JSON, модель API-DEF, переводы...
 * (кстати, приложение могло выйти целиком на "ломаном" английском,
 * так как мне было постоянно лень переключать раскладку или лезть
 * в ресурсы приложения, хотя я и скачал Punto Switcher от Яндекса,
 * а ресурсы перекинул в что-то типа Crowdin)
 */
