package com.alkemy.ong.dto;

import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PagesDTO<T> {

    private Page<T> page;
    private String previousPage;
    private String nextPage;

    public PagesDTO(Page page, String url) {
        this.page = page;
        this.previousPage = page.hasPrevious() ? url + page.previousOrFirstPageable().getPageNumber() : null;
        this.nextPage = page.hasNext() ? url + page.nextOrLastPageable().getPageNumber() : null;
    }
}