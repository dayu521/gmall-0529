package com.atguigu.gmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.movie.MovieService;
import com.atguigu.gmall.user.Movie;
import com.atguigu.gmall.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Reference
    MovieService movieService;


    @ResponseBody
    @RequestMapping("/movie")
    public Movie buyTicket(String userId,String mid){
        Movie movie = movieService.getMovie(mid);

        return movie;
    }
}
