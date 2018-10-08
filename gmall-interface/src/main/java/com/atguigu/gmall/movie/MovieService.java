package com.atguigu.gmall.movie;

import com.atguigu.gmall.user.Movie;

public interface MovieService {

    /**
     * 返回movie
     * @param id
     * @return
     */
    public Movie getMovie(String id);
}
