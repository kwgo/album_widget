package com.jchip.album.common;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NinePatchHelper {
    public NinePatchDrawable getDrawable(Bitmap bitmap) {
        final byte[] chunk = bitmap.getNinePatchChunk();
        if (NinePatch.isNinePatchChunk(chunk)) {
            NinePatchChunk ninePatchChunk = new NinePatchChunk(chunk);
            //NinePatchDrawable drawable = new NinePatchDrawable(Resources.getSystem(), bitmap, chunk, ninePatchChunk.getPadding(), null);
            return new NinePatchDrawable(Resources.getSystem(), bitmap, chunk, ninePatchChunk.getPadding(), null);
        }
        return null;
    }

    public class NinePatchChunk {
        private final Rect padding = new Rect();
        private int divX[];
        private int divY[];
        private int color[];

        public NinePatchChunk(final byte[] chunk) {
            this.deserialize(chunk);
        }

        public Rect getPadding() {
            return this.padding;
        }

        public void deserialize(final byte[] chunk) {
            final ByteBuffer byteBuffer = ByteBuffer.wrap(chunk).order(ByteOrder.nativeOrder());

            if (byteBuffer.get() == 0) {
                return; // is not serialized
            }

            this.divX = new int[byteBuffer.get()];
            this.divY = new int[byteBuffer.get()];
            this.color = new int[byteBuffer.get()];

            checkDivCount(this.divX.length);
            checkDivCount(this.divY.length);

            // skip 8 bytes
            byteBuffer.getInt();
            byteBuffer.getInt();

            this.padding.left = byteBuffer.getInt();
            this.padding.right = byteBuffer.getInt();
            this.padding.top = byteBuffer.getInt();
            this.padding.bottom = byteBuffer.getInt();

            // skip 4 bytes
            byteBuffer.getInt();

            readIntArray(this.divX, byteBuffer);
            readIntArray(this.divY, byteBuffer);
            readIntArray(this.color, byteBuffer);
        }

        private void readIntArray(final int[] data, final ByteBuffer buffer) {
            for (int index = 0, size = data.length; index < size; ++index) {
                data[index] = buffer.getInt();
            }
        }

        private void checkDivCount(final int length) {
            if (length == 0 || (length & 0x01) != 0) {
                throw new RuntimeException("invalid nine-patch: " + length);
            }
        }
    }
}