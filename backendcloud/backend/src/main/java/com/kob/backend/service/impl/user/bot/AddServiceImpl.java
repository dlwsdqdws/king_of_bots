package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if(title == null || title.length() == 0){
            map.put("error_msg", "Bot title can not be empty");
            return map;
        }

        if(title.length() > 100){
            map.put("error_msg", "Bot title length must not be longer than 100");
            return map;
        }

//        if(description == null || description.length() == 0){
//            map.put("error_msg", "description can not be empty");
//            return map;
//        }
        if(description == null || description.length() == 0){
            description = "This guy is too lazy to leave a word.";
        }

        if(description.length() > 500){
            map.put("error_msg", "Bot description length must not be longer than 100");
            return map;
        }

        if(content == null || content.length() == 0){
            map.put("error_msg", "Bot content can not be empty");
            return map;
        }

        if(content.length() > 10000){
            map.put("error_msg", "Bot content length must not be longer than 100");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(
                null,
                user.getId(),
                title,
                description,
                content,
                now,
                now
        );

        botMapper.insert(bot);
        map.put("error_msg", "success");

        return map;
    }
}
