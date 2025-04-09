package com.mongodb.infra.adapters.entity;

import com.mongodb.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    private String document;
    private String name;
    private int age;

    public static UserEntity fromUser(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .document(user.getDocument())
                .name(user.getName())
                .age(user.getAge())
                .build();
    }

    public User toUser() {
        return User.builder()
                .id(this.id)
                .document(this.document)
                .name(this.name)
                .age(this.age)
                .build();
    }
} 