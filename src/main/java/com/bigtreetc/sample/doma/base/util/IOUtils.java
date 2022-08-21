package com.bigtreetc.sample.doma.base.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

  private static final int BUFFER_SIZE = 1024 * 4;

  /**
   * ByteArrayOutputStream を返します。
   *
   * @param is
   * @return
   * @throws IOException
   */
  public static byte[] toByteArray(InputStream is) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    try {
      byte[] b = new byte[BUFFER_SIZE];
      int n = 0;
      while ((n = is.read(b)) != -1) {
        output.write(b, 0, n);
      }
      return output.toByteArray();
    } finally {
      output.close();
    }
  }

  /**
   * 入力を出力に書き出します。
   *
   * @param in
   * @param out
   * @return
   * @throws IOException
   */
  public static long copy(InputStream in, OutputStream out) throws IOException {
    byte[] buf = new byte[BUFFER_SIZE];
    long count = 0;
    int n = 0;
    while ((n = in.read(buf)) > -1) {
      out.write(buf, 0, n);
      count += n;
    }
    out.flush();
    return count;
  }
}
