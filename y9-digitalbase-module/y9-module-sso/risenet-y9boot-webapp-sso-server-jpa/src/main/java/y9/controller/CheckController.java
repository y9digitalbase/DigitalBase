package y9.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import y9.util.Y9Context;
import y9.util.Y9Result;

@Controller
@RequestMapping(value = "/api")
@Slf4j
@RequiredArgsConstructor
public class CheckController {

    @ResponseBody
    @GetMapping(value = "/getRandom")
    public Y9Result<Object> getRandom() {
        try {
            // String[] rsaArr = RSAUtil.genKeyPair();
            // redisTemplate.opsForValue().set(rsaArr[0], rsaArr[1], 120, TimeUnit.SECONDS);
            return Y9Result.success(Y9Context.getProperty("y9.login.encryptionRsaPublicKey"), "获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Y9Result.failure("获取失败");
        }
    }

}
