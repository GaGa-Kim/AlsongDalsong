package AlsongDalsong_backend.AlsongDalsong.web.controller;

import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_CATEGORY;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_DATE;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_DECISION;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_EMAIL;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_IMPORTANCE;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_INTRODUCE;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_KAKAO_ID;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_LINK;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_NAME;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_NICKNAME;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_OLD;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_POST_CONTENT;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_POST_ID;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_PROFILE;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_REASON;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_TODO;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_USER_ID;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_WHAT;
import static AlsongDalsong_backend.AlsongDalsong.TestConstants.VALID_WHO;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import AlsongDalsong_backend.AlsongDalsong.config.SecurityConfig;
import AlsongDalsong_backend.AlsongDalsong.config.jwt.JwtRequestFilter;
import AlsongDalsong_backend.AlsongDalsong.domain.post.Post;
import AlsongDalsong_backend.AlsongDalsong.domain.user.User;
import AlsongDalsong_backend.AlsongDalsong.service.post.PostService;
import AlsongDalsong_backend.AlsongDalsong.web.dto.post.PostResponseDto;
import AlsongDalsong_backend.AlsongDalsong.web.dto.post.PostSaveRequestVO;
import AlsongDalsong_backend.AlsongDalsong.web.dto.post.PostUpdateRequestVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 게시글 컨트롤러 테스트
 */
@WebMvcTest(controllers = PostController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
        })
@WithMockUser(username = "테스트_최고관리자", roles = {"USER"})
class PostControllerTest {
    private Post post;
    private PostSaveRequestVO postSaveRequestVO;
    private PostUpdateRequestVO postUpdateRequestVO;
    private PostResponseDto postResponseDto;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(VALID_USER_ID)
                .kakaoId(VALID_KAKAO_ID)
                .name(VALID_NAME)
                .email(VALID_EMAIL)
                .nickname(VALID_NICKNAME)
                .profile(VALID_PROFILE)
                .introduce(VALID_INTRODUCE)
                .build();

        post = Post.builder()
                .id(VALID_POST_ID)
                .todo(VALID_TODO)
                .category(VALID_CATEGORY)
                .who(VALID_WHO)
                .old(VALID_OLD)
                .date(VALID_DATE)
                .what(VALID_WHAT)
                .content(VALID_POST_CONTENT)
                .link(VALID_LINK)
                .importance(VALID_IMPORTANCE)
                .decision(VALID_DECISION)
                .reason(VALID_REASON)
                .build();
        post.setUser(user);
        postSaveRequestVO = PostSaveRequestVO.builder()
                .email(post.getUserId().getEmail())
                .todo(post.getTodo().getTodo())
                .category(post.getCategory().getCategory())
                .what(post.getWhat())
                .old(post.getOld().getOld())
                .date(post.getDate())
                .who(post.getWho().getWho())
                .content(post.getContent())
                .link(post.getLink())
                .importance(post.getImportance())
                .photos(null)
                .build();
        postUpdateRequestVO = PostUpdateRequestVO.builder()
                .id(post.getId())
                .email(post.getUserId().getEmail())
                .todo(post.getTodo().getTodo())
                .category(post.getCategory().getCategory())
                .what(post.getWhat())
                .old(post.getOld().getOld())
                .date(post.getDate())
                .who(post.getWho().getWho())
                .content(post.getContent())
                .link(post.getLink())
                .importance(post.getImportance())
                .photos(null)
                .deleteId(null)
                .build();
        List<Long> photoId = new ArrayList<>();
        Pair<Long, Long> vote = Pair.of(0L, 0L);
        postResponseDto = new PostResponseDto(post, photoId, vote);
    }

    @Test
    void testPostAdd() throws Exception {
        when(postService.addPostWithPhotos(any(), any())).thenReturn(postResponseDto);

        mockMvc.perform(post("/api/post/save")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(new ObjectMapper().writeValueAsString(postSaveRequestVO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postUpdateRequestVO.getId()));

        verify(postService, times(1)).addPostWithPhotos(any(), any());
    }

    @Test
    void testPostDetails() throws Exception {
        when(postService.findPostDetailByPostId(any())).thenReturn(postResponseDto);

        mockMvc.perform(get("/api/post/inquire")
                        .param("id", String.valueOf(post.getId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postUpdateRequestVO.getId()));

        verify(postService, times(1)).findPostDetailByPostId(any());
    }

    @Test
    void testPostLatestList() throws Exception {
        when(postService.findLatestPosts(any())).thenReturn(Collections.singletonList(postResponseDto));

        mockMvc.perform(get("/api/post/inquireLatest")
                        .param("todo", post.getTodo().getTodo())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(postService, times(1)).findLatestPosts(any());
    }

    @Test
    void testPostPopularList() throws Exception {
        when(postService.findPopularPosts(any())).thenReturn(Collections.singletonList(postResponseDto));

        mockMvc.perform(get("/api/post/inquirePopular")
                        .param("todo", post.getTodo().getTodo())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(postService, times(1)).findPopularPosts(any());
    }

    @Test
    void testPostCategoryList() throws Exception {
        when(postService.findPostsByCategory(any(), any())).thenReturn(Collections.singletonList(postResponseDto));

        mockMvc.perform(get("/api/post/inquireCategory")
                        .param("todo", post.getTodo().getTodo())
                        .param("category", post.getCategory().getCategory())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(postService, times(1)).findPostsByCategory(any(), any());
    }

    @Test
    void testPostUserList() throws Exception {
        when(postService.findUserPosts(any())).thenReturn(Collections.singletonList(postResponseDto));

        mockMvc.perform(get("/api/post/my")
                        .param("email", post.getUserId().getEmail())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(postService, times(1)).findUserPosts(any());
    }

    @Test
    void testPostModify() throws Exception {
        when(postService.modifyPost(any(), any(), any())).thenReturn(postResponseDto);

        mockMvc.perform(put("/api/post/update")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(new ObjectMapper().writeValueAsString(postUpdateRequestVO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postUpdateRequestVO.getId()));

        verify(postService, times(1)).modifyPost(any(), any(), any());
    }

    @Test
    void testPostRemove() throws Exception {
        when(postService.removePost(any(), any())).thenReturn(true);

        mockMvc.perform(delete("/api/post/delete")
                        .param("id", String.valueOf(post.getId()))
                        .param("email", post.getUserId().getEmail())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(postService, times(1)).removePost(any(), any());
    }

    @Test
    void testPostModifyDecision() throws Exception {
        when(postService.modifyPostDecision(any(), any(), any(), any())).thenReturn(postResponseDto);

        mockMvc.perform(put("/api/post/updateDecision")
                        .param("id", String.valueOf(post.getId()))
                        .param("email", post.getUserId().getEmail())
                        .param("decision", post.getDecision().getDecision())
                        .param("reason", post.getReason())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postUpdateRequestVO.getId()));

        verify(postService, times(1)).modifyPostDecision(any(), any(), any(), any());
    }
}