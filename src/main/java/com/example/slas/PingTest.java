package com.example.slas;

import com.example.slas.model.Ping;
import com.example.slas.repository.PingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class PingTest implements CommandLineRunner {

    @Autowired
    private PingRepository pingRepository;

    @Override
    public void run(String... args) throws Exception {
        Ping ping = new Ping();
        ping.setMesaj("Bağlantı başarılı!");
        pingRepository.save(ping);
        System.out.println("✅ Test verisi eklendi: Bağlantı başarılı!");
    }
}
