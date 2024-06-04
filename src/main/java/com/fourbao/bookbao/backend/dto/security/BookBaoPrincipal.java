package com.fourbao.bookbao.backend.dto.security;

import com.fourbao.bookbao.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * custom user details
 */
@NoArgsConstructor
@AllArgsConstructor
@Component
public class BookBaoPrincipal implements UserDetails {
    private long id;    // db에서 pk 값
    private String schoolId;
    private String email;
    private Collection<SimpleGrantedAuthority> authorities;


    public BookBaoPrincipal(String name, String schoolId, String email) {
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }


    public static BookBaoPrincipal buildBookbaoPrincipalEntity(String name, String schoolId, String email) {
        return new BookBaoPrincipal(
                name,
                schoolId,
                email
        );
    }

    public static BookBaoPrincipal createBookbaoPrincipalByUserEntity(User user) {
        return BookBaoPrincipal.buildBookbaoPrincipalEntity(
                user.getName(),
                user.getSchoolId(),
                user.getEmail()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    /** schoolId도 고유한 값이므로*/
    @Override
    public String getUsername() {
        return schoolId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
