package hackathon.qolmods.ui;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.PacketType;
import net.minecraft.util.Identifier;

public record SortInventoryC2SPacket() implements CustomPayload {
    public static final CustomPayload.Id<SortInventoryC2SPacket> ID =
            new CustomPayload.Id<>(Identifier.of("yourmodid", "sort_inventory"));

    public static final PacketCodec<PacketByteBuf, SortInventoryC2SPacket> CODEC =
            PacketCodec.of((value, buf) -> {}, buf -> new SortInventoryC2SPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
