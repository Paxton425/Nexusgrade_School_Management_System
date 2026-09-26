package com.nexusgrade.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeoLocationService {

    private static final Logger logger = LoggerFactory.getLogger(GeoLocationService.class);
    private final RestClient restClient;

    public GeoLocationService() {
        this.restClient = RestClient.builder().build();
    }

    /**
     * Resolves an IP address to a formatted location string (e.g., "Durban, South Africa").
     * Uses ip-api.com (free tier allows up to 45 requests per minute).
     */
    public String getLocationSummary(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank() || isLocalIp(ipAddress)) {
            return "Localhost / Internal Network";
        }

        try {
            String url = "http://ip-api.com/json/" + ipAddress;

            IpApiResponse response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(IpApiResponse.class);

            if (response != null && "success".equalsIgnoreCase(response.status())) {
                return String.format("%s, %s", response.city(), response.country());
            } else {
                logger.warn("Could not resolve location for IP: {}. Reason: {}",
                        ipAddress, response != null ? response.message() : "Null response");
            }
        } catch (Exception e) {
            logger.error("Failed to query IP geolocation API for IP: {}", ipAddress, e);
        }

        return "Unknown Location";
    }

    private boolean isLocalIp(String ip) {
        return "127.0.0.1".equals(ip)
                || "0:0:0:0:0:0:0:1".equals(ip)
                || ip.startsWith("192.168.")
                || ip.startsWith("10.")
                || ip.startsWith("172.16.")
                || ip.startsWith("172.31.");
    }

    // DTO mapped directly to ip-api.com JSON response structure
    private record IpApiResponse(
            String status,
            String message,
            String country,
            String regionName,
            String city,
            String query
    ) {}
}
