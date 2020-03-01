package ro.atlas.service;

import org.springframework.stereotype.Service;

@Service
public interface CronService {
	public void keepaliveTask();
}
