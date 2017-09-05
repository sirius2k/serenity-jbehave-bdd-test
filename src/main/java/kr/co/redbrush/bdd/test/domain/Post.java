package kr.co.redbrush.bdd.test.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Created by kwpark on 13/03/2017.
 */
@Builder
@Data
public class Post {
    private Integer id;
    private String title;
    private String body;
    private Integer userId;
}
