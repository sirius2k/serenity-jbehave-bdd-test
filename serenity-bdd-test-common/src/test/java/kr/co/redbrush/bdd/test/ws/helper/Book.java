package kr.co.redbrush.bdd.test.ws.helper;

import lombok.Data;

@Data
public class Book {
    String category;
    String author;
    String title;
    Float price;
}