package com.intellective.sample.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Wrapper for response. Allows to accumulate and return data as String
 */
public class ByteResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayServletOutputStream wrapOutputStream = null;
    private PrintWriter printWriter = null;

    public ByteResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public void close() throws IOException {
        if (this.printWriter != null) {
            this.printWriter.close();
        }

        if (this.wrapOutputStream != null) {
            this.wrapOutputStream.close();
        }
    }

    /**
     * Flush OutputStream or PrintWriter
     *
     * @throws IOException
     */
    @Override
    public void flushBuffer() throws IOException {
        if (this.printWriter != null) {
            this.printWriter.flush();
        }

        IOException exception1 = null;
        try {
            if (this.wrapOutputStream != null) {
                this.wrapOutputStream.flush();
            }
        } catch (IOException e) {
            exception1 = e;
        }

        IOException exception2 = null;
        try {
            super.flushBuffer();
        } catch (IOException e) {
            exception2 = e;
        }

        if (exception1 != null) throw exception1;
        if (exception2 != null) throw exception2;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (printWriter != null) {
            throw new IllegalStateException(
                    "PrintWriter obtained already - cannot get OutputStream");
        }
        if (wrapOutputStream == null) {
            wrapOutputStream = new ByteArrayServletOutputStream();
        }
        return wrapOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter == null && wrapOutputStream != null) {
            throw new IllegalStateException(
                    "OutputStream obtained already - cannot get PrintWriter");
        }
        if (this.printWriter == null) {
            wrapOutputStream = new ByteArrayServletOutputStream();
            printWriter =
                    new PrintWriter(new OutputStreamWriter(wrapOutputStream,
                            getResponse().getCharacterEncoding()));
        }
        return this.printWriter;
    }

    /**
     * Returns the response data as String
     *
     * @return String with the response
     * @throws IOException
     */
    public String getData() throws IOException {
        return null != wrapOutputStream ? wrapOutputStream.toString(getResponse().getCharacterEncoding()) : null;
    }

    class ByteArrayServletOutputStream extends ServletOutputStream {

        private ByteArrayOutputStream wrapOutputStream;

        public ByteArrayServletOutputStream() throws IOException {
            super();
            this.wrapOutputStream = new ByteArrayOutputStream();
        }

        @Override
        public void close() throws IOException {
            this.wrapOutputStream.close();
        }

        @Override
        public void flush() throws IOException {
            this.wrapOutputStream.flush();
        }

        @Override
        public void write(byte b[]) throws IOException {
            this.wrapOutputStream.write(b);
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException {
            this.wrapOutputStream.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            this.wrapOutputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // NO-OP
        }

        public String toString(String charsetName) throws IOException {
            return wrapOutputStream.toString(charsetName);
        }
    }

}
