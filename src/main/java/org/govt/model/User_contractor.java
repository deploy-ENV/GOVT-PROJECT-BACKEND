package org.govt.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Add;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "contractor")
@Getter
@Setter
public class User_contractor implements UserDetails {
    @Id
    private String id;

    private String name;
    private String password;
    private String username;
    private String DOB;
    private long phone;
    private String email;
    private String gst_number;
    private  Address address;
    private boolean approved;
    private long createdAt = System.currentTimeMillis();
    


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
