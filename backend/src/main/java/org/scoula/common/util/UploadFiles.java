package org.scoula.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public class UploadFiles {

  /**
   * 파일을 지정된 디렉토리에 업로드하는 메서드
   *
   * @param baseDir 기본 저장 디렉토리
   * @param part    업로드된 파일 객체
   * @return 저장된 파일의 전체 경로
   * @throws IOException 파일 처리 중 오류 발생 시
   */
  public static String upload(String baseDir, MultipartFile part) throws IOException {
    // 기본 디렉토리 존재 여부 확인 및 생성
    File base = new File(baseDir);
    if (!base.exists()) {
      base.mkdirs();  // 중간 디렉토리까지 모두 생성
    }

    // 원본 파일명 획득
    String fileName = part.getOriginalFilename();

    // 고유한 파일명으로 대상 파일 생성
    File dest = new File(baseDir, UploadFileName.getUniqueName(fileName));

    // 업로드된 파일을 지정된 경로로 이동
    part.transferTo(dest);

    // 저장된 파일의 전체 경로 반환
    return dest.getPath();
  }

  /**
   * 파일 크기를 사용자 친화적 형태로 변환
   *
   * @param size 바이트 단위 파일 크기
   * @return 포맷된 문자열 (예: 1.2 MB)
   */
  public static String getFormatSize(Long size) {
    if (size <= 0) {
      return "0";
    }

    final String[] units = new String[]{"Bytes", "KB", "MB", "GB", "TB"};
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

    return new DecimalFormat("#,##0.#")
        .format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }

  public static void download(HttpServletResponse response, File file, String orgName)
      throws IOException {
    // 헤더 설정
    response.setContentType("application/download");
    response.setContentLength((int) file.length());

    String fileName = URLEncoder.encode(orgName, "UTF-8");  // 한글 파일명인 경우 인코딩 필수
    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

    // body 부분
    try (OutputStream os = response.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os)
    ) {
      Files.copy(file.toPath(), bos);
    }
  }

  public static void downloadImage(HttpServletResponse response, File file) {
    try {
      Path path = Path.of(file.getPath());
      String mimeType = Files.probeContentType(path);
      response.setContentType(mimeType);
      response.setContentLength((int) file.length());

      try (OutputStream os = response.getOutputStream();
          BufferedOutputStream bos = new BufferedOutputStream(os)) {
        Files.copy(path, bos);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
