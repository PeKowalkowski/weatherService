# Worldwide windsurfers weather service

This application provides a **16-day weather forecast** for surfers across five selected locations. It integrates with an external API to fetch weather data and offers several features including:

- **User Registration and Login**: Secure access via Spring Security.
- **Best Surfing Location Finder**: Identify the optimal surfing spot based on the date.
- **Location Management**: Add or remove surfing locations.

## Configuration

Below is the configuration for `application.properties`. Make sure to replace placeholders with your actual credentials and secrets.

```properties
# Application Name
spring.application.name=weatherService

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/weatherService?useSSL=true&serverTimezone=UTC&useLegacyDatetimeCode=false
spring.datasource.username=root
spring.datasource.password=your password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type=TRACE

# Other Settings
spring.main.allow-circular-references=true
server.error.whitelabel.enabled=false

# JWT Configuration
jwt.rsa-private-key=classpath:certs/privateKey.pem
jwt.rsa-public-key=classpath:certs/publicKey.pem
jwt.secret=your JWT secret

# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.protocol=smtp
spring.mail.port=587
spring.mail.username=your email
spring.mail.password=your password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

# Weather API Configuration
weather.api.key=your API key
weather.api.base-url=https://api.weatherbit.io/v2.0/forecast/daily?city=%s&country=%s&key=%s

````
## Gmail Configuration

To send emails using Gmail, you need to configure your Gmail account to allow SMTP access. Follow the instructions in the [Gmail SMTP Configuration Guide](https://kinsta.com/blog/gmail-smtp-server/) to set up your Gmail account for sending emails.

## Key Configuration

To complete the setup, you need to create a `certs` folder within the `resources` directory of your project and add your `privateKey.pem` and `publicKey.pem` files to it.

1. **Create the `certs` Folder**: In your project's `resources` directory, create a folder named `certs`.

2. **Add Key Files**: Place your `privateKey.pem` and `publicKey.pem` files into the `certs` folder.

3. **Generate Key Files**: If you need to generate RSA keys, you can use the [RSA Key Generator](https://travistidwell.com/jsencrypt/demo/) to create your `privateKey.pem` and `publicKey.pem` files.

Make sure to keep your keys secure and do not share them publicly.

## SQL Query to Insert a New User

Use the following SQL query to insert a new user into your `USER` table:

```sql
INSERT INTO USER (first_name, last_name, email, password, is_admin, created_at, ROLES)
VALUES ('user', '', 'testUser@gmail.com', '$2a$12$g6l5ZBi3GWDK3L1zwZdZTuJ1qOQgxO8/SQvE9VtIVxfAPrsvFlvUy', false, '2024-09-08 10:35:55', 'ROLE_USER');

```

## Logging In with Postman

### 1. Login Settings

1. **Open Postman.**
2. **Create a new HTTP request.**
3. **Set the method to `POST`.**
4. **Enter the following URL:**

    ```
    http://localhost:8080/sign-in
    ```

5. **Go to the "Authorization" tab.**
6. **Select "Basic Auth" from the dropdown list.**
7. **Enter the login credentials:**
  - **Username:** `your mail` (test user)
  - **Password:** `your password` (password used in the SQL query)

### 2. Execute the Request

- Click the "Send" button to submit the request.

### 3. Response

If the login is successful, you will receive a JSON response with an access token:

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJwYWJsbyIsInN1YiI6Imlkb250dXNlQGVudW5hbC5jb20iLCJleHAiOjE3MjU4MDYzMjAsImlhdCI6MTcyNTgwNjI2MCwic2NvcGUiOiJSRUFEIn0.JQ2JN_xZR0wLq5g-ZnJuVhFyxlzyTJfbenpIgB8hxnbjCzVIUWWl5VLW2OwVZobyKY9XfS75MTT4y2LzEEH2tTFkbF00q6njIpEstxQynuTaujzrxkXV6iYqZqHQMAlw92GYSt_iQExViJkPlms-tJCgnWf2qkkxOPuIc9PWbnfGHyHs8Ojv-u0KIvGhqg4O_QOWjhv91DJEPsUenWxwYpYV5a9DJZ8SaYvnOAHTlJzZQJjw8skqe_JhomFFfMiOzig4VZiGsJQ-nVOi10OSV3jEhj2CqT8HVIcRSXfpN2vhEdbi94DoghAwdsvlOL5IxknCNSpgv1Dg0mix5fL61A",
    "access_token_expiry": 900,
    "token_type": "Bearer",
    "user_name": "testUser"
}
```
![Sign-In](C:\Users\Paweł\OneDrive\Pulpit\signin.png)

