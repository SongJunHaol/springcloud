package org.example.clent;

import org.example.dto.ItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("hm-service")
public interface ItemClent {
    @GetMapping("/item")
    ItemDTO querItemByIds(@RequestParam("id") String id);
}
