package com.auth.studyprojectauthserver.Domain.Member.Service.impl;

import com.auth.studyprojectauthserver.Domain.Member.Dto.MemberResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Dto.ResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Service.inter.UserService;
import com.auth.studyprojectauthserver.Global.Config.GatewayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserService {
    private final GatewayConfig gatewayConfig;

    private final RestTemplate restTemplate;
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException{
        ResponseEntity<ResponseDto<MemberResponseDto>> response = restTemplate.exchange(
                gatewayConfig.getMemberUrl() + "/api/v1/member/" + loginId,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>(){}
        );
        log.info("Member Info : {}", response);
        MemberResponseDto member = response.getBody().getData();

        log.info("UserDetailsServiceImpl, member={}", member.getLoginId());

        if (Objects.isNull(member)) {
            throw new UsernameNotFoundException(loginId);
        }

        User user = new User(
                member.getLoginId(),
                member.getPassword(),
                member.getRoles()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        log.info("user={}", user);
        return user;
    }
}