package services.clone;

import java.io.IOException;
import java.io.OutputStream;

/**
 * User: dmitry
 * Date: 25.12.11
 * Time: 17:54
 */
class FastByteArrayOutputStream extends OutputStream {
    /**
     * buffer and size
     */
    protected byte[] buffer = null;
    protected int size = 0;

    /**
     * Default constructor
     */
    FastByteArrayOutputStream() {
        this(5 * 1024);
    }

    /**
     * Constructs a stream with the given initial size
     * @param initSize initial buffer size
     */
    FastByteArrayOutputStream(int initSize) {
        this.size = 0;
        this.buffer = new byte[initSize];
    }

    private void verifyBufferSize(int size) {
        if (size > buffer.length) {
            byte[] old = buffer;
            buffer = new byte[Math.max(size, 2 * old.length)];
            System.arraycopy(old, 0, buffer, 0, old.length);
        }
    }

    public int getSize() {
        return size;
    }

    public byte[] getByteArray() {
        return buffer;
    }

    public final void write(byte[] b) {
        verifyBufferSize(size + b.length);
        System.arraycopy(b, 0, buffer, size, b.length);
        size += b.length;
    }

    public final void write(byte[] b, int off, int len) {
        verifyBufferSize(size + len);
        System.arraycopy(b, off, buffer, size, len);
        size += len;
    }

    public final void write(int i) throws IOException {
        verifyBufferSize(size + 1);
        buffer[size++] = (byte) i;
    }

    public void reset() {
        size = 0;
    }
}
