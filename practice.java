import java.io.File;
import java.util.Scanner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class practice {
    // Base URL for the API
    private static final String BASE_URL = "https://ymcaorg.affnetz.com/api";
    private static final String TOKEN_API = "/get-access-token";
    private static final String CSV_UPLOAD_API = "/entity/external-csv/upload"; // Removed trailing space

    public static void main(String[] args) {
        // Step 1: Fetch the access token
        String accessToken = getAccessToken("ghtn_client_id", "@@ghtn225#123");

        if (accessToken == null || accessToken.isEmpty()) {
            System.err.println("Failed to retrieve access token. Exiting.");
            return;
        }
        Scanner sc=new Scanner(System.in);
        System.out.print("Give the file: ");
        String filePath=sc.next();
        // Step 2: Upload the CSV file using the token
        uploadCsv(accessToken, filePath);
    }

    // Method to fetch the access token
    public static String getAccessToken(String clientId, String clientSecret) {
        RestAssured.baseURI = BASE_URL;

        // Request payload
        String payload = "{\n" +
                "  \"client_id\": \"" + clientId + "\",\n" +
                "  \"client_secret\": \"" + clientSecret + "\"\n" +
                "}";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post(TOKEN_API);

        // Debugging: Print response
        System.out.println("Access Token Response: " + response.asPrettyString());

        if (response.statusCode() != 200) {
            System.err.println("Failed to retrieve access token. Status: " + response.statusCode());
            return null;
        }

        // Extract token safely
        try {
            String accessToken = response.jsonPath().getString("data.access_token");
            if (accessToken == null) {
                System.err.println("Access token not found in response.");
                return null;
            }
            System.out.println("Access Token: " + accessToken);
            return accessToken;
        } catch (Exception e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            return null;
        }
    }

    // Method to upload the CSV file
    public static void uploadCsv(String accessToken, String filePath) {
        RestAssured.baseURI = BASE_URL;

        // Prepare the file
        File csvFile = new File(filePath);
        if (!csvFile.exists()) {
            System.err.println("File not found: " + filePath);
            return;
        }

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .multiPart("file", csvFile)
                .when()
                .post(CSV_UPLOAD_API);

        // Debugging: Print response
        System.out.println("CSV Upload Response: " + response.asPrettyString());

        if (response.statusCode() != 200) {
            System.err.println("CSV upload failed. Status: " + response.statusCode());
        }
    }
	}

	


