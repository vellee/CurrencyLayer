import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CurrencyLayerTest {
    public static final String ACCESS_KEY = "4141c0c274cb91e1801972e794897f43";
    public static final String CURRENCY = "&currencies=CAD,EUR,NIS,RUB";
    public static final String SINGLE_CURRENCY = "&currencies=EUR";
    public static final String INCORRECT_CURRENCY = "&currencies=NONE";
    public static final String DATE = "&date=2020-07-19";
    public static final String INVALID_DATE = "&date=2022-07-19";
    public static final String CONVERT_FROM = "&USD";
    public static final String CONVERT_TO = "&UAH";
    public static final String AMOUNT_TO_CONVERT = "&100";
    public static final String FORMAT = "&format=1";
    public static final String TIME_FRAME = "&start_date=2020-07-19&end_date=2020-07-20";
    public static Response response;
    public static final Logger logger = LogManager.getLogger(CurrencyLayerTest.class);

    // Test for invalid access key for LIVE endpoint
    @Test
    public void invalidAccessKeyTest() {
        logger.info("Test for invalid access key for LIVE endpoint");
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.LIVE_RATE_ENDPOINT + "?access_key=" + "bla" + CURRENCY + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(101));
        response.then().body("error.info", containsString("You have not supplied a valid API Access Key"));
        System.out.println(response.asString());

    }

    // Test for valid access key for LIVE endpoint
    @Test
    public void validAccessKeyTest() {
        logger.info("Test for valid access key for LIVE endpoint");
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.LIVE_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        System.out.println(response.asString());
    }

    // Test for a correct response code with a missing access key for LIVE endpoint request
    @Test
    public void missingAccessKeyTest() {
        logger.info("Test for a correct response code with a missing access key for LIVE endpoint request");
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.LIVE_RATE_ENDPOINT + "?access_key=" + CURRENCY + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(101));
        response.then().body("error.info", containsString("have not supplied an API Access Key"));
        System.out.println(response.asString());
    }

    // Test for valid access key for HISTORY endpoint
    @Test
    public void validAccessKeyTestHistoryEndpoint() {
        logger.info("Test for valid access key for HISTORY endpoint");
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.HISTORICAL_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + DATE + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("historical", equalTo(true));
        System.out.println(response.asString());
    }

    // Test for invalid access key for HISTORY endpoint
    @Test
    public void invalidAccessKeyTestHistoryEndpoint() {
        logger.info("Test for invalid access key for HISTORY endpoint");
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.HISTORICAL_RATE_ENDPOINT + "?access_key=" + "bla" + CURRENCY + DATE + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(101));
        response.then().body("error.info", containsString("You have not supplied a valid API Access Key"));
        System.out.println(response.asString());
    }

    // Test for valid access key for CONVERT endpoint
    @Test
    public void validAccessKeyTestConvertEndpoint() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.CONVERT_ENDPOINT + "?access_key=" + ACCESS_KEY + CONVERT_FROM + CONVERT_TO + AMOUNT_TO_CONVERT + FORMAT);
        response.then().statusCode(200);
        //response.then().body("success", equalTo(true));
        System.out.println(response.asString());
    }

    // Test for correct response code for a Convert Endpoint request with Restricted Subscription Plan
    @Test
    public void restrictionResponseTestConvertEndpoint() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.CONVERT_ENDPOINT + "?access_key=" + ACCESS_KEY + CONVERT_FROM + CONVERT_TO + AMOUNT_TO_CONVERT + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(105));
        response.then().body("error.info", containsString("Access Restricted"));
        System.out.println(response.asString());
    }

    // Test for invalid access key for CONVERT endpoint
    @Test
    public void invalidAccessKeyTestConvertEndpoint() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.CONVERT_ENDPOINT + "?access_key=" + "bla" + CONVERT_FROM + CONVERT_TO + AMOUNT_TO_CONVERT + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(101));
        response.then().body("error.info", containsString("You have not supplied a valid API Access Key"));
        System.out.println(response.asString());
    }

    // Test for valid access key for Timeframe endpoint
    @Test
    public void validAccessKeyTestTimeframeEndpoint() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.TIME_FRAME_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + TIME_FRAME + FORMAT);
        response.then().statusCode(200);
        //response.then().body("success", equalTo(true));
        System.out.println(response.asString());
    }

    // Test for invalid access key for Timeframe endpoint
    @Test
    public void invalidAccessKeyTestTimeframeEndpoint() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.TIME_FRAME_ENDPOINT + "?access_key=" + "bla" + CURRENCY + TIME_FRAME + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(101));
        response.then().body("error.info", containsString("You have not supplied a valid API Access Key"));
        System.out.println(response.asString());
    }

    // Test for valid access key for Change endpoint
    @Test
    public void validAccessKeyTestChangeEndpoint() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.CHANGE_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + TIME_FRAME + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        System.out.println(response.asString());
    }

    // Test for invalid access key for Change endpoint
    @Test
    public void invalidAccessKeyTestChangeEndpoint() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.CHANGE_ENDPOINT + "?access_key=" + "bla" + CURRENCY + TIME_FRAME + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(101));
        response.then().body("error.info", containsString("You have not supplied a valid API Access Key"));
        System.out.println(response.asString());
    }

    // Test for API endpoint functionality for LIVE endpoint
    @Test
    public void endpointLiveFunctionalityTest() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.LIVE_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", notNullValue());
        response.then().body("terms", equalTo("https://currencylayer.com/terms"));
        response.then().body("privacy", equalTo("https://currencylayer.com/privacy"));
        response.then().body("timestamp", notNullValue());
        response.then().body("source", equalTo("USD"));
        response.then().body("quotes", notNullValue());
        System.out.println(response.asString());
    }

    // Test for a single currency input in Live Endpoint request
    @Test
    public void singleCurrencyLiveEndpointTest() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.LIVE_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + SINGLE_CURRENCY + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("quotes", containsString("USDEUR"));
        System.out.println(response.asString());

    }

    // Test for correct response code with incorrect currency code input in Live Endpoint request
    @Test
    public void incorrectCurrencyCodeLiveEndpointTest() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.LIVE_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + INCORRECT_CURRENCY + FORMAT);
        response.then().statusCode(200);
        response.then().body("error.code", equalTo(202));
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString("invalid Currency Codes"));
        System.out.println(response.asString());

    }

    // Test for multiple currencies input in Live Endpoint request
    @Test
    public void multipleCurrenciesTest() {

        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.LIVE_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        System.out.println(response.asString());
    }

    // Test for correct response code with missing date parameter in Historical Endpoint request
    @Test
    public void missingDateParameterResponseTest() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.HISTORICAL_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + FORMAT);
        response.then().statusCode(200);
        response.then().body("error.code", equalTo(301));
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString("have not specified a date"));
        System.out.println(response.asString());

    }

    // Test for date verification input in Historical Endpoint request
    @Test
    public void dateVerificationTestHistoryEndpoint() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.HISTORICAL_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + DATE + FORMAT);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("historical", equalTo(true));
        response.then().body("date", equalTo("2020-07-19"));
        response.then().body("timestamp", notNullValue());
        System.out.println(response.asString());

    }

    // Test for correct response code for invalid date input in Historical Endpoint request
    @Test
    public void invalidDateParameterResponseTest() {
        response = given().contentType("application/json").get(Consts.BASE_URL + Consts.HISTORICAL_RATE_ENDPOINT + "?access_key=" + ACCESS_KEY + CURRENCY + INVALID_DATE + FORMAT);
        response.then().statusCode(200);
        response.then().body("error.code", equalTo(302));
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString("have entered invalid date"));
        System.out.println(response.asString());

    }

}
