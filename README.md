Spring Boot Application that exposes an API for weather forecasts.

The weather forecasts are fethed from https://www.weatherapi.com/ and cached in H2 database.

To allow the usage of the weather api an API key is required. 
You need to pass it to the application as an environment variable API_KEY.
The API key can be acquired free of charge from https://www.weatherapi.com/
