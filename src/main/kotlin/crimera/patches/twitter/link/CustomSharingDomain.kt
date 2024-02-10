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
    name = "Custom sharing domain",
    description = "Allows for changing the sharing link domain",
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
            const-string v0, "twitter"
            const-string v1, "fxtwitter"

            invoke-virtual {p0, v0, v1}, Ljava/lang/String;->replaceFirst(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
            move-result-object p0
    
            const-string v0, "x.com"
            const-string v1, "fixupx.com"

            invoke-virtual {p0, v0, v1}, Ljava/lang/String;->replaceFirst(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; 
            move-result-object p0
        """.trimIndent())
    }
}