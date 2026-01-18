package com.Project.SpiderSync.Enteties;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 768)
    private String url;

    @Column(nullable = false, unique = true)
    private String title;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;


}
