package it.unicam.cs.ids2425.eshop.model;

import it.unicam.cs.ids2425.articles.model.IArticle;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class Cart {
    private Map<IArticle, Integer> articles;

}
