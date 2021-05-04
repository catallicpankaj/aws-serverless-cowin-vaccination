package com.cowin.vaccination.function.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cowin.vaccination.function.model.CenterDetails;
import com.cowin.vaccination.function.model.SessionDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class CowinCoreLogic {

	public String processData(int requiredAge, String pincode) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String date = sdf.format(new Date());
		String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pincode
				+ "&date=" + date;
		HttpResponse<String> httpResponse = Unirest.get(url).asString();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, CenterDetails> outputResponse = new HashMap<>();
		ArrayNode centersData = (ArrayNode) mapper.readTree(httpResponse.getBody()).at("/centers");
		centersData.forEach(center -> {
			traverseNodeForData(mapper, outputResponse, center, requiredAge);
		});
		return mapper.writeValueAsString(outputResponse);
	}

	private void traverseNodeForData(ObjectMapper mapper, Map<String, CenterDetails> outputResponse, JsonNode center,
			int requiredAge) {
		CenterDetails centerDetails;
		try {
			Map<String, SessionDetails> dateWiseCenterDetails = new HashMap<>();
			center.at("/sessions").forEach(session -> {
				SessionDetails sd;
				try {
					if (session.at("/min_age_limit").asInt() == requiredAge
							&& session.at("/available_capacity").asInt() > 0) {
						sd = SessionDetails.builder()
								.minAgeLimit(center.at("/sessions").get(0).at("/min_age_limit").asInt())
								.availableCapacity(center.at("/sessions").get(0).at("/available_capacity").asInt())
								.availableSlots(mapper.readValue(
										mapper.writeValueAsString(center.at("/sessions").get(0).at("/slots")),
										TypeFactory.defaultInstance().constructCollectionType(List.class,
												String.class)))
								.build();
						dateWiseCenterDetails.put(session.at("/date").asText(), sd);
					}
				} catch (Exception w1) {

				}
			});
			if (!dateWiseCenterDetails.isEmpty()) {
				centerDetails = CenterDetails.builder().name(center.at("/name").asText())
						.address(center.at("/address").asText()).blockName(center.at("/block_name").asText())
						.pincode(center.at("/pincode").asText()).sessionDetails(dateWiseCenterDetails).build();
				outputResponse.put(center.at("/name").asText(), centerDetails);
			}
		} catch (Exception e) {
		}
	}
}
