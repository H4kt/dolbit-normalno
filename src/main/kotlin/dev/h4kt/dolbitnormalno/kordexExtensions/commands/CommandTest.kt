package dev.h4kt.dolbitnormalno.kordexExtensions.commands

import dev.h4kt.dolbitnormalno.extensions.tryRegisterToTestGuild
import dev.h4kt.dolbitnormalno.generated.i18n.Translations
import dev.kord.common.annotation.KordVoice
import dev.kordex.core.components.forms.ModalForm
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand

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
