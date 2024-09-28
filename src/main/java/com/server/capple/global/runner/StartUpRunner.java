package com.server.capple.global.runner;

import com.server.capple.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUpRunner implements ApplicationRunner {

    private final BoardService boardService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boardService.createMaterializedViewIfNotExists();
    }
}
