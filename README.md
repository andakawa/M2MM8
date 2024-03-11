# M2MM8 - Machine 2 Machine API Request Testing Tool

Hey there! This is a fun project I've been working on. It's a tool for testing Machine-to-Machine (M2M) API requests. It is built with Kotlin and uses the Compose for Desktop library for the UI.
The main purpose was to test Compose for Desktop. The side-effect was this little daily helper that automates the boring stuff.

## What's inside?

- Fields to enter your Client ID, Client Secret, Client Scope, Auth Server URL, and API Endpoint.
- A dropdown to select the HTTP method for the API request (GET, POST, PUT, DELETE).
- A history dropdown that shows your previous inputs and lets you quickly fill the input fields with a previous set.
- A button to trigger the API call and display the result in a text field.
- An alert dialog for error handling that shows the error message.

## How to Use

1. Enter the Client ID, Client Secret, and Client Scope of your client under test.
2. Enter the URL of the auth endpoint.
3. Enter the endpoint of the API under test.
4. Select the HTTP method for the API request.
5. Click the "Request" button to trigger the API call. The result will be displayed in the text field below the button.

## Code Structure

The main Composable function `App()` contains the UI elements and their state. The `main()` function sets up the window and calls `App()`.

The `callStack()` function orchestrates the API call. It first gets the access token with `getAccessToken()`, then authenticates the client with `authenticateClient()`, and finally calls the endpoint with `callEndpoint()`.

## Running the Project

This project uses Gradle for building and running. You can run the project with the following command:

```bash
./gradlew run
```

## Download

If you don't want to compile the project yourself, you can download a pre-compiled version from the [Releases](https://github.com/andakawa/M2MM8/releases) page of this repository.

## Contributing

If you want to contribute to this project, feel free to fork the repository, make your changes, and open a pull request. You can also open an issue if you find a bug or have a suggestion for improving the project.

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

This project was created by [andakawa](https://github.com/andakawa).
