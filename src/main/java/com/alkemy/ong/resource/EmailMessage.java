package com.alkemy.ong.resource;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class EmailMessage {
	
	private String to;
	private String subject;
	private String message;
	
}
