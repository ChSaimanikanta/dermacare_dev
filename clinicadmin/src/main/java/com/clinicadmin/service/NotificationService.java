package com.clinicadmin.service;


import org.springframework.http.ResponseEntity;
import com.clinicadmin.dto.ImageForNotificationDto;


public interface NotificationService {
	
	public ResponseEntity<?> storeImageForNotification(ImageForNotificationDto imageForNotificationDto);
	public byte[] getImageForNotification();				


}
