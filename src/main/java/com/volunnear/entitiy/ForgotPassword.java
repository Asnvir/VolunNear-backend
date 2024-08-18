package com.volunnear.entitiy;

import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpID;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expiryDate;

    @OneToOne
    private AppUser appUser;

}
