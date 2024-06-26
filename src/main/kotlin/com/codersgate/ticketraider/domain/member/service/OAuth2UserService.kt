package com.codersgate.ticketraider.domain.member.service

import com.codersgate.ticketraider.domain.member.dto.OAuth2UserInfo
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserService(
    private val memberService: MemberService
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val originUser = super.loadUser(userRequest)
        val provider = userRequest.clientRegistration.clientName
        return OAuth2UserInfo.of(provider, userRequest, originUser)
            .also { memberService.socialSignUpOrLogin(it) }
    }
}