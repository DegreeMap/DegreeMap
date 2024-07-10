package com.degreemap.DegreeMap.dmEntities.blocks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blocks")
public class BlockController {
    static class Request {
        public String name;
    }
}
