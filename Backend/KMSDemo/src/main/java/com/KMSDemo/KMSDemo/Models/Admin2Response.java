package com.KMSDemo.KMSDemo.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin2Response {
	private String encryptedAdmin2Key;
	private String Status;

}
