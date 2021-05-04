package com.cowin.vaccination.function.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CenterDetails {
	private String name;
	private String address;
	private String blockName;
	private String pincode;
	private Map<String, SessionDetails> sessionDetails;

}
