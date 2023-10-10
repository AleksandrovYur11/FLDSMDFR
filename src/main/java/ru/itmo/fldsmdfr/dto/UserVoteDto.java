package ru.itmo.fldsmdfr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.itmo.fldsmdfr.models.User;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserVoteDto {
    private Long breakfastDishId;
    private Long lunchDishId;
    private Long dinnerDishId;
    private User user;
}
