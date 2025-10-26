package dev.h4kt.pivosound.kordexExtensions.commands

import dev.h4kt.pivosound.extensions.tryRegisterToTestGuild
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.kord.common.annotation.KordVoice
import dev.kordex.core.components.forms.Form
import dev.kordex.core.components.forms.ModalForm
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.kordex.core.i18n.types.Key

class CommandTest : Extension() {

    override val name = "command:test"

    class TestModal : ModalForm() {

        override var title = Translations.Commands.Test.Modal.title

        val text = lineText {
            label = Translations.Commands.Test.Modal.text
        }

    }

    @OptIn(KordVoice::class)
    override suspend fun setup() {
        publicSlashCommand(::TestModal) {

            name = Translations.Commands.Test.name
            description = Translations.Commands.Test.description

            tryRegisterToTestGuild()

            action { modal ->
                modal?.sendAndDeferPublic(this)
            }
        }
    }

}
