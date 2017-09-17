package kr.co.redbrush.bdd.test.ws.helper;

import kr.co.redbrush.bdd.test.ws.helper.Book;
import lombok.Data;

import java.util.List;

@Data
public class Store {
    List<Book> books;
}
