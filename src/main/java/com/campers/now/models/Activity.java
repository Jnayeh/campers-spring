package com.campers.now.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String label;
    private String image;
    private String description;
    private int discount;

    private float price;
    private float duration;
    private int number;
    private int capacity;
    private boolean isActive;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant modifiedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private CampingCenter campingCenter;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    @JsonIgnore
    private List<FeedBack> feedBacks;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    @JsonIgnore
    private List<Reclamation> reclamations;*/
}