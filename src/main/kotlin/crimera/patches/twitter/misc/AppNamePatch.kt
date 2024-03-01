package app.revanced.patches.all.misc.packagename

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.patch.options.PatchOption.PatchExtensions.stringPatchOption
import app.revanced.patcher.patch.options.PatchOptionException
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.Closeable
import java.lang.Exception

@Patch(
    name = "Change app name to Twitter",
    description = ""
)
@Suppress("unused")
object AppNamePatch : ResourcePatch(), Closeable {

    private lateinit var context: ResourceContext

    private val languages = listOf("values", "values-ar", "values-ar-rEH", "values-bg", "values-bn", "values-ca", "values-cs", "values-da", "values-de", "values-el", "values-en-rGB", "values-es", "values-fa", "values-fi", "values-fr", "values-gu", "values-hi", "values-hr", "values-hu", "values-in", "values-it", "values-iw", "values-ja", "values-kn", "values-ko", "values-mr", "values-ms", "values-nb", "values-nl", "values-pl", "values-pt", "values-ro", "values-ru", "values-sk", "values-sr", "values-sv", "values-ta", "values-th", "values-tl", "values-tr", "values-uk", "values-vi", "values-zh-rCN", "values-zh-rHK", "values-zh-rTW")

    override fun execute(context: ResourceContext) {
        this.context = context
    }

    override fun close() {
        for (lang in languages) {
            try {
                context.xmlEditor["res/$lang/strings.xml"].use { editor ->
//                    val originalAppName = "X"
                    val targetAppName = "Twitter"

                    val strings = editor.file.getElementsByTagName("string") as NodeList
                    for (i in 0 until strings.length) {
                        val s = strings.item(i) as Element
                        if (s.getAttribute("name").equals("app_name"))
                            s.textContent = targetAppName
                    }
                }
            } catch (e: Exception) {
                System.err.println(e.message + "\n" + e.stackTrace)
            }
        }
    }
}
