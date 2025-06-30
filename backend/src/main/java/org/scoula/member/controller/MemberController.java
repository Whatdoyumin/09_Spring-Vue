package org.scoula.member.controller;

import java.io.File;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.common.util.UploadFiles;
import org.scoula.member.dto.MemberDTO;
import org.scoula.member.dto.MemberJoinDTO;
import org.scoula.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

  final MemberService service;

  @GetMapping("/checkusername/{username}")
  public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
    return ResponseEntity.ok().body(service.checkDuplicate(username));
  }

  @PostMapping("")
  public ResponseEntity<MemberDTO> join(MemberJoinDTO member) {
    return ResponseEntity.ok(service.join(member));
  }

  @GetMapping("/{username}/avatar")
  public void getAvatar(@PathVariable String username, HttpServletResponse response) {
    String avatarPath = "/Users/whatdoyumin/upload/avatar/" + username + ".png";
    File file = new File(avatarPath);
    if (!file.exists()) {  // 아바타 등록이 없는 경우, 디폴트 아바타 이미지 사용
      file = new File("/Users/whatdoyumin/upload/avatar/unknown.jpeg");
    }

    UploadFiles.downloadImage(response, file);
  }
}
