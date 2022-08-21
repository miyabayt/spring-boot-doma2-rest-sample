package com.bigtreetc.sample.doma.controller.system.uploadfiles;

import com.bigtreetc.sample.doma.base.util.FileUtils;
import com.bigtreetc.sample.doma.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ListApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.util.MultipartFileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "ファイルアップロード")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UploadFileController extends AbstractRestController implements InitializingBean {

  @Value(
      "${application.fileUploadLocation:#{systemProperties['java.io.tmpdir']}}") // 設定ファイルに定義されたアップロード先を取得する
  String fileUploadLocation;

  /**
   * ファイルの一覧を返します。
   *
   * @return
   */
  @Operation(summary = "ファイル一覧取得", description = "ファイルの一覧を取得します。")
  @PreAuthorize("hasAuthority('uploadFile')")
  @GetMapping("/files")
  public ApiResponse listFiles() {
    // ファイル名のリストを作成する
    val location = Paths.get(fileUploadLocation);
    val filePaths =
        FileUtils.listAllFiles(location).stream()
            .map(path -> path.getFileName().toString())
            .toList();

    val response = new ListApiResponseImpl();
    response.success(filePaths);

    return response;
  }

  /**
   * ファイル内容をレスポンスします。
   *
   * @param filename
   * @return
   */
  @Operation(summary = "ファイル内容表示", description = "ファイルの内容を表示します。")
  @PreAuthorize("hasAuthority('uploadFile')")
  @GetMapping(path = "/file/{filename:.+}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    val resource = FileUtils.loadFile(Paths.get(fileUploadLocation), filename);
    return toResponseEntity(resource, filename);
  }

  /**
   * ファイルをダウンロードします。
   *
   * @param filename
   * @return
   */
  @Operation(summary = "ファイルダウンロード", description = "ファイルをダウンロードします。")
  @PreAuthorize("hasAuthority('uploadFile')")
  @GetMapping(
      path = "/file/download/{filename:.+}",
      produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
    val resource = FileUtils.loadFile(Paths.get(fileUploadLocation), filename);
    return toResponseEntity(resource, filename, true);
  }

  /**
   * ファイルをアップロードします。
   *
   * @param file
   * @return
   */
  @Operation(summary = "ファイルアップロード", description = "ファイルをアップロードします。")
  @PreAuthorize("hasAuthority('uploadFile')")
  @PostMapping(path = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse uploadFile(@RequestPart("file") MultipartFile file) {
    MultipartFileUtils.saveFile(Paths.get(fileUploadLocation), file);

    val response = new SimpleApiResponseImpl().success();
    response.success();

    return response;
  }

  @Override
  public void afterPropertiesSet() {
    // アップロードディレクトリ
    val location = Paths.get(fileUploadLocation);

    // ディレクトリがない場合は作成する
    FileUtils.createDirectories(location);
  }
}
