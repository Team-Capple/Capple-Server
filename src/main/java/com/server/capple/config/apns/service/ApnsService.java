package com.server.capple.config.apns.service;

import java.util.List;

public interface ApnsService {
    <T> Boolean sendApns(T request, List<String> deviceToken);
}
