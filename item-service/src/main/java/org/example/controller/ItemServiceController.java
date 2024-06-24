package org.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.clent.ItemClent;
import org.example.domain.R;
import org.example.dto.ItemDTO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemServiceController {

//    @Autowired
    public final RestTemplate restTemplate;
    public final DiscoveryClient discoveryClient;
//    private final ItemClent itemClent;

    @GetMapping("/getUser")
    @ApiOperation("getUser")
    public R getUser() {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("hm-service");
//        如何集合为空，直接跳出
        if(CollUtil.isEmpty(serviceInstanceList)){
            return R.error("二级服务调用错误");
        }
//        获取随机数
        Integer suiji = RandomUtil.randomInt(serviceInstanceList.size());
        log.info(suiji.toString());
//        随机获取服务
        ServiceInstance instance = serviceInstanceList.get(suiji);
//        获取服务uri
        URI uri = instance.getUri();


        ResponseEntity<ItemDTO> exchange = restTemplate.exchange(
                 uri+"/items/317578",
                HttpMethod.GET,
                null,
                ItemDTO.class
        );
        if(!exchange.getStatusCode().is2xxSuccessful()){
            return R.error("查询失败");
        }
        ItemDTO itemDTOList = exchange.getBody();

//        ItemDTO itemDTO = itemClent.querItemByIds("317578");
        Router router = new Router();
        router.setName("Admin");
        router.setJiage("10");
        router.setItemDTO(itemDTOList);
        return R.ok(router);
//        return R.ok("你好");
    }

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Router {
    public String name;
    public String jiage;
    public ItemDTO itemDTO;

}
