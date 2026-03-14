package com.kdw.boardapi.domain.member.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdw.boardapi.domain.member.dto.MemberCreateRequest;
import com.kdw.boardapi.domain.member.dto.MemberResponse;
import com.kdw.boardapi.domain.member.dto.MemberUpdateRequest;
import com.kdw.boardapi.domain.member.service.MemberService;
import com.kdw.boardapi.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    /**
     * 회원가입
     *
     * @param request {@link MemberCreateRequest} 회원가입 요청 DTO
     * @return 생성된 회원 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> join(@RequestBody @Valid MemberCreateRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.success(memberService.join(request)));
    }

    /**
     * 회원 단건 조회         
     * @param id 회원 ID
     * @return  회원 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable Long id) {
        return ResponseEntity.status(200).body(ApiResponse.success(memberService.getMember(id)));
    }

    /**
     * 회원 정보 업데이트
     * @param request {@link MemberUpdateRequest} 업데이트 요청 DTO
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(@PathVariable Long id, @RequestBody @Valid MemberUpdateRequest request){
        return ResponseEntity.status(200).body(ApiResponse.success(memberService.updateMember(id, request)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id){
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
    
}
