package com.irtimaled.bbor;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashSet;
import java.util.Set;

public class BoundingBoxMessage implements IMessage {
    private int dimension;
    private BoundingBox key;
    private Set<BoundingBox> boundingBoxes;

    public static BoundingBoxMessage from(int dimension, BoundingBox key, Set<BoundingBox> boundingBoxes) {
        BoundingBoxMessage message = new BoundingBoxMessage();
        message.dimension = dimension;
        message.key = key;
        message.boundingBoxes = boundingBoxes;
        return message;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = ByteBufUtils.readVarShort(buf);
        key = BoundingBoxDeserializer.deserialize(buf);
        boundingBoxes = new HashSet<BoundingBox>();
        while (buf.isReadable()) {
            BoundingBox boundingBox = BoundingBoxDeserializer.deserialize(buf);
            boundingBoxes.add(boundingBox);
        }
        if (boundingBoxes.size() == 0)
            boundingBoxes.add(key);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarShort(buf, dimension);
        BoundingBoxSerializer.serialize(key, buf);
        if (boundingBoxes.size() > 1) {
            for (BoundingBox boundingBox : boundingBoxes) {
                BoundingBoxSerializer.serialize(boundingBox, buf);
            }
        }
    }

    public int getDimension() {
        return dimension;
    }

    public BoundingBox getKey() {
        return key;
    }

    public Set<BoundingBox> getBoundingBoxes() {
        return boundingBoxes;
    }
}