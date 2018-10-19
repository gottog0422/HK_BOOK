package com.example.sh.hk_book.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Content {
    public static int INCOME = 0;
    public static int EXPENSE = 1;
    private Integer id;
    private Integer cost;
    private Integer kind;
    private Integer year;
    private Integer month;
    private Integer day;
}
