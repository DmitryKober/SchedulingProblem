package services.clone;

import java.io.InputStream;

/**
 * User: dmitry
 * Date: 25.12.11
 * Time: 22:26
 */
public class FastByteArrayInputStream extends InputStream {

    protected byte[] buffer = null; // buffer
    protected int count = 0;        // number of bytes that can be read from the buffer
    protected int pos = 0;          // number of bytes that have been read from the buffer

    public FastByteArrayInputStream(byte[] buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public final int available() {
       return count - pos;
    }

    public final int read() {
        return (pos < count) ? (buffer[pos++] & 0xff) : -1;
    }

    public final int read(byte[] b, int off, int len) {
        if (pos >= count) {
            return -1;
        }

        if (pos + len > count) {
            len = (count - pos);
        }

        System.arraycopy(buffer, pos, b, off, len);
        pos += len;
        return len;
    }

    public final long skip(long n) {
        if (pos + n > count) {
            n = count - pos;
        }

        if (n < 0) {
            return 0;
        }

        pos += n;
        return n;
    }
}
