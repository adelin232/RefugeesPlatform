package com.project.statuses;

import com.project.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class UserStatus {
    private User user;
    private String status;
    private String message;
}
