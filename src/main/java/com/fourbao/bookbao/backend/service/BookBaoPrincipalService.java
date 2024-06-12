package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.security.BookBaoPrincipal;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookBaoPrincipalService implements UserDetailsService {
    private final UserRepository userRepository;

    // 학번으로 사용자 찾아서 createUserEntity
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findBySchoolId(username)
                .orElseThrow(() ->  new BaseException(BaseResponseStatus.NON_EXIST_USER));

        BookBaoPrincipal bookBaoPrincipalByUserEntity = BookBaoPrincipal.createBookbaoPrincipalByUserEntity(user);

        return bookBaoPrincipalByUserEntity;
    }
}
