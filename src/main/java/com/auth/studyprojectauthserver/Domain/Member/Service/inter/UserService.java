package com.auth.studyprojectauthserver.Domain.Member.Service.inter;

import com.auth.studyprojectauthserver.Domain.Member.Dto.MemberResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    //MemberResponseDto getUserDtailsByEmail(String email);
    //Iterable<MemberEntity> getUserByAll();
}
