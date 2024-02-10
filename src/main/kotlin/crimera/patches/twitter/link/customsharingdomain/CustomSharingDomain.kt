package crimera.patches.twitter.link.customsharingdomain;

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import crimera.patches.twitter.link.customsharingdomain.fingerprints.LinkResourceGetterFingerprint

@Patch(
    name = "Custom sharing domain",
    description = "Allows for changing the sharing link domain",
    compatiblePackages = [CompatiblePackage("com.twitter.android")]
)
@Suppress("unused")
object CustomSharingDomain : BytecodePatch(
    setOf(LinkResourceGetterFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val result = LinkResourceGetterFingerprint.result
            ?: throw PatchException("Fingerprint not found")

        val setUsernameIndex = result.scanResult.stringsScanResult!!.matches.first().index

        result.mutableMethod.removeInstruction(setUsernameIndex - 1)

        result.mutableMethod.addInstructions(
            setUsernameIndex - 1, """
               const-string v2, "https://fxtwitter.com/%1${"$"}s/status/%2${"$"}s"

                invoke-static {v2, v1}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
                move-result-object v1 
            """.trimIndent())
    }
}