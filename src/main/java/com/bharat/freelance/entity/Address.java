package com.bharat.freelance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstLine;
    private String secondLine;
    private String thirdLine;
    private String fourthLine;
    private String fifthLine;
    private String postCode;
    private Boolean preferNotToSay;
    private String dateMovedIn;

}
