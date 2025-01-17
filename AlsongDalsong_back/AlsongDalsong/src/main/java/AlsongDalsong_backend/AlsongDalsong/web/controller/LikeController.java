package AlsongDalsong_backend.AlsongDalsong.web.controller;

import AlsongDalsong_backend.AlsongDalsong.constants.Message;
import AlsongDalsong_backend.AlsongDalsong.service.like.LikeService;
import AlsongDalsong_backend.AlsongDalsong.web.dto.like.LikeRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 댓글 좋아요 컨트롤러
 */
@Api(tags = {"Like API (댓글 좋아요 API)"})
@RestController
@Validated
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/save")
    @ApiOperation(value = "댓글 좋아요 작성/취소", notes = "댓글에 좋아요를 작성한 후, true를 리턴합니다. 댓글이 이미 좋아요일 경우 좋아요가 취소되고 false를 리턴합니다. ")
    @ApiImplicitParam(name = "likeSaveRequestDto", value = "좋아요 작성 정보", required = true)
    public ResponseEntity<Boolean> likeSave(@RequestBody @Valid LikeRequestDto likeSaveRequestDto) {
        return ResponseEntity.ok().body(likeService.saveLike(likeSaveRequestDto));
    }

    @PostMapping("/check")
    @ApiOperation(value = "사용자별 댓글에 따른 좋아요 여부 조회", notes = "사용자별로 댓글에 좋아요를 눌렀는지 조회합니다. 좋아요 했다면 true, 그렇지 않다면 false를 리턴합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "댓글 id", example = "1"),
            @ApiImplicitParam(name = "email", value = "이메일", example = "1234@gmail.com")
    })
    public ResponseEntity<Boolean> likeDetails(@RequestParam @NotNull(message = Message.INPUT_COMMENT_ID) Long id,
                                               @RequestParam @Email(message = Message.INPUT_EMAIL) String email) {
        return ResponseEntity.ok().body(likeService.findLike(id, email));
    }
}
