package com.api.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {

private static ExtentReports extent;
private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

public static ExtentReports createInstance(){
    if(extent==null){
        String fileName = generateReportFileName();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/reports/" + fileName);

        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("API Test Report");
        sparkReporter.config().setReportName("REST API Test Results");

        System.out.println(fileName);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("User" , System.getProperty("user.name"));

    }
    return extent;

}

    private static String generateReportFileName() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss");
    return "APITestReport_" + dtf.format(LocalDateTime.now()) + ".html";
    }

    public static void startTest(String testName){
    ExtentTest extentTest = createInstance().createTest(testName);
    test.set(extentTest);
    }


    public static void logExeRequest(FilterableRequestSpecification requestSpec) {
    StringBuilder requestDetails = new StringBuilder();
    requestDetails.append("<pre>");
    requestDetails.append("Request Method: " ).append(requestSpec.getMethod()).append("\n");
    requestDetails.append("Request URI: " ).append(requestSpec.getURI()).append("\n");
    requestDetails.append("Request Headers: " ).append("\n");

    for(Header header : requestSpec.getHeaders()){
            requestDetails.append("    ").append(header.getName()).append(": ")
                    .append(header.getValue()).append("\n");
    }

    if(requestSpec.getBody() != null) {
        requestDetails.append("Request Body: ").append("\n");
        requestDetails.append(requestSpec.getBody().toString());
    }
    requestDetails.append("</pre>");

    test.get().log(Status.INFO, "Request Details: " + requestDetails.toString());

    }

    public static void logExeResponse(Response response) {

        StringBuilder responseDetails = new StringBuilder();
        responseDetails.append("<pre>");
        responseDetails.append("Response Status: " ).append(response.getStatusCode()).append("\n");
        responseDetails.append("Response Headers: " ).append("\n");



        for(Header header : response.getHeaders()){
            responseDetails.append("    ").append(header.getName()).append(": ")
                    .append(header.getValue()).append("\n");
        }

        responseDetails.append("Response Body: ").append("\n");
        responseDetails.append(response.getBody().prettyPrint());
        responseDetails.append("</pre>");

        test.get().log(Status.INFO, "Response Details: " + responseDetails.toString());

        // Log Based on the Status Code
        if(response.getStatusCode() >= 200 && response.getStatusCode() < 300 ){
            test.get().pass("Request Completed Successfully!!");
        }else {
            test.get().fail("Request failed with Status Code : " + response.getStatusCode());

        }

    }

    public static void endTest(){
       if(extent!= null){
           extent.flush();
       }
    }
}
