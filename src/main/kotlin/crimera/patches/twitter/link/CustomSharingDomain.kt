package crimera.patches.twitter.link;

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import crimera.patches.twitter.link.fingerprints.AddSessionTokenFingerprint

@Patch(
    name = "Always use \"twitter.com\" as domain & no tracking param",
    description = "Changes domain in url to twitter.com",
    compatiblePackages = [CompatiblePackage("com.twitter.android")]
)
@Suppress("unused")
object CustomSharingDomain: BytecodePatch(
    setOf(AddSessionTokenFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val result = AddSessionTokenFingerprint.result
            ?: throw PatchException("Fingerprint not found")

        result.mutableMethod.addInstructions(0, """
            const-string v0, "x.com"
            const-string v1, "twitter.com"

            invoke-virtual {p0, v0, v1}, Ljava/lang/String;->replaceFirst(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; 
            move-result-object p0
            return-object p0
        """.trimIndent())
    }
}