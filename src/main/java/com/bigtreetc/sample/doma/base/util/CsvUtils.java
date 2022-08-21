package com.bigtreetc.sample.doma.base.util;

import static com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/** TSVファイル出力ユーティリティ */
@Slf4j
public class CsvUtils {

  private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 4096;

  private static final CsvMapper csvMapper = createCsvMapper();

  /**
   * CSVマッパーを生成する。
   *
   * @return
   */
  private static CsvMapper createCsvMapper() {
    val mapper = new CsvMapper();
    mapper.configure(ALWAYS_QUOTE_STRINGS, true);
    mapper.findAndRegisterModules();
    return mapper;
  }

  /**
   * CSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @throws Exception
   */
  public static ByteArrayOutputStream writeCsv(Class<?> clazz, Collection<?> data)
      throws IOException {
    return write(clazz, data, StandardCharsets.UTF_8.name(), ',');
  }

  /**
   * CSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @param charsetName
   * @throws Exception
   */
  public static ByteArrayOutputStream writeCsv(
      Class<?> clazz, Collection<?> data, String charsetName) throws IOException {
    return write(clazz, data, charsetName, ',');
  }

  /**
   * TSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @throws Exception
   */
  public static ByteArrayOutputStream writeTsv(Class<?> clazz, Collection<?> data)
      throws IOException {
    return write(clazz, data, StandardCharsets.UTF_8.name(), '\t');
  }

  /**
   * TSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @param charsetName
   * @throws Exception
   */
  public static ByteArrayOutputStream writeTsv(
      Class<?> clazz, Collection<?> data, String charsetName) throws IOException {
    return write(clazz, data, charsetName, '\t');
  }

  /**
   * CSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @param charsetName
   * @param delimiter
   * @return
   * @throws Exception
   */
  private static ByteArrayOutputStream write(
      Class<?> clazz, Collection<?> data, String charsetName, char delimiter) throws IOException {
    // CSVヘッダをオブジェクトから作成する
    val schema = csvMapper.schemaFor(clazz).withHeader().withColumnSeparator(delimiter);

    // 書き出し
    val outputStream = new ByteArrayOutputStream(OUTPUT_BYTE_ARRAY_INITIAL_SIZE);
    try (val writer = new OutputStreamWriter(outputStream, charsetName)) {
      csvMapper.writer(schema).writeValue(writer, data);
    }

    return outputStream;
  }
}
