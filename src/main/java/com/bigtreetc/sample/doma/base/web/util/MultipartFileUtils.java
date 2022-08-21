package com.bigtreetc.sample.doma.base.web.util;

import com.bigtreetc.sample.doma.base.domain.model.MultipartFileConvertible;
import com.bigtreetc.sample.doma.base.util.FileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/** MultipartFile関連のユーティリティ */
@Slf4j
public class MultipartFileUtils {

  /**
   * MultipartFileConvertibleに値を詰め替えます。
   *
   * @param from
   * @param to
   */
  public static void convert(MultipartFile from, MultipartFileConvertible to) {
    to.setFilename(from.getName());
    to.setOriginalFilename(from.getOriginalFilename());
    to.setContentType(from.getContentType());

    try {
      to.setContent(from.getBytes());
    } catch (IOException e) {
      log.error("failed to getBytes", e);
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * ファイルを保存します。
   *
   * @param location
   * @param file 保存先ディレクトリ
   */
  public static void saveFile(Path location, MultipartFile file) {
    Objects.requireNonNull(file, "file can't be null");
    String filename = file.getOriginalFilename();

    try {
      if (file.isEmpty()) {
        throw new IllegalArgumentException("cloud not save empty file. " + filename);
      }

      // ディレクトリがない場合は作成する
      FileUtils.createDirectories(location);

      // インプットストリームをファイルに書き出す
      Files.copy(file.getInputStream(), location.resolve(filename));

    } catch (IOException e) {
      throw new IllegalStateException("failed to save file. " + filename, e);
    }
  }
}
