package dev.h4kt.pivosound.serializers

import dev.kord.common.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias HexColor = @Serializable(HexColorSerializer::class) Color

object HexColorSerializer : KSerializer<Color> {

    override val descriptor = PrimitiveSerialDescriptor(
        serialName = "Kord.color.hex",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): Color {
        return decoder.decodeString()
            .drop(1)
            .toInt(16)
            .run(::Color)
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString("#${value.rgb.toHexString()}")
    }

}
