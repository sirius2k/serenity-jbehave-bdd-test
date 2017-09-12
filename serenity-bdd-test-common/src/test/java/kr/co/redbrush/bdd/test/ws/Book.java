package kr.co.redbrush.bdd.test.ws;

import lombok.Data;

@Data
public class Book {
    String category;
    String author;
    String title;
    Float price;
}